package com.krushit.model;

public enum VehicleType {
    TWO_WHEELER("2W"),
    THREE_WHEELER("3W"),
    FOUR_WHEELER("4W");

    private final String typeName;

    VehicleType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static VehicleType getType(String typeName) {
        for (VehicleType type : VehicleType.values()) {
            if (type.getTypeName().equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Vehicle Type: " + typeName);
    }

    public static boolean isValidVehicleType(String vehicleType){
        for (VehicleType type : VehicleType.values()) {
            if (type.getTypeName().equalsIgnoreCase(vehicleType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
