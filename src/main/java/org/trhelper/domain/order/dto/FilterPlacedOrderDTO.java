package org.trhelper.domain.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trhelper.domain.order.TransportType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FilterPlacedOrderDTO {

    public FilterPlacedOrderDTO(String origin, String destination, List<TransportType> transportTypeList, Double minWeight, Double maxWeight, Integer minNumberOfItems, Integer maxNumberOfItems, String pickupDate, Double minCost, Double maxCost) {
        this.origin = origin;
        this.destination = destination;
        this.transportTypeList = transportTypeList;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.minNumberOfItems = minNumberOfItems;
        this.maxNumberOfItems = maxNumberOfItems;
        this.pickupDate = pickupDate;
        this.minCost = minCost;
        this.maxCost = maxCost;
    }

    String origin;
    String destination;
    List<TransportType> transportTypeList;
    Double minWeight;
    Double maxWeight;
    Integer minNumberOfItems;
    Integer maxNumberOfItems;
    String pickupDate;
    Double minCost;
    Double maxCost;
}
