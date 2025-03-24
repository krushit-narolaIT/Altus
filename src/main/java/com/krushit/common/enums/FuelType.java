package com.krushit.common.enums;

public enum FuelType {
    PETROL("Petrol"),
    DIESEL("Diesel"),
    CNG("CNG"),
    ELECTRIC("Electric"),
    HYBRID("Hybrid");

    private final String fuelName;

    FuelType(String fuelName) {
        this.fuelName = fuelName;
    }

    public String getFuelName() {
        return fuelName;
    }

    public static FuelType getType(String fuelName) {
        for (FuelType type : FuelType.values()) {
            if (type.getFuelName().equalsIgnoreCase(fuelName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Fuel Type: " + fuelName);
    }

    public static boolean isValidFuelType(String fuelName) {
        for (FuelType type : FuelType.values()) {
            if (type.getFuelName().equalsIgnoreCase(fuelName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return fuelName;
    }
}
