package org.trhelper.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
@AllArgsConstructor
public class FollowingRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    Driver follower;

    @ManyToOne
    Driver followed;

    public FollowingRelation(Driver driver1, Driver driver2) {
        follower=driver1;
        followed=driver2;
    }
}
