package org.trhelper.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trhelper.domain.order.TransportType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class GetPlacedOrderDTO {

    public GetPlacedOrderDTO(String origin, String destination, TransportType transportType, Double weight, Integer numberOfItems,Double cost, String pickupDate, String additionalInformation, String placedByUsername,String latitudeOrigin,String longitudeOrigin,
                             String latitudeDestination,String longitudeDestination) {
        this.origin = origin;
        this.destination = destination;
        this.transportType = transportType;
        this.weight = weight;
        this.numberOfItems = numberOfItems;
        this.cost=cost;
        this.pickupDate = pickupDate;
        this.additionalInformation = additionalInformation;
        this.placedByUsername = placedByUsername;
        this.latitudeDestination=latitudeDestination;
        this.latitudeOrigin=latitudeOrigin;
        this.longitudeDestination=longitudeDestination;
        this.longitudeOrigin=longitudeOrigin;
    }

    private String origin;

    private String destination;

    @Enumerated(EnumType.STRING)
    TransportType transportType;

    private Double weight;

    private Integer numberOfItems;

    private Double cost;

    private String pickupDate;

    private String additionalInformation;

    private String placedByUsername;

    private String latitudeOrigin;

    private String longitudeOrigin;

    private String latitudeDestination;

    private String longitudeDestination;
}
