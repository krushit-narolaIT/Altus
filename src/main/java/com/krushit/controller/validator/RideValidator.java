package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ValidationException;
import com.krushit.dto.DistanceRequestDTO;

public class RideValidator {
    public static void validateLocation(DistanceRequestDTO distanceRequestDTO) throws ValidationException {
        if (String.valueOf(distanceRequestDTO.getFrom()).trim().isEmpty() ||
                String.valueOf(distanceRequestDTO.getTo()).trim().isEmpty()) {
            System.out.println("in validation");
            throw new ValidationException(Message.Ride.LOCATION_IS_REQUIRE);
        }
    }
}
