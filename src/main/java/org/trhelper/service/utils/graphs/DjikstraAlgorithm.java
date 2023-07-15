package org.trhelper.service.utils.graphs;

import org.trhelper.domain.order.PlacedOrderLocation;

import java.util.*;

public class DjikstraAlgorithm {

    public static List<String> dijkstra(Map<String, List<Edge>> adjacencyList, String sourceId, String destinationId) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(location -> distances.get(location)));

        for (String locationId : adjacencyList.keySet()) {
            distances.put(locationId, Double.MAX_VALUE);
            previousNodes.put(locationId, null);
        }

        distances.put(sourceId, 0.0);
        pq.add(sourceId);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            String currentId = current;

            if (Objects.equals(currentId, destinationId)) {
                break;
            }

            for (Edge edge : adjacencyList.get(currentId)) {
                double newDistance = distances.get(currentId) + edge.distance;
                if (newDistance < distances.get(edge.to.getId())) {
                    distances.put(edge.to.getId(), newDistance);
                    previousNodes.put(edge.to.getId(), currentId);
                    pq.add(edge.to.getId());
                }
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
