package org.trhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.trhelper.domain.order.dto.GetEditUserDataDTO;
import org.trhelper.domain.order.dto.UserDataDTO;
import org.trhelper.domain.user.Client;
import org.trhelper.service.user.implementation.ClientServiceImpl;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/user/client")
public class ClientController {

    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping("/find")
    public ResponseEntity<?> findAll() {

        final List<Client> clients;
        clients = clientService.findAll();
        return ResponseEntity.ok().body(clients);
    }

    @GetMapping("/find/entity/{username}")
    public ResponseEntity<?> findClientDataByUsername(@PathVariable String username) {

        UserDataDTO driverData= clientService.findClientDataByUsername(username);
        return ResponseEntity.ok().body(driverData);
    }

    @PostMapping("/upload/avatar/{username}")
    public ResponseEntity<String> uploadPhoto(@PathVariable String username,@RequestParam("photo") MultipartFile photo) {
        try {
            clientService.uploadClientAvatar(photo,username);
            return ResponseEntity.status(HttpStatus.OK).body("Photo uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }

    @PutMapping("/edit/data")
    public ResponseEntity<String> editUserData(@RequestBody GetEditUserDataDTO editUserDataDTO) {
        try {
            clientService.editUserData(editUserDataDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Client updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating relation");
        }
    }
}