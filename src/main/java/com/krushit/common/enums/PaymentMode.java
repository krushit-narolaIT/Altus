package com.krushit.common.enums;

public enum PaymentMode {
    CASH("Cash"),
    CARD("Card"),
    UPI("UPI");

    private final String mode;

    PaymentMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return mode;
    }

    public static PaymentMode fromString(String mode) {
        for (PaymentMode p : PaymentMode.values()) {
            if (p.mode.equalsIgnoreCase(mode)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMode: " + mode);
    }
}
