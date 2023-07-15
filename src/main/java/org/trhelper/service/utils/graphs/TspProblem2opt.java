package org.trhelper.service.utils.graphs;


import org.trhelper.domain.order.PlacedOrderLocation;
import org.trhelper.service.utils.graphs.Edge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

public class TspProblem2opt {

    public static List<String> findShortestPath(List<Edge> edges, List<PlacedOrderLocation> locations, String idStartingNode) {
        Map<String, List<Edge>> adjacencyList = GraphUtils.createCompleteGraph(locations);

        List<Edge> minimumSpanningTree = KruskalAlgorithm.applyKruskalAlgorithm(locations);
        List<String> pathFromMST = pathFromMinimumSpanningTree(minimumSpanningTree, idStartingNode);
        List<String> refinedPath = twoOpt(pathFromMST, adjacencyList);

            for(String el:refinedPath){
                for(PlacedOrderLocation l:locations) {
                    if (l.getId().equals(el))
                        System.out.println(el+" "+l.getName());
                }
            }

        return refinedPath;
    }

    public static List<String> pathFromMinimumSpanningTree(List<Edge> minimumSpanningTree, String idStartingNode) {
        Map<String, List<String>> adjacencyList = new HashMap<>();
        for (Edge edge : minimumSpanningTree) {
            adjacencyList.putIfAbsent(edge.from.id, new ArrayList<>());
            adjacencyList.putIfAbsent(edge.to.id, new ArrayList<>());
            adjacencyList.get(edge.from.id).add(edge.to.id);
            adjacencyList.get(edge.to.id).add(edge.from.id);
        }

        List<String> path = new ArrayList<>();
        Set<String> visitedNodes = new HashSet<>();
        Stack<String> stack = new Stack<>();

        stack.push(idStartingNode);
        while (!stack.isEmpty()) {
            String currentNode = stack.pop();
            if (!visitedNodes.contains(currentNode)) {
                visitedNodes.add(currentNode);
                path.add(currentNode);

                List<String> neighbors = adjacencyList.get(currentNode);
                if (neighbors != null) {
                    for (String neighbor : neighbors) {
                        if (!visitedNodes.contains(neighbor)) {
                            stack.push(neighbor);
                        }
                    }
                }
            }
        }

        return path;
    }


    public static double pathDistance(List<String> path, Map<String, List<Edge>> adjacencyList) {
        double distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String currentNode = path.get(i);
            String nextNode = path.get(i + 1);
            for (Edge edge : adjacencyList.get(currentNode)) {
                if ((edge.from.id.equals(currentNode) && edge.to.id.equals(nextNode)) ||
                        (edge.from.id.equals(nextNode) && edge.to.id.equals(currentNode))) {
                    distance += edge.distance;
                    break;
                }
            }
        }
        return distance;
    }

    public static List<String> twoOpt(List<String> path, Map<String, List<Edge>> adjacencyList) {
        List<String> newPath = new ArrayList<>(path);
        boolean improved = true;

        while (improved) {
            improved = false;
            double bestDistance = pathDistance(newPath, adjacencyList);

            for (int i = 1; i < newPath.size() - 2; i++) {
                for (int j = i + 1; j < newPath.size() - 1; j++) {
                    List<String> candidate = new ArrayList<>(newPath);
                    Collections.reverse(candidate.subList(i, j + 1));

                    double candidateDistance = pathDistance(candidate, adjacencyList);
                    if (candidateDistance < bestDistance) {
                        newPath = candidate;
                        improved = true;
                        bestDistance = candidateDistance;
                    }
                }
            }
        }

        return newPath;
    }

}
