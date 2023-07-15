package org.trhelper.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
public abstract class User {

    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected String nationality;

    @OneToOne
    protected UserAvatar userAvatar;

    public User(String username, String password, String firstName, String lastName, String email, String phoneNumber, String nationality) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
    }
}
