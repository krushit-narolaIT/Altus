package com.krushit.exception;

import java.sql.SQLException;

public class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, SQLException e) {
        super(message);
    }
}