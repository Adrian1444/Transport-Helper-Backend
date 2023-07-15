package org.trhelper.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trhelper.domain.order.OrderStatus;
import org.trhelper.domain.order.TransportType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class SendPlacedOrderDTO {
    public SendPlacedOrderDTO(Long id, String origin, String destination, TransportType transportType, Double weight, Integer numberOfItems, String pickupDate, String additionalInformation, String placedByUsername
    , String acceptedByUsername, OrderStatus status, String timestamp, Double cost, String pickupTime, String arrivalToDestinationTime) {
        this.id=id;
        this.origin = origin;
        this.destination = destination;
        this.transportType = transportType;
        this.weight = weight;
        this.numberOfItems = numberOfItems;
        this.pickupDate = pickupDate;
        this.additionalInformation = additionalInformation;
        this.placedByUsername = placedByUsername;
        this.acceptedByUsername=acceptedByUsername;
        this.status=status;
        this.timestamp=timestamp;
        this.cost=cost;
        this.pickupTime=pickupTime;
        this.arrivalToDestinationTime=arrivalToDestinationTime;
    }

    private Long id;

    private String origin;

    private String destination;

    @Enumerated(EnumType.STRING)
    TransportType transportType;

    private Double weight;

    private Integer numberOfItems;

    private String pickupDate;

    private String additionalInformation;

    private String placedByUsername;

    private String acceptedByUsername;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    private String timestamp;

    private Double cost;

    private String pickupTime;

    private String arrivalToDestinationTime;


}
