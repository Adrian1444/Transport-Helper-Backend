package org.trhelper.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String nationality;
    private String vehiclesOwned;
    private String avatarBase64;
    private String avatarContentType;

}
