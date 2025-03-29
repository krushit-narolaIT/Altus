package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.enums.PaymentMode;
import com.krushit.common.enums.PaymentStatus;
import com.krushit.common.enums.RideRequestStatus;
import com.krushit.common.enums.RideStatus;
import com.krushit.common.exception.DBException;
import com.krushit.dto.RideCancellationDetails;
import com.krushit.model.Ride;
import com.krushit.model.RideRequest;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RideDAOImpl implements IRideDAO {
    private static final String GET_PENDING_RIDES_FOR_AVAILABLE_DRIVERS =
            "SELECT rr.ride_request_id, rr.pick_up_location_id, rr.drop_off_location_id, rr.ride_request_status, rr.vehicle_service_id, rr.created_at, rr.ride_date, rr.pick_up_time, rr.user_id " +
                    "FROM ride_requests AS rr " +
                    "JOIN vehicle_service AS vs ON rr.vehicle_service_id = vs.service_id " +
                    "JOIN brand_models AS bm ON vs.service_id = bm.service_id " +
                    "JOIN vehicles AS v ON bm.brand_model_id = v.brand_model_id " +
                    "JOIN drivers AS d ON v.driver_id = d.driver_id " +
                    "WHERE d.is_available = TRUE " +
                    "AND rr.ride_request_status = 'PENDING' " +
                    "AND d.driver_id = ?";
    private static final String GET_RIDE_BY_ID = "SELECT ride_id, customer_id, driver_id, ride_status, ride_date, pick_up_time, display_id, total_cost, commission_percentage " +
            "FROM rides WHERE ride_id = ?";
    private static final String GET_DRIVER_RIDES_BY_DATE_RANGE =
            "SELECT display_id, ride_status, pick_location_id, drop_off_location_id, " +
                    "ride_date, pick_up_time, total_km, total_cost, " +
                    "payment_mode, payment_status, driver_earning, cancellation_driver_earning " +
                    "FROM rides " +
                    "WHERE driver_id = ? AND ride_date BETWEEN ? AND ?";

    private static final String GET_TOTAL_RIDES =
            "SELECT COUNT(*) AS total_rides " +
                    "FROM rides " +
                    "WHERE driver_id = ? AND ride_date BETWEEN ? AND ?";

    private static final String GET_TOTAL_EARNINGS =
            "SELECT SUM(driver_earning + cancellation_driver_earning - driver_penalty) AS total_earning " +
                    "FROM rides " +
                    "WHERE driver_id = ? AND ride_date BETWEEN ? AND ?";

    private static String GET_RIDE_REQUEST_BY_ID = "SELECT * FROM ride_requests WHERE ride_request_id = ?";
    private static String INSERT_RIDE = "INSERT INTO rides (ride_status, pick_location_id, drop_off_location_id, customer_id, driver_id, " +
            "ride_date, pick_up_time, display_id, total_km, total_cost, payment_mode, payment_status, " +
            "commission_percentage, driver_earning, system_earning) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static String UPDATE_RIDE_STATUS = "UPDATE rides SET ride_status = ?, cancellation_charge = ?, cancellation_driver_earning = ?, cancellation_system_earning = ?, driver_penalty = ?, driver_earning =?, system_earning = ? WHERE ride_id = ?";
    private static String GET_RIDE_BY_USER_ID = "SELECT * FROM rides WHERE customer_id = ? OR driver_id = ? ORDER BY ride_date DESC";
    private static String GET_RIDE_STATUS = "SELECT ride_status FROM rides WHERE ride_id = ?";

    public List<RideRequest> getAllMatchingRideRequests(int driverId) throws DBException {
        List<RideRequest> rideRequests = new ArrayList<>();
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PENDING_RIDES_FOR_AVAILABLE_DRIVERS)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RideRequest rideRequest = new RideRequest.RideRequestBuilder()
                        .setRideRequestId(rs.getInt("ride_request_id"))
                        .setPickUpLocationId(rs.getInt("pick_up_location_id"))
                        .setDropOffLocationId(rs.getInt("drop_off_location_id"))
                        .setUserId(rs.getInt("user_id"))
                        .setRideDate(rs.getDate("ride_date").toLocalDate())
                        .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                        .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build();
                rideRequests.add(rideRequest);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_GETTING_ALL_RIDE_REQUEST_FOR_DRIVER, e);
        }
        return rideRequests;
    }

    @Override
    public Optional<RideRequest> getRideRequestById(int rideRequestId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_RIDE_REQUEST_BY_ID)) {
            pstmt.setInt(1, rideRequestId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Optional.of(new RideRequest.RideRequestBuilder()
                        .setRideRequestId(rs.getInt("ride_request_id"))
                        .setRideRequestStatus(RideRequestStatus.fromString(rs.getString("ride_request_status")))
                        .setPickUpLocationId(rs.getInt("pick_up_location_id"))
                        .setDropOffLocationId(rs.getInt("drop_off_location_id"))
                        .setVehicleServiceId(rs.getInt("vehicle_service_id"))
                        .setUserId(rs.getInt("user_id"))
                        .setRideDate(rs.getDate("ride_date").toLocalDate())
                        .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                        .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_GETTING_RIDE_REQUEST_BY_ID, e);
        }
        return Optional.empty();
    }

    @Override
    public void createRide(Ride ride) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_RIDE)) {
            pstmt.setString(1, ride.getRideStatus().getStatus());
            pstmt.setInt(2, ride.getPickLocationId());
            pstmt.setInt(3, ride.getDropOffLocationId());
            pstmt.setInt(4, ride.getCustomerId());
            pstmt.setInt(5, ride.getDriverId());
            pstmt.setDate(6, Date.valueOf(ride.getRideDate()));
            pstmt.setTime(7, Time.valueOf(ride.getPickUpTime()));
            pstmt.setString(8, ride.getDisplayId());
            pstmt.setDouble(9, ride.getTotalKm());
            pstmt.setDouble(10, ride.getTotalCost());
            pstmt.setString(11, ride.getPaymentMode().getMode());
            pstmt.setString(12, ride.getPaymentStatus().getStatus());
            pstmt.setDouble(13, ride.getCommissionPercentage());
            pstmt.setDouble(14, ride.getDriverEarning());
            pstmt.setDouble(15, ride.getSystemEarning());
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_CREATING_RIDE, e);
        }
    }

    public Optional<Ride> getRideById(int rideId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(GET_RIDE_BY_ID);
            stmt.setInt(1, rideId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Optional.of(new Ride.RideBuilder()
                        .setRideId(rs.getInt("ride_id"))
                        .setCustomerId(rs.getInt("customer_id"))
                        .setDriverId(rs.getInt("driver_id"))
                        .setRideStatus(RideStatus.fromString(rs.getString("ride_status")))
                        .setRideDate(rs.getDate("ride_date").toLocalDate())
                        .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                        .setDisplayId(rs.getString("display_id"))
                        .setTotalCost(rs.getDouble("total_cost"))
                        .setCommissionPercentage(rs.getDouble("commission_percentage"))
                        .build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_CREATING_RIDE, e);
        }
        return Optional.empty();
    }

    public void updateRideCancellation(RideCancellationDetails cancellationDetails) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_RIDE_STATUS);) {
            stmt.setString(1, cancellationDetails.getRideStatus());
            stmt.setDouble(2, cancellationDetails.getCancellationCharge());
            stmt.setDouble(3, cancellationDetails.getDriverEarning());
            stmt.setDouble(4, cancellationDetails.getSystemEarning());
            stmt.setDouble(5, cancellationDetails.getDriverPenalty());
            stmt.setDouble(6, 0.0);
            if (cancellationDetails.getDriverPenalty() != 0.0) {
                stmt.setDouble(7, 120);
            } else {
                stmt.setDouble(7, 0.0);
            }
            stmt.setInt(8, cancellationDetails.getRideId());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_RIDE_CANCELLATION, e);
        }
    }

    @Override
    public List<Ride> getAllRideByUserId(int userId) throws DBException {
        List<Ride> rideList = new ArrayList<>();
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_RIDE_BY_USER_ID)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ride ride = new Ride.RideBuilder()
                        .setRideId(rs.getInt("ride_id"))
                        .setRideStatus(RideStatus.fromString(rs.getString("ride_status")))
                        .setPickLocationId(rs.getInt("pick_location_id"))
                        .setDropOffLocationId(rs.getInt("drop_off_location_id"))
                        .setCustomerId(rs.getInt("customer_id"))
                        .setDriverId(rs.getInt("driver_id"))
                        .setRideDate(rs.getDate("ride_date").toLocalDate())
                        .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                        .setDisplayId(rs.getString("display_id"))
                        .setTotalKm(rs.getDouble("total_km"))
                        .setTotalCost(rs.getDouble("total_cost"))
                        .setPaymentMode(PaymentMode.fromString(rs.getString("payment_mode")))
                        .setPaymentStatus(PaymentStatus.fromString(rs.getString("payment_status")))
                        .setCancellationCharge(rs.getDouble("cancellation_charge"))
                        .setDriverEarning(rs.getDouble("driver_earning"))
                        .setCancellationDriverEarning(rs.getDouble("cancellation_driver_earning"))
                        .setDriverPenalty(rs.getDouble("driver_penalty"))
                        .build();
                rideList.add(ride);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_GETTING_ALL_RIDES, e);
        }
        return rideList;
    }

    @Override
    public String getRideStatus(int rideId) throws DBException {
        String rideStatus = null;
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_RIDE_STATUS)) {
            preparedStatement.setInt(1, rideId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rideStatus = resultSet.getString("ride_status");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_FETCHING_RIDE_STATUS, e);
        }
        return rideStatus;
    }

    public List<Ride> getRideDetailsByDateRange(int driverId, LocalDate startDate, LocalDate endDate) throws DBException {
        List<Ride> rideDTOList = new ArrayList<>();
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_DRIVER_RIDES_BY_DATE_RANGE)) {
            ps.setInt(1, driverId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ride ride = new Ride.RideBuilder()
                            .setDisplayId(rs.getString("display_id"))
                            .setRideStatus(RideStatus.fromString(rs.getString("ride_status")))
                            .setPickLocationId(rs.getInt("pick_location_id"))
                            .setDropOffLocationId(rs.getInt("drop_off_location_id"))
                            .setRideDate(rs.getDate("ride_date").toLocalDate())
                            .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                            .setTotalKm(rs.getDouble("total_km"))
                            .setTotalCost(rs.getDouble("total_cost"))
                            .setPaymentMode(PaymentMode.fromString(rs.getString("payment_mode")))
                            .setPaymentStatus(PaymentStatus.fromString(rs.getString("payment_status")))
                            .setDriverEarning(rs.getDouble("driver_earning"))
                            .setCancellationDriverEarning(rs.getDouble("cancellation_driver_earning"))
                            .build();
                    rideDTOList.add(ride);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_FETCHING_RIDE_DETAILS_BY_RANGE, e);
        }
        return rideDTOList;
    }

    public int getTotalRides(int driverId, LocalDate startDate, LocalDate endDate) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_TOTAL_RIDES)) {
            ps.setInt(1, driverId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_rides");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_FETCHING_TOTAL_RIDES, e);
        }
        return 0;
    }

    public double getTotalEarnings(int driverId, LocalDate startDate, LocalDate endDate) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_TOTAL_EARNINGS)) {
            ps.setInt(1, driverId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_earning");
                }
            }
        }  catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_CALCULATING_DRIVER_TOTAL_EARNING, e);
        }
        return 0.0;
    }
}