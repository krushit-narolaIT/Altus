package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.enums.RideRequestStatus;
import com.krushit.common.exception.DBException;
import com.krushit.model.Ride;
import com.krushit.model.RideRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    private static String GET_RIDE_REQUEST_BY_ID = "SELECT * FROM ride_requests WHERE ride_request_id = ?";
    private static String INSERT_RIDE = "INSERT INTO rides (ride_status, pick_location_id, drop_off_location_id, customer_id, driver_id, " +
            "ride_date, pick_up_time, display_id, total_km, total_cost, payment_mode, payment_status, " +
            "commission_percentage, driver_earning, system_earning) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
    public RideRequest getRideRequestById(int rideRequestId) throws DBException {
        RideRequest rideRequest = null;
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_RIDE_REQUEST_BY_ID)) {
            pstmt.setInt(1, rideRequestId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                rideRequest = new RideRequest.RideRequestBuilder()
                        .setRideRequestId(rs.getInt("ride_request_id"))
                        .setRideRequestStatus(RideRequestStatus.fromString(rs.getString("ride_request_status")))
                        .setPickUpLocationId(rs.getInt("pick_up_location_id"))
                        .setDropOffLocationId(rs.getInt("drop_off_location_id"))
                        .setVehicleServiceId(rs.getInt("vehicle_service_id"))
                        .setUserId(rs.getInt("user_id"))
                        .setRideDate(rs.getDate("ride_date").toLocalDate())
                        .setPickUpTime(rs.getTime("pick_up_time").toLocalTime())
                        .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Ride.ERROR_WHILE_GETTING_RIDE_REQUEST_BY_ID, e);
        }
        return rideRequest;
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
}
