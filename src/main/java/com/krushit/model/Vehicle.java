package com.krushit.model;

public class Vehicle {
    private int vehicleId;
    private int driverId;
    private int brandModelId;
    private String registrationNumber;
    private int year;
    private String fuelType;
    private String transmission;
    private double groundClearance;
    private double wheelBase;
    private String verificationStatus;
    private String verificationMessage;

    public Vehicle() {}

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getBrandModelId() {
        return brandModelId;
    }

    public void setBrandModelId(int brandModelId) {
        this.brandModelId = brandModelId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public double getGroundClearance() {
        return groundClearance;
    }

    public void setGroundClearance(double groundClearance) {
        this.groundClearance = groundClearance;
    }

    public double getWheelBase() {
        return wheelBase;
    }

    public void setWheelBase(double wheelBase) {
        this.wheelBase = wheelBase;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getVerificationMessage() {
        return verificationMessage;
    }

    public void setVerificationMessage(String verificationMessage) {
        this.verificationMessage = verificationMessage;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", driverId=" + driverId +
                ", brandModelId=" + brandModelId +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", year=" + year +
                ", fuelType='" + fuelType + '\'' +
                ", transmission='" + transmission + '\'' +
                ", groundClearance=" + groundClearance +
                ", wheelBase=" + wheelBase +
                ", verificationStatus='" + verificationStatus + '\'' +
                ", verificationMessage='" + verificationMessage + '\'' +
                '}';
    }
}
