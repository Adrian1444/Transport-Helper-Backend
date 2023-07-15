package org.trhelper.service.utils.graphs;
import org.trhelper.domain.order.PlacedOrderLocation;
import org.trhelper.domain.order.PlacedOrderLocationType;

import java.util.*;

public class ShortestPathThroughOtherNodes {

    public static List<String> findShortestPathThroughNodes(List<PlacedOrderLocation> locations, List<Edge> edges, String sourceId, String destinationId, List<String> intermediateNodeIds) {
        Map<String, List<Edge>> adjacencyList = GraphUtils.createAdjacencyList(locations, edges);

        List<String> nodeIds = new ArrayList<>();
        nodeIds.add(sourceId);
        nodeIds.addAll(intermediateNodeIds);
        nodeIds.add(destinationId);

        List<PlacedOrderLocation> fullPath = new ArrayList<>();

        for (int i = 0; i < nodeIds.size() - 1; i++) {
            String fromId = nodeIds.get(i);
            String toId = nodeIds.get(i + 1);

            List<String> path=new ArrayList<>();
            path = DjikstraAlgorithm.dijkstra(adjacencyList, fromId, toId);

            if (path == null) {
                return null; // No path found
            }

            // Add the path to the fullPath, excluding the last node (to avoid duplicating nodes)
            for (int j = 0; j < path.size() - 1; j++) {
                fullPath.add(GraphUtils.getLocationById(locations, path.get(j)));
            }
        }

        // Add the destination node
        fullPath.add(GraphUtils.getLocationById(locations, destinationId));

        List<String> locationIds=new ArrayList<>();
        for(PlacedOrderLocation location:fullPath)
            locationIds.add(location.getId());
        return locationIds;
    }





}