package org.trhelper.service.user;

import org.springframework.stereotype.Service;
import org.trhelper.domain.order.dto.RegisterDataDTO;

@Service
public interface UserCommonService {
    String findUserRole(String username);

}
