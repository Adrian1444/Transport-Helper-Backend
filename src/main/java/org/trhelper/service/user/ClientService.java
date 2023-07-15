package org.trhelper.service.user;

import org.springframework.stereotype.Service;
import org.trhelper.domain.user.Client;

import java.util.List;

@Service
public interface ClientService {
    List<Client> findAll();
    Client findByUsername(String username) throws Exception;
}
