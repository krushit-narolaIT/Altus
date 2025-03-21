package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ValidationException;
import com.krushit.dto.DistanceCalculatorDTO;

public class RideValidator {
    public static void validateLocation(DistanceCalculatorDTO distanceCalculatorDTO) throws ValidationException {
        if (distanceCalculatorDTO == null || String.valueOf(distanceCalculatorDTO.getFrom()).trim().isEmpty() ||
                String.valueOf(distanceCalculatorDTO.getTo()).trim().isEmpty()) {
            System.out.println("in validation");
            throw new ValidationException(Message.Ride.LOCATION_IS_REQUIRE);
        }
    }
}
