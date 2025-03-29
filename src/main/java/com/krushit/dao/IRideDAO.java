package com.krushit.dao;

import com.krushit.common.config.DBConfig;
import com.krushit.common.enums.PaymentMode;
import com.krushit.common.enums.PaymentStatus;
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

public interface IRideDAO {
    List<RideRequest> getAllMatchingRideRequests(int driverId) throws DBException;
    Optional<RideRequest> getRideRequestById(int rideRequestId) throws DBException;
    void createRide(Ride ride) throws DBException;
    Optional<Ride> getRideById(int rideId) throws DBException;
    void updateRideCancellation(RideCancellationDetails cancellationDetails) throws DBException;
    List<Ride> getAllRideByUserId(int userId) throws DBException;
    String getRideStatus(int rideId) throws DBException;
    List<Ride> getRideDetailsByDateRange(int driverId, LocalDate startDate, LocalDate endDate) throws DBException;
    int getTotalRides(int driverId, LocalDate startDate, LocalDate endDate) throws DBException;
    double getTotalEarnings(int driverId, LocalDate startDate, LocalDate endDate) throws DBException;
}
