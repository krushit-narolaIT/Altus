package com.krushit.exception;

import java.sql.SQLException;

public class ValidationException extends ApplicationException {
    public ValidationException(String message, SQLException e) {
        super(message, e);
    }

    public ValidationException(String message) {
        super(message);
    }
}