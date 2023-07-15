package org.trhelper.service.utils.graphs;

import org.springframework.beans.factory.annotation.Autowired;
import org.trhelper.domain.order.PlacedOrder;
import org.trhelper.domain.order.PlacedOrderLocation;
import org.trhelper.repository.order.PlacedOrderRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstructOrderDistancesGraph {


    //create edge between 2 nodes only if there is no other route through other nodes between them that is not with more than 30% longer
    private static final double THRESHOLD = 0.1;


    public static List<Edge> applyThresholdCriteria(List<PlacedOrderLocation> locations, List<Edge> mstEdges) {
        List<Edge> thresholdEdges = new ArrayList<>();

        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                double directDistance = GraphUtils.distance(locations.get(i), locations.get(j));
                boolean edgeRequired = true;

                for (int k = 0; k < locations.size(); k++) {
                    if (k != i && k != j) {
                        double alternativeDistance = GraphUtils.distance(locations.get(i), locations.get(k))
                                + GraphUtils.distance(locations.get(k), locations.get(j));
                        if (alternativeDistance <= directDistance * (1 + THRESHOLD)) {
                            edgeRequired = false;
                            break;
                        }
                    }
                }

                if (edgeRequired && !GraphUtils.edgeExists(mstEdges, i, j)) {
                    thresholdEdges.add(new Edge(locations.get(i), locations.get(j), directDistance));
                }
            }
        }

        return thresholdEdges;
    }

    public static List<Edge> constructGraph(List<PlacedOrderLocation> locations) {
        List<Edge> edges = KruskalAlgorithm.applyKruskalAlgorithm(locations);
        List<Edge> thresholdEdges = applyThresholdCriteria(locations, edges);
        edges.addAll(thresholdEdges);
        return edges;
    }


}



