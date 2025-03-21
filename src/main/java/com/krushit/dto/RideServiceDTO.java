package com.krushit.dto;

public class RideServiceDTO {
    private String serviceName;
    private double totalPrice;
    private String fromLocation;
    private String toLocation;
    private int capacity;

    public RideServiceDTO(String serviceName, double totalPrice, String fromLocation, String toLocation, int capacity) {
        this.serviceName = serviceName;
        this.totalPrice = totalPrice;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.capacity = capacity;
    }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getFromLocation() { return fromLocation; }
    public void setFromLocation(String fromLocation) { this.fromLocation = fromLocation; }

    public String getToLocation() { return toLocation; }
    public void setToLocation(String toLocation) { this.toLocation = toLocation; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
