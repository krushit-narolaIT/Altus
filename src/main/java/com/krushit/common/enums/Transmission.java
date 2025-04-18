package com.krushit.common.enums;

import com.krushit.common.Message;

public enum Transmission {
    MANUAL("Manual"),
    AUTOMATIC("Automatic");

    private final String transmissionType;

    Transmission(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public static Transmission getType(String transmissionType) {
        for (Transmission type : Transmission.values()) {
            if (type.getTransmissionType().equalsIgnoreCase(transmissionType)) {
                return type;
            }
        }
        throw new IllegalArgumentException(Message.Vehicle.INVALID_TRANSMISSION_TYPE + transmissionType);
    }

    public static boolean isValidTransmission(String transmissionType) {
        for (Transmission type : Transmission.values()) {
            if (type.getTransmissionType().equalsIgnoreCase(transmissionType)) {
                return true;
            }
        }
        return false;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    @Override
    public String toString() {
        return transmissionType;
    }
}
