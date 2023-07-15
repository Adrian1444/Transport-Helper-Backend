package org.trhelper.service.user.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.trhelper.domain.order.dto.GetEditUserDataDTO;
import org.trhelper.domain.order.dto.UserDataDTO;
import org.trhelper.domain.user.Client;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.UserAvatar;
import org.trhelper.repository.ClientRepository;
import org.trhelper.repository.UserAvatarRepository;
import org.trhelper.service.user.ClientService;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserAvatarRepository userAvatarRepository;

    public List<Client> findAll(){return clientRepository.findAll();}

    public Client findByUsername(String username) throws Exception{
        Client client=clientRepository.findClientByUsername(username);
        if(client==null)
            throw new Exception("No client with this username could be found");
        return client;
    }

    public UserDataDTO findClientDataByUsername(String username) {
        Client client=clientRepository.findClientByUsername(username);
        UserDataDTO clientData= convertClientToDTO(client);
        return clientData;
    }

    private UserDataDTO convertClientToDTO(Client client) {
        UserDataDTO clientData= new UserDataDTO();
        clientData.setUsername(client.getUsername());
        clientData.setFirstName(client.getFirstName());
        clientData.setLastName(client.getLastName());
        clientData.setEmail(client.getEmail());
        clientData.setPhoneNumber(client.getPhoneNumber());
        clientData.setNationality(client.getNationality());
        clientData.setVehiclesOwned("");
        UserAvatar userAvatar = client.getUserAvatar();
        if (userAvatar != null) {
            String avatarBase64 = Base64.getEncoder().encodeToString(userAvatar.getData());
            clientData.setAvatarBase64(avatarBase64);
            clientData.setAvatarContentType(userAvatar.getContentType());
        }
        return clientData;
    }

    public void uploadClientAvatar(MultipartFile photo, String username) throws IOException {
        UserAvatar newPhoto = new UserAvatar();
        newPhoto.setName(photo.getOriginalFilename());
        newPhoto.setContentType(photo.getContentType());
        newPhoto.setData(photo.getBytes());
        userAvatarRepository.save(newPhoto);
        clientRepository.updateUserAvatar(newPhoto,username);
    }

    public void editUserData(GetEditUserDataDTO editUserDataDTO) {
        Client driver=clientRepository.findClientByUsername(editUserDataDTO.getUsername());
        driver.setEmail(editUserDataDTO.getEmail());
        driver.setPhoneNumber(editUserDataDTO.getPhoneNumber());
        driver.setFirstName(editUserDataDTO.getFirstName());
        driver.setLastName(editUserDataDTO.getLastName());
        clientRepository.save(driver);
    }
}
