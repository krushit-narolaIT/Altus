package com.krushit.common.enums;

import com.krushit.common.Message;

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

    public static FuelType getType(String fuelName) {
        for (FuelType type : FuelType.values()) {
            if (type.getFuelName().equalsIgnoreCase(fuelName)) {
                return type;
            }
        }
        throw new IllegalArgumentException(Message.INVALID_FUEL_TYPE + fuelName);
    }

    public static boolean isValidFuelType(String fuelName) {
        for (FuelType type : FuelType.values()) {
            if (type.getFuelName().equalsIgnoreCase(fuelName)) {
                return true;
            }
        }
        return false;
    }

    public String getFuelName() {
        return fuelName;
    }

    @Override
    public String toString() {
        return fuelName;
    }
}
