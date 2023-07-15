package org.trhelper.service.utils.graphs;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.FollowingRelation;
import org.trhelper.repository.FollowingRelationRepository;


import java.util.*;

public class BFSAlgorithmForUserRelations {

    public List<Driver> bfsTraversal(List<FollowingRelation> graphRelations,Driver startDriver, String searchString) {
        List<Driver> foundDrivers=new ArrayList<>();
        Queue<Driver> queue = new LinkedList<>();
        Set<Driver> visited = new HashSet<>();
        queue.add(startDriver);
        visited.add(startDriver);
        while (!queue.isEmpty()) {
            Driver currentDriver = queue.poll();
            if(currentDriver.getFirstName().contains(searchString) || currentDriver.getLastName().contains(searchString))
                foundDrivers.add(currentDriver);

            List<FollowingRelation> relations = findByFollower(graphRelations,currentDriver);
            for (FollowingRelation relation : relations) {
                Driver followedDriver = relation.getFollowed();
                if (!visited.contains(followedDriver)) {
                    queue.add(followedDriver);
                    visited.add(followedDriver);
                }
            }
        }
        return foundDrivers;
    }

    private List<FollowingRelation> findByFollower(List<FollowingRelation> relations,Driver currentDriver) {
        List<FollowingRelation> foundRelations=new ArrayList<>();
        for(FollowingRelation relation: relations){
            if(relation.getFollower().getId().equals(currentDriver.getId()))
                foundRelations.add(relation);
        }
        return foundRelations;
    }
}
