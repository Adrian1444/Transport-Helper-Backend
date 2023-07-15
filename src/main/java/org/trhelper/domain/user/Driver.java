package org.trhelper.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table
public class Driver extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehiclesOwned;

    public Driver(String username, String password, String firstName, String lastName, String email, String phoneNumber,String nationality, String vehiclesOwned, Long id) {
        super(username, password, firstName, lastName, email, phoneNumber, nationality);
        this.vehiclesOwned=vehiclesOwned;
        this.id = id;
    }

    public Driver(String username, String password, String firstName, String lastName, String email, String phoneNumber,String nationality, String vehiclesOwned) {
        super(username, password, firstName, lastName, email, phoneNumber, nationality);
        this.vehiclesOwned=vehiclesOwned;
    }
}
