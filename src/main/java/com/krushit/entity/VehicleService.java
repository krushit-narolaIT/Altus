package com.krushit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle_service")
public class VehicleService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private int serviceId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "base_fare")
    private double baseFare;

    @Column(name = "per_km_rate")
    private double perKmRate;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "max_passengers")
    private int maxPassengers;

    @Column(name = "commission_percentage")
    private double commissionPercentage;

    public VehicleService() {}

    public VehicleService(int serviceId, String serviceName, double baseFare, double perKmRate,
                          String vehicleType, int maxPassengers, double commissionPercentage) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
        this.vehicleType = vehicleType;
        this.maxPassengers = maxPassengers;
        this.commissionPercentage = commissionPercentage;
    }

    public VehicleService(int serviceId, String serviceName, double baseFare, double perKmRate,
                          String vehicleType, int maxPassengers) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
        this.vehicleType = vehicleType;
        this.maxPassengers = maxPassengers;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public double getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(double perKmRate) {
        this.perKmRate = perKmRate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public double getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(double commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    @Override
    public String toString() {
        return "VehicleService [serviceId=" + serviceId + ", serviceName=" + serviceName +
                ", baseFare=" + baseFare + ", perKmRate=" + perKmRate + ", vehicleType=" + vehicleType +
                ", maxPassengers=" + maxPassengers + ", commissionPercentage=" + commissionPercentage + "]";
    }
}
