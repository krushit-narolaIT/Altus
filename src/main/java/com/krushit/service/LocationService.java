package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.dao.ILocationDAO;
import com.krushit.dao.LocationDAOImpl;
import com.krushit.model.Location;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class LocationService {
    private final ILocationDAO locationDAO = new LocationDAOImpl();
    private static final String GEO_BASE_URL = "https://graphhopper.com/api/1/geocode";
    private static final String ROUTE_BASE_URL = "https://graphhopper.com/api/1/route";
    private static final String API_KEY = "6d96b6fb-13b1-43b6-99ab-2e1d55edd76b";

    public List<Location> getAllLocations() throws ApplicationException {
        if(locationDAO.getAllLocations().isEmpty()){
            throw new ApplicationException(Message.Location.LOCATIONS_NOT_FOUND);
        }
        return locationDAO.getAllLocations();
    }

    public void deleteLocation(int locationId) throws ApplicationException {
        if(locationDAO.getLocationName(locationId).isEmpty()){
            throw new ApplicationException(Message.Location.LOCATION_NOT_FOUND);
        }
    }

    public static String getCoordinates(String place) throws Exception {
        String query = URLEncoder.encode(place, Message.UTF_8);
        String geoUrl = GEO_BASE_URL + "?q=" + query + "&limit=1&key=" + API_KEY;
        URL url = new URL(geoUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String responseString = response.toString();
        int latIndex = responseString.indexOf("\"lat\":");
        int lngIndex = responseString.indexOf("\"lng\":");
        if (latIndex == -1 || lngIndex == -1) {
            throw new Exception(Message.Ride.COORDINATES_NOT_FOUND_FOR + place);
        }
        String lat = responseString.substring(latIndex + 6, responseString.indexOf(",", latIndex)).trim();
        String lng = responseString.substring(lngIndex + 6, responseString.indexOf("}", lngIndex)).trim();
        return lat + "," + lng;
    }

    public double calculateDistance(int fromId, int toId) throws Exception {
        String fromLocation = locationDAO.getLocationName(fromId);
        String toLocation = locationDAO.getLocationName(toId);
        if (fromLocation == null || toLocation == null) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_LOCATION);
        }
        String fromCoordinates = getCoordinates(fromLocation + ", Surat, Gujarat");
        String toCoordinates = getCoordinates(toLocation + ", Surat, Gujarat");
        String apiUrl = ROUTE_BASE_URL + "?point=" + fromCoordinates + "&point=" + toCoordinates +
                "&vehicle=car&locale=en&key=" + API_KEY + "&points_encoded=false";
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String responseString = response.toString();
        String distanceKey = "\"distance\":";
        int distanceInd = responseString.indexOf(distanceKey);
        if (distanceInd == -1) {
            throw new Exception(Message.Ride.INVALID_GRAPH_HOPPER_API_RESPONSE);
        }
        distanceInd += distanceKey.length();
        int endIndex = responseString.indexOf(",", distanceInd);
        if (endIndex == -1) {
            endIndex = responseString.indexOf("}", distanceInd);
        }
        String distanceValue = responseString.substring(distanceInd, endIndex).trim();
        return Math.round(Double.parseDouble(distanceValue) / 1000 * 100.0) / 100.0;
        //return Double.parseDouble(distanceValue) / 1000;
    }

    public void addLocation(String location) throws ApplicationException {
        locationDAO.addLocation(location);
    }

    public String getLocationNameById(int pickLocationId) throws ApplicationException{
        return locationDAO.getLocationName(pickLocationId);
    }

    public double getCommissionByDistance(double distance) throws ApplicationException{
        return locationDAO.getCommissionByDistance(distance);
    }
}
