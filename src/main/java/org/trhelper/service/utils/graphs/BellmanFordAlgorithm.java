package org.trhelper.service.utils.graphs;

import org.trhelper.domain.order.PlacedOrderLocation;

import java.util.*;

public class BellmanFordAlgorithm {

    public static List<String> bellmanFord(Map<String, List<Edge>> adjacencyList, String sourceId, String destinationId, List<PlacedOrderLocation> locations,List<Edge> edges) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();

        for (String locationId : adjacencyList.keySet()) {
            distances.put(locationId, Double.MAX_VALUE);
            previousNodes.put(locationId, null);
        }

        distances.put(sourceId, 0.0);

        int numLocations = locations.size();
        for (int i = 0; i < numLocations - 1; i++) {
            for (Edge edge : edges) {
                String fromId = edge.from.getId();
                String toId = edge.to.getId();
                double newDistance = distances.get(fromId) + edge.distance;
                if (newDistance < distances.get(toId)) {
                    distances.put(toId, newDistance);
                    previousNodes.put(toId, fromId);
                }
            }
        }

        // Check for negative weight cycles
        for (Edge edge : edges) {
            String fromId = edge.from.getId();
            String toId = edge.to.getId();
            double newDistance = distances.get(fromId) + edge.distance;
            if (newDistance < distances.get(toId)) {
                return null; // Negative weight cycle detected
            }
        }

        if (previousNodes.get(destinationId) == null) {
            return null; // No path found
        }

        // Reconstruct the path
        List<String> path = new ArrayList<>();
        String currentNode = destinationId;
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = previousNodes.get(currentNode);
        }
        Collections.reverse(path);

        return path;
    }

}
