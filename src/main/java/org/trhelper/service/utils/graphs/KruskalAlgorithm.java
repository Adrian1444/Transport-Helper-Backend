package org.trhelper.service.utils.graphs;

import org.trhelper.domain.order.PlacedOrderLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KruskalAlgorithm {


    public static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];

            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false;
            }

            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            return true;
        }
    }

    public static List<Edge> applyKruskalAlgorithm(List<PlacedOrderLocation> locations) {
        List<Edge> edges = new ArrayList<>();
        List<Edge> allEdges = new ArrayList<>();

        // Generate a complete graph
        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                double directDistance = GraphUtils.distance(locations.get(i), locations.get(j));
                allEdges.add(new Edge(locations.get(i), locations.get(j), directDistance));
            }
        }

        // Sort the edges by weight (ascending)
        allEdges.sort(Comparator.comparingDouble(edge -> edge.distance));

        // Apply Kruskal's algorithm
        UnionFind uf = new UnionFind(locations.size());
        for (Edge edge : allEdges) {
            if (uf.union(Integer.parseInt(edge.from.getId()), Integer.parseInt(edge.to.getId()))) {
                edges.add(edge);
            }
        }

        return edges;
    }
}
