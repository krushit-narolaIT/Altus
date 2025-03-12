package com.krushit.exception;

import java.sql.SQLException;

public class DBException extends ApplicationException {
    public DBException(String message, SQLException e) {
        super(message, e);
    }

    public DBException(String message) {
        super(message);
    }
}
