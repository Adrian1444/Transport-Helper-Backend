package org.trhelper.service.user.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trhelper.domain.order.dto.RegisterDataDTO;
import org.trhelper.domain.user.Client;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.User;
import org.trhelper.domain.user.UserAvatar;
import org.trhelper.repository.ClientRepository;
import org.trhelper.repository.DriverRepository;
import org.trhelper.repository.UserAvatarRepository;
import org.trhelper.security.JwtUtil;
import org.trhelper.service.user.UserCommonService;

import java.util.Optional;

@Service
public class UserCommonServiceImpl implements UserCommonService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserAvatarRepository userAvatarRepository;


    @Override
    public String findUserRole(String username) {
        User user=clientRepository.findClientByUsername(username);
        if(user==null){
            user=driverRepository.findDriverByUsername(username);
            if(user==null)
                return null;
            return "driver";
        }
        return "client";
    }

    @Transactional
    public void register(RegisterDataDTO registerDataDTO) throws Exception {
        String username=registerDataDTO.getUsername();
        String password=registerDataDTO.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        password=bCryptPasswordEncoder.encode(password);
        String firstName= registerDataDTO.getFirstName();
        String lastName=registerDataDTO.getLastName();
        String email=registerDataDTO.getEmail();
        String phoneNumber= registerDataDTO.getPhoneNumber();
        String nationality= registerDataDTO.getNationality();
        String vehiclesOwned= registerDataDTO.getVehiclesOwned();
        Optional<UserAvatar> avatar= Optional.ofNullable(userAvatarRepository.findUserAvatarByName("DEFAULT_AVATAR"));
        if(driverRepository.findDriverByUsername(username)!=null || clientRepository.findClientByUsername(username)!=null)
            throw new Exception("Username already exists");
        if(registerDataDTO.getAccountType().equals("driverType")){
            Driver driver=new Driver(username,password,firstName,lastName,email,phoneNumber,nationality,vehiclesOwned);
            driver.setUserAvatar(avatar.get());
            driverRepository.save(driver);
        }
        else{
            Client client=new Client(username,password,firstName,lastName,email,phoneNumber,nationality);
            client.setUserAvatar(avatar.get());
            clientRepository.save(client);
        }
    }

}
