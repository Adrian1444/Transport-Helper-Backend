package org.trhelper.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlacedOrderLocation {
    public String id;
    public double latitude;
    public  double longitude;
    public  Long orderId;
    public  PlacedOrderLocationType type;
    public  String name;
    public double orderValue;

    public PlacedOrderLocation(String locationId,double latitude, double longitude, Long orderId, PlacedOrderLocationType type,String name,double orderValue) {
        this.id=locationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderId = orderId;
        this.type = type;
        this.name=name;
        this.orderValue=orderValue;
    }

    public PlacedOrderLocation(String locationId,double latitude, double longitude, Long orderId, PlacedOrderLocationType type,String name) {
        this.id=locationId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderId = orderId;
        this.type = type;
        this.name=name;
    }
}
