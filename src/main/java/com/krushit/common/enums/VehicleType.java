package com.krushit.common.enums;

import com.krushit.common.Message;

public enum VehicleType {
    TWO_WHEELER("2W"),
    THREE_WHEELER("3W"),
    FOUR_WHEELER("4W");

    private final String typeName;

    VehicleType(String typeName) {
        this.typeName = typeName;
    }

    public static VehicleType getType(String typeName) {
        for (VehicleType type : VehicleType.values()) {
            if (type.getTypeName().equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException(Message.Vehicle.INVALID_VEHICLE_TYPE + typeName);
    }

    public static boolean isValidVehicleType(String vehicleType) {
        for (VehicleType type : VehicleType.values()) {
            if (type.getTypeName().equalsIgnoreCase(vehicleType)) {
                return true;
            }
        }
        return false;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
