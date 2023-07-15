package org.trhelper.service.user;


import org.springframework.stereotype.Service;
import org.trhelper.domain.user.Driver;

import java.util.List;

@Service
public interface DriverService {
    List<Driver> findAll();
}
