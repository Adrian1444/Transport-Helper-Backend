package org.trhelper.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trhelper.domain.user.Client;
import org.trhelper.domain.user.Driver;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PlacedOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public PlacedOrder(String origin, String destination, TransportType transportType,
                       Double weight, Integer numberOfItems, String pickupDate,
                       String additionalInformation, Client placedBy,
                       Driver acceptedBy, OrderStatus status, String timestamp, Double cost,
                       String pickupTime,String arrivalToDestinationTime,
                       String durationToPickupLocation, String durationToDestination,
                       String latitudeOrigin,String longitudeOrigin,
                       String latitudeDestination,String longitudeDestination
    ) {
        this.origin = origin;
        this.destination = destination;
        this.transportType = transportType;
        this.weight = weight;
        this.numberOfItems = numberOfItems;
        this.pickupDate = pickupDate;
        this.additionalInformation = additionalInformation;
        this.placedBy = placedBy;
        this.acceptedBy = acceptedBy;
        this.status=status;
        this.timestamp=timestamp;
        this.cost=cost;
        this.pickupTime=pickupTime;
        this.arrivalToDestinationTime=arrivalToDestinationTime;
        this.durationToPickupLocation=durationToPickupLocation;
        this.durationToDestination=durationToDestination;
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

    private String pickupDate;

    private String additionalInformation;

    @ManyToOne
    @JoinColumn(nullable = false,name = "client_id", referencedColumnName = "id")
    private Client placedBy;

    @ManyToOne
    @JoinColumn(nullable = true,name = "driver_id", referencedColumnName = "id")
    private Driver acceptedBy;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    private String timestamp;

    private Double cost;

    private String pickupTime;

    private String arrivalToDestinationTime;

    private String durationToPickupLocation;

    private String durationToDestination;

    private String latitudeOrigin;

    private String longitudeOrigin;

    private String latitudeDestination;

    private String longitudeDestination;
}
