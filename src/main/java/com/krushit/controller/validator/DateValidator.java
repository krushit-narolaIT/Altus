package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidator {
    public static LocalDate getLocalDate(String dateStr) throws ApplicationException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_DATE);
        }
        try {
            LocalDate date = LocalDate.parse(dateStr);
            if (date.isAfter(LocalDate.now())) {
                throw new ApplicationException(Message.Ride.DATE_CANNOT_BE_IN_FUTURE);
            }
            return date;
        } catch (DateTimeParseException e) {
            throw new ApplicationException(Message.Ride.INVALID_DATE_FORMAT);
        }
    }
}
