package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ValidationException;
import com.krushit.dto.DistanceRequestDTO;

public class RideValidator {
    public static void validateLocation(DistanceRequestDTO distanceRequestDTO) throws ValidationException {
        if (distanceRequestDTO.getFrom() == null || distanceRequestDTO.getFrom().trim().isEmpty()
                || distanceRequestDTO.getTo() == null || distanceRequestDTO.getTo().trim().isEmpty()) {
            throw new ValidationException(Message.Ride.LOCATION_IS_REQUIRE);
        }
    }
}
