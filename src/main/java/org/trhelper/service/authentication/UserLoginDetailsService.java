package org.trhelper.service.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.trhelper.domain.user.User;
import org.trhelper.repository.ClientRepository;
import org.trhelper.repository.DriverRepository;
import org.trhelper.service.user.implementation.UserCommonServiceImpl;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserLoginDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    UserCommonServiceImpl userCommonServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        User user=clientRepository.findClientByUsername(username);
        if(user==null){
            user=driverRepository.findDriverByUsername(username);
            if(user==null)
                throw new UsernameNotFoundException("User not found!");
        }
        String role=userCommonServiceImpl.findUserRole(username);
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),authorities);
    }
}
