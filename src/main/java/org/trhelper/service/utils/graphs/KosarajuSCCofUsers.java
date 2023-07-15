package org.trhelper.service.utils.graphs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.FollowingRelation;
import org.trhelper.repository.FollowingRelationRepository;

import java.util.*;

public class KosarajuSCCofUsers {

    private Map<Driver, List<Driver>> graph;
    private Map<Driver, List<Driver>> reverseGraph;
    private Set<Driver> visited;
    private Stack<Driver> stack;
    private Set<Driver> scc;


    public Set<Driver> findSCCs(List<FollowingRelation> relations, Driver targetDriver) {
        graph = buildGraphFromRelations(relations);
        reverseGraph = buildReverseGraphFromRelations(relations);
        visited = new HashSet<>();
        stack = new Stack<>();
        scc = new HashSet<>();

        for (Driver driver : graph.keySet()) {
            if (!visited.contains(driver)) {
                fillOrder(driver);
            }
        }

        visited.clear();

        while (!stack.isEmpty()) {
            Driver driver = stack.pop();

            if (!visited.contains(driver)) {
                DFS(driver);
                if (scc.contains(targetDriver)) {
                    return scc;
                }
                scc.clear();
            }
        }

        return new HashSet<>();
    }

    private Map<Driver, List<Driver>> buildGraphFromRelations(List<FollowingRelation> relations) {
        Map<Driver, List<Driver>> graph = new HashMap<>();

        for (FollowingRelation relation : relations) {
            Driver follower = relation.getFollower();
            Driver followed = relation.getFollowed();
            graph.computeIfAbsent(follower, k -> new ArrayList<>()).add(followed);
        }

        return graph;
    }

    private Map<Driver, List<Driver>> buildReverseGraphFromRelations(List<FollowingRelation> relations) {
        Map<Driver, List<Driver>> graph = new HashMap<>();

        for (FollowingRelation relation : relations) {
            Driver follower = relation.getFollower();
            Driver followed = relation.getFollowed();
            graph.computeIfAbsent(followed, k -> new ArrayList<>()).add(follower);
        }

        return graph;
    }

    private void fillOrder(Driver driver) {
        visited.add(driver);

        for (Driver neighbor : graph.getOrDefault(driver, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                fillOrder(neighbor);
            }
        }

        stack.push(driver);
    }

    private void DFS(Driver driver) {
        visited.add(driver);
        scc.add(driver);

        for (Driver neighbor : reverseGraph.getOrDefault(driver, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                DFS(neighbor);
            }
        }
    }

}

