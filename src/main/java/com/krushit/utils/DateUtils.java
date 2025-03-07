package com.krushit.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateUtils {
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toLocalDateTime();
        }
        return null;
    }
}
