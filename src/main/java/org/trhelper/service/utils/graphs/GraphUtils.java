package org.trhelper.service.utils.graphs;

import org.trhelper.domain.order.PlacedOrderLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphUtils {

    public static Map<String, List<Edge>> createAdjacencyList(List<PlacedOrderLocation> locations, List<Edge> edges) {
        Map<String, List<Edge>> adjacencyList = new HashMap<>();

        for (PlacedOrderLocation location : locations) {
            adjacencyList.put(location.getId(), new ArrayList<>());
        }

        for (Edge edge : edges) {
            adjacencyList.get(edge.from.getId()).add(edge);
            adjacencyList.get(edge.to.getId()).add(new Edge(edge.to, edge.from, edge.distance));
        }

        return adjacencyList;
    }

    public static Map<String, List<Edge>> createAdjacencyListDirectedGraphs(List<PlacedOrderLocation> locations, List<Edge> edges) {
        Map<String, List<Edge>> adjacencyList = new HashMap<>();

        for (PlacedOrderLocation location : locations) {
            adjacencyList.put(location.getId(), new ArrayList<>());
        }

        for (Edge edge : edges) {
            adjacencyList.get(edge.from.getId()).add(edge);
        }

        return adjacencyList;
    }

    public static double distance(PlacedOrderLocation a, PlacedOrderLocation b) {
        double earthRadius = 6371; // Earth radius in km
        double latDiff = Math.toRadians(b.getLatitude() - a.getLatitude());
        double lonDiff = Math.toRadians(b.getLongitude() - a.getLongitude());
        double aComp = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(a.getLatitude())) * Math.cos(Math.toRadians(b.getLatitude()))
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double cComp = 2 * Math.atan2(Math.sqrt(aComp), Math.sqrt(1 - aComp));

        return earthRadius * cComp;
    }

    public static boolean edgeExists(List<Edge> edges, int from, int to) {
        for (Edge edge : edges) {
            if ((Integer.valueOf(edge.from.getId()) == from && Integer.valueOf(edge.to.getId()) == to) || (Integer.valueOf(edge.from.getId()) == to && Integer.valueOf(edge.to.getId()) == from)) {
                return true;
            }
        }
        return false;
    }

    public static PlacedOrderLocation getLocationById(List<PlacedOrderLocation> locations, String id) {
        for (PlacedOrderLocation location : locations) {
            if (location.getId().equals(id)) {
                return location;
            }
        }
        return null;
    }

    public static Map<String, List<Edge>> createCompleteGraph(List<PlacedOrderLocation> locations) {
        Map<String, List<Edge>> graph = new HashMap<>();

        for (PlacedOrderLocation location1 : locations) {
            List<Edge> edges = new ArrayList<>();
            for (PlacedOrderLocation location2 : locations) {
                if (!location1.getId().equals(location2.getId())) {
                    double dist = distance(location1, location2);
                    Edge edge = new Edge(location1, location2, dist);
                    edges.add(edge);
                }
            }
            graph.put(location1.getId(), edges);
        }

        return graph;
    }


}
