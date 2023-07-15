package org.trhelper.service.utils.graphs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.trhelper.domain.order.PlacedOrderLocation;

@Getter
@Setter
@NoArgsConstructor
public class Edge {
    public PlacedOrderLocation from;
    public PlacedOrderLocation to;
    public double distance;

    public Edge(PlacedOrderLocation from, PlacedOrderLocation to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }
}
