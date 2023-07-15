package org.trhelper.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConfirmOrderDTO {
    Long id;
    String acceptedByUsername;
    String startingDate;
    String startingTime;
    String durationToPickupLocation;
    String durationToDestination;

    public ConfirmOrderDTO(Long id, String acceptedByUsername, String startingDate, String startingTime, String durationToPickupLocation, String durationToDestination) {
        this.id = id;
        this.acceptedByUsername = acceptedByUsername;
        this.startingDate = startingDate;
        this.startingTime = startingTime;
        this.durationToPickupLocation = durationToPickupLocation;
        this.durationToDestination = durationToDestination;
    }
}
