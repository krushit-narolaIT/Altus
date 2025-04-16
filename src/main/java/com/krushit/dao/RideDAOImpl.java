package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.config.JPAConfig;
import com.krushit.common.enums.PaymentMode;
import com.krushit.common.enums.PaymentStatus;
import com.krushit.common.enums.RideRequestStatus;
import com.krushit.common.enums.RideStatus;
import com.krushit.common.exception.DBException;
import com.krushit.dto.RideCancellationDetailsDTO;
import com.krushit.entity.Ride;
import com.krushit.entity.RideRequest;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RideDAOImpl implements IRideDAO {
    private static final String GET_PENDING_RIDES_FOR_AVAILABLE_DRIVERS =
            "SELECT rr FROM RideRequest rr " +
                    "JOIN rr.vehicleService vs " +
                    "JOIN Vehicle v ON v.brandModel.brandModelId = vs.serviceId " +
                    "JOIN Driver d ON v.driver.driverId = d.driverId " +
                    "WHERE d.isAvailable = true " +
                    "AND rr.rideRequestStatus = com.krushit.common.enums.RideRequestStatus.PENDING " +
                    "AND d.driverId = :driverId";
    private static final String GET_RIDE_BY_ID = "SELECT ride_id, customer_id, driver_id, ride_status, ride_date, pick_up_time, display_id, total_cost, commission_percentage " +
            "FROM rides WHERE ride_id = :rideId";
    private static final String GET_DRIVER_RIDES_BY_DATE_RANGE =
            "SELECT ride_id, display_id, ride_status, pick_location_id, drop_off_location_id, customer_id, " +
                    "ride_date, pick_up_time, total_km, total_cost, " +
                    "payment_mode, payment_status, driver_earning, cancellation_driver_earning, driver_penalty " +
                    "FROM rides " +
                    "WHERE driver_id = ? AND ride_date BETWEEN ? AND ?";
    private static final String GET_RIDES_BY_DATE_RANGE =
            "SELECT * FROM rides WHERE ride_date BETWEEN ? AND ?";
    private static final String GET_TOTAL_DRIVER_RIDES =
            "SELECT COUNT(*) AS total_rides " +
                    "FROM rides " +
                    "WHERE driver_id = ? AND ride_date BETWEEN ? AND ?";
    private static final String GET_TOTAL_RIDES =
            "SELECT COUNT(*) AS total_rides " +
                    "FROM rides " +
                    "WHERE ride_date BETWEEN ? AND ?";
    private static final String GET_TOTAL_DRIVER_EARNING =
            "SELECT SUM(driver_earning + cancellation_driver_earning - driver_penalty) AS total_earning " +
                    "FROM rides " +
                    "WHERE driver_id = ? AND ride_date BETWEEN ? AND ?";
    private static final String GET_TOTAL_EARNINGS =
            "SELECT SUM(system_earning + cancellation_system_earning) AS total_earning " +
                    "FROM rides " +
                    "WHERE ride_date BETWEEN ? AND ?";
    private static final String GET_RIDE_REQUEST_BY_ID = "SELECT * FROM ride_requests WHERE ride_request_id = ?";
    private static final String INSERT_RIDE = "INSERT INTO rides (ride_status, pick_location_id, drop_off_location_id, customer_id, driver_id, " +
            "ride_date, pick_up_time, display_id, total_km, total_cost, payment_mode, payment_status, " +
            "commission_percentage, driver_earning, system_earning) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_RIDE_STATUS = "UPDATE rides SET ride_status = ?, cancellation_charge = ?, cancellation_driver_earning = ?, cancellation_system_earning = ?, driver_penalty = ?, driver_earning =?, system_earning = ? WHERE ride_id = ?";
    private static final String GET_RIDE_BY_USER_ID = "SELECT * FROM rides WHERE customer_id = ? OR driver_id = ? ORDER BY ride_date DESC";
    private static final String GET_RIDE_STATUS = "SELECT ride_status FROM rides WHERE ride_id = ?";
    private static final String UPDATE_RIDE_REQUEST_STATUS = "UPDATE Ride_requests SET ride_request_status = ? WHERE ride_request_id = ?";
    private static final String CHECK_IS_ANOTHER_RIDE_IS_ALREADY_SCHEDULED = "SELECT ride_id, ride_status, pick_location_id, drop_off_location_id, pick_up_time, ride_date, display_id " +
            "FROM rides " +
            "WHERE driver_id = ? AND ride_date = ? " +
            "AND pick_up_time >= ? AND pick_up_time <= ? " +
            "AND ride_status IN ('Scheduled', 'Ongoing') ";

    @Override
    public List<RideRequest> getAllMatchingRideRequests(int driverId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(GET_PENDING_RIDES_FOR_AVAILABLE_DRIVERS, RideRequest.class)
                    .setParameter("driverId", driverId)
                    .getResultList();
        } catch (Exception e) {
            throw new DBException(Message.Ride.ERROR_WHILE_GETTING_ALL_RIDE_REQUEST_FOR_DRIVER, e);
        }
    }

    @Override
    public Optional<RideRequest> getRideRequest(int rideRequestId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            RideRequest rideRequest = em.find(RideRequest.class, rideRequestId);
            return Optional.ofNullable(rideRequest);
        } catch (Exception e) {
            throw new DBException(Message.Ride.ERROR_WHILE_GETTING_RIDE_REQUEST_BY_ID, e);
        }
    }

    @Override
    public Optional<Ride> getConflictingRide(int driverId, LocalDate rideDate, LocalTime pickUpTime) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_IS_ANOTHER_RIDE_IS_ALREADY_SCHEDULED)) {
            LocalTime startTime = pickUpTime.minusMinutes(15);
            LocalTime endTime = pickUpTime.plusMinutes(30);
            stmt.setInt(1, driverId);
            stmt.setDate(2, Date.valueOf(rideDate));
            stmt.setTime(3, Time.valueOf(startTime));
            stmt.setTime(4, Time.valueOf(endTime));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ride ride = new Ride.RideBuilder()
                            .setRideId(rs.getInt("ride_id"))
                            .setRideStatus(RideStatus.valueOf(rs.getString("ride_status").toUpperCase()))
                            //.setPickLocationId(rs.getInt("pick_location_id"))
                            //.setDropOffLocationId(rs.getInt("drop_off_location_id"))
                            .setRideDate(rs.getDate("ride_date").toLocalDate())
                            .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                            .setDisplayId(rs.getString("display_id"))
                            .build();
                    return Optional.of(ride);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_GETTING_RIDE_REQUEST_BY_ID, e);
        }
        return Optional.empty();
    }

    @Override
    public void createRide(int rideRequestId, Ride ride) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_RIDE)) {
            try {
                conn.setAutoCommit(false);
                pstmt.setString(1, ride.getRideStatus().getStatus());
                pstmt.setInt(2, ride.getPickLocation().getId());
                pstmt.setInt(3, ride.getDropOffLocation().getId());
                pstmt.setInt(4, ride.getCustomer().getUserId());
                pstmt.setInt(5, ride.getDriver().getUserId());
                pstmt.setDate(6, Date.valueOf(ride.getRideDate()));
                pstmt.setTime(7, Time.valueOf(ride.getPickUpTime()));
                pstmt.setString(8, ride.getDisplayId());
                pstmt.setDouble(9, ride.getTotalKm());
                pstmt.setDouble(10, ride.getTotalCost().doubleValue());
                pstmt.setString(11, ride.getPaymentMode().getMode());
                pstmt.setString(12, ride.getPaymentStatus().getStatus());
                pstmt.setDouble(13, ride.getCommissionPercentage().doubleValue());
                pstmt.setDouble(14, ride.getDriverEarning().doubleValue());
                pstmt.setDouble(15, ride.getSystemEarning().doubleValue());
                pstmt.executeUpdate();
                updateRideRequestStatus(rideRequestId ,RideRequestStatus.ACCEPTED, conn);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new DBException(Message.Ride.ERROR_WHILE_CREATING_RIDE, e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_CREATING_RIDE, e);
        }
    }

    @Override
    public Optional<Ride> getRide(int rideId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_RIDE_BY_ID)) {
            stmt.setInt(1, rideId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Ride.RideBuilder()
                        .setRideId(rs.getInt("ride_id"))
                        //.setCustomerId(rs.getInt("customer_id"))
                        //.setDriverId(rs.getInt("driver_id"))
                        .setRideStatus(RideStatus.valueOf(rs.getString("ride_status")))
                        .setRideDate(rs.getDate("ride_date").toLocalDate())
                        .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                        .setDisplayId(rs.getString("display_id"))
                        .setTotalCost(new BigDecimal(rs.getDouble("total_cost")))
                        .setCommissionPercentage(new BigDecimal(rs.getDouble("commission_percentage")))
                        .build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_CREATING_RIDE, e);
        }
        return Optional.empty();
    }

    @Override
    public void updateRideRequestStatus(int rideRequestId, RideRequestStatus status, Connection connection) throws DBException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_RIDE_REQUEST_STATUS)) {
            stmt.setString(1, status.getStatus());
            stmt.setInt(2, rideRequestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_UPDATING_RIDE_REQUEST_STATUS, e);
        }
    }

    @Override
    public void updateRideCancellation(RideCancellationDetailsDTO cancellationDetails) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_RIDE_STATUS);) {
            stmt.setString(1, RideStatus.getType(cancellationDetails.getRideStatus()));
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
    public List<Ride> getAllRidesByUserId(int userId) throws DBException {
        List<Ride> rideList = new ArrayList<>();
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_RIDE_BY_USER_ID)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ride ride = new Ride.RideBuilder()
                        .setRideId(rs.getInt("ride_id"))
                        .setRideStatus(RideStatus.getType(rs.getString("ride_status")))
                        //.setPickLocationId(rs.getInt("pick_location_id"))
                        //.setDropOffLocationId(rs.getInt("drop_off_location_id"))
                        //.setCustomerId(rs.getInt("customer_id"))
                        //.setDriverId(rs.getInt("driver_id"))
                        .setRideDate(rs.getDate("ride_date").toLocalDate())
                        .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                        .setDisplayId(rs.getString("display_id"))
                        .setTotalKm(rs.getDouble("total_km"))
                        //.setTotalCost(rs.getDouble("total_cost")) TODO
                        .setPaymentMode(PaymentMode.getType(rs.getString("payment_mode")))
                        .setPaymentStatus(PaymentStatus.getType(rs.getString("payment_status")))
                        //.setCancellationCharge(rs.getDouble("cancellation_charge"))
                        //.setDriverEarning(rs.getDouble("driver_earning"))
                        //.setCancellationDriverEarning(rs.getDouble("cancellation_driver_earning"))
                        //.setDriverPenalty(rs.getDouble("driver_penalty"))
                        .build();
                rideList.add(ride);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_GETTING_ALL_RIDES, e);
        }
        return rideList;
    }

    @Override
    public RideStatus getRideStatus(int rideId) throws DBException {
        RideStatus rideStatus = null;
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_RIDE_STATUS)) {
            preparedStatement.setInt(1, rideId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rideStatus = RideStatus.getType(resultSet.getString("ride_status"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_FETCHING_RIDE_STATUS, e);
        }
        return rideStatus;
    }

    @Override
    public List<Ride> getRidesByDateRange(int driverId, LocalDate startDate, LocalDate endDate) throws DBException {
        List<Ride> rideList = new ArrayList<>();
        String GET_RIDES = GET_DRIVER_RIDES_BY_DATE_RANGE;
        if (driverId == 0) {
            GET_RIDES = GET_RIDES_BY_DATE_RANGE;
        }
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_RIDES)) {
            setQueryParameters(ps, driverId, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ride.RideBuilder builder = new Ride.RideBuilder()
                            .setRideId(rs.getInt("ride_id"))
                            .setDisplayId(rs.getString("display_id"))
                            .setRideStatus(RideStatus.valueOf(rs.getString("ride_status")))
                            //.setPickLocationId(rs.getInt("pick_location_id"))
                            //.setDropOffLocationId(rs.getInt("drop_off_location_id"))
                            //.setCustomerId(rs.getInt("customer_id"))
                            .setRideDate(rs.getDate("ride_date").toLocalDate())
                            .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                            .setTotalKm(rs.getDouble("total_km"))
                            //.setTotalCost(rs.getDouble("total_cost"))
                            .setPaymentMode(PaymentMode.getType(rs.getString("payment_mode")))
                            .setPaymentStatus(PaymentStatus.getType(rs.getString("payment_status")));
                            //.setDriverEarning(rs.getDouble("driver_earning"))
                            //.setCancellationDriverEarning(rs.getDouble("cancellation_driver_earning"))
                            //.setDriverPenalty(rs.getDouble("driver_penalty"));
                    if (driverId == 0) {
                        builder.setSystemEarning(new BigDecimal(rs.getDouble("system_earning")));
                                //.setCancellationSystemEarning(rs.getDouble("cancellation_system_earning"));
                    }
                    rideList.add(builder.build());
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_FETCHING_RIDE_DETAILS_BY_RANGE, e);
        }
        return rideList;
    }

    @Override
    public int getTotalRides(int driverId, LocalDate startDate, LocalDate endDate) throws DBException {
        String GET_RIDES = GET_TOTAL_DRIVER_RIDES;
        if (driverId == 0) {
            GET_RIDES = GET_TOTAL_RIDES;
        }
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_RIDES)) {
            setQueryParameters(ps, driverId, startDate, endDate);
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

    @Override
    public double getTotalEarnings(int driverId, LocalDate startDate, LocalDate endDate) throws DBException {
        String GET_RIDES = GET_TOTAL_DRIVER_EARNING;
        if (driverId == 0) {
            GET_RIDES = GET_TOTAL_EARNINGS;
        }
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_RIDES)) {
            setQueryParameters(ps, driverId, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_earning");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_CALCULATING_DRIVER_TOTAL_EARNING, e);
        }
        return 0.0;
    }

    private void setQueryParameters(PreparedStatement ps, int driverId, LocalDate startDate, LocalDate endDate) throws SQLException {
        if (driverId == 0) {
            ps.setDate(1, Date.valueOf(startDate));
            ps.setDate(2, Date.valueOf(endDate));
        } else {
            ps.setInt(1, driverId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
        }
    }
}