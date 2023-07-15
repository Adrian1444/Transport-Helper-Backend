package org.trhelper.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetEditUserDataDTO {
    String username;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String vehiclesOwned;
}
