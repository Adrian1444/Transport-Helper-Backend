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
public class Client extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Client(String username, String password, String firstName, String lastName, String email, String phoneNumber,String nationality, Long id) {
        super(username, password, firstName, lastName, email, phoneNumber, nationality);
        this.id = id;
    }

    public Client(String username, String password, String firstName, String lastName, String email, String phoneNumber,String nationality) {
        super(username, password, firstName, lastName, email, phoneNumber, nationality);
    }
}
