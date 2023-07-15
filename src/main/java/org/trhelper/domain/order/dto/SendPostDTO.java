package org.trhelper.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendPostDTO {
    Long id;
    UserDataDTO userData;
    String content;
    String timestamp;

}
