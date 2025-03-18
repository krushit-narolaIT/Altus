package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.dao.DriverDAOImpl;
import com.krushit.dao.IDriverDAO;
import com.krushit.dao.IVehicleDAO;
import com.krushit.dao.VehicleDAOImpl;
import com.krushit.common.exception.ApplicationException;
import com.krushit.model.BrandModel;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class VehicleRideService {
    private static final String API_KEY = "6d96b6fb-13b1-43b6-99ab-2e1d55edd76b";
    private static final String BASE_URL = "https://graphhopper.com/api/1/route";
    private IVehicleDAO vehicleDAO = new VehicleDAOImpl();
    private IDriverDAO driverDAO = new DriverDAOImpl();

    public void addVehicleService(VehicleService vehicleService) throws ApplicationException {
        vehicleDAO.addVehicleService(vehicleService);
    }

    public void addBrandModel(BrandModel brandModel) throws ApplicationException {
        vehicleDAO.addBrandModel(brandModel);
    }

    public void addVehicle(Vehicle vehicle, int userId) throws ApplicationException {
        int driverId = driverDAO.getDriverIdFromUserId(userId);
        if(!driverDAO.isDriverDocumentUploaded(driverId)){
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_UPLOADED);
        }
        if(!driverDAO.isDriverDocumentVerified(driverId)){
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_VERIFIED);
        }
        if(vehicleDAO.isDriverVehicleExist(driverId)){
            throw new ApplicationException(Message.Vehicle.DRIVER_VEHICLE_ALREADY_EXIST);
        }
        if(vehicleDAO.isBrandModelExist(vehicle.getBrandModelId())){
            throw new ApplicationException(Message.Vehicle.DRIVER_VEHICLE_ALREADY_EXIST);
        }
        int minYear = vehicleDAO.getMinYearForBrandModel(vehicle.getBrandModelId());
        if (vehicle.getYear() < minYear) {
            throw new ApplicationException(Message.Vehicle.BRAND_MODEL_NOT_SUPPORTED);
        }
        vehicle.setDriverId(driverId);
        vehicleDAO.addVehicle(vehicle);
    }

    public Map<String, List<String>> getAllBrandModels() throws DBException {
        return vehicleDAO.getAllBrandModels();
    }

    public double calculateDistance(String from, String to) throws Exception {
        String queryFrom = from.replace(" ", "+");
        String queryTo = to.replace(" ", "+");
        URL url = new URL(BASE_URL + "?point=" + queryFrom + "&point=" + queryTo + "&vehicle=car&locale=en&key=" + API_KEY + "&points_encoded=false");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getJSONArray("paths").getJSONObject(0).getDouble("distance") / 1000;
    }
}
