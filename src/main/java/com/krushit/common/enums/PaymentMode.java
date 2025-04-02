package com.krushit.common.enums;

import com.krushit.common.Message;

public enum PaymentMode {
    CASH("Cash"),
    CARD("Card"),
    UPI("UPI");

    private final String mode;

    PaymentMode(String mode) {
        this.mode = mode;
    }

    public static PaymentMode getType(String mode) {
        for (PaymentMode p : PaymentMode.values()) {
            if (p.mode.equalsIgnoreCase(mode)) {
                return p;
            }
        }
        throw new IllegalArgumentException(Message.INVALID_PAYMENT_MODE + mode);
    }

    public String getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return mode;
    }
}
