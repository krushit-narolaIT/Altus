package com.krushit.common.exception;

public enum Transmission {
    MANUAL("Manual"),
    AUTOMATIC("Automatic");

    private final String transmissionType;

    Transmission(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public static Transmission getType(String transmissionType) {
        for (Transmission type : Transmission.values()) {
            if (type.getTransmissionType().equalsIgnoreCase(transmissionType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Transmission Type: " + transmissionType);
    }

    public static boolean isValidTransmission(String transmissionType) {
        for (Transmission type : Transmission.values()) {
            if (type.getTransmissionType().equalsIgnoreCase(transmissionType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return transmissionType;
    }
}
