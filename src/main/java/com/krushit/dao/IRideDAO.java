package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.model.Ride;
import com.krushit.model.RideRequest;

import java.util.List;

public interface IRideDAO {
    List<RideRequest> getAllMatchingRideRequests(int driverId) throws DBException;
    RideRequest getRideRequestById(int rideRequestId) throws DBException;
    void createRide(Ride ride) throws DBException;
}
