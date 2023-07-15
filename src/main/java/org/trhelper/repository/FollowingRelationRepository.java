package org.trhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.FollowingRelation;

import java.util.List;

@Repository
public interface FollowingRelationRepository extends JpaRepository<FollowingRelation,Long> {
    FollowingRelation findFollowingRelationByFollowerAndFollowed(Driver follower, Driver followed);

    List<FollowingRelation> findByFollower(Driver follower);
    List<FollowingRelation> findByFollowed(Driver follower);
}
