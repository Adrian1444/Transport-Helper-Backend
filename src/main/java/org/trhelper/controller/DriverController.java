package org.trhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.trhelper.domain.order.dto.GetEditUserDataDTO;
import org.trhelper.domain.order.dto.UserDataDTO;
import org.trhelper.domain.order.dto.GetPostDTO;
import org.trhelper.domain.order.dto.SendPostDTO;
import org.trhelper.domain.user.Driver;
import org.trhelper.domain.user.UserAvatar;
import org.trhelper.service.user.implementation.DriverServiceImpl;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping(path="/user/driver")
public class DriverController {

    @Autowired
    private DriverServiceImpl driverService;

    @GetMapping("/find")
    public ResponseEntity<?> findAll() {

        final List<Driver> drivers;
        drivers = driverService.findAll();
        return ResponseEntity.ok().body(drivers);
    }

    @GetMapping("/find/entity/{username}")
    public ResponseEntity<?> findDriverDataByUsername(@PathVariable String username) {

        UserDataDTO driverData= driverService.findDriverDataByUsername(username);
        return ResponseEntity.ok().body(driverData);
    }

    @GetMapping("/find/avatar/{username}")
    public ResponseEntity<byte[]> findDriverAvatarByUsername(@PathVariable String username) {
        UserAvatar userAvatar= driverService.findUserAvatar(username);
        if (userAvatar!=null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(userAvatar.getContentType()));
            return new ResponseEntity<>(userAvatar.getData(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.ok().body(null);

        }
    }

    @PostMapping("/upload/avatar/{username}")
    public ResponseEntity<String> uploadPhoto(@PathVariable String username,@RequestParam("photo") MultipartFile photo) {
        try {
            driverService.uploadDriverAvatar(photo,username);
            return ResponseEntity.status(HttpStatus.OK).body("Photo uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo");
        }
    }

    @GetMapping("/find/users/{username}/{searchString}")
    public ResponseEntity<?> searchUsers(@PathVariable String username,@PathVariable String searchString) {
        List<UserDataDTO> foundDrivers=driverService.searchUsers(username,searchString);

        return ResponseEntity.ok().body(foundDrivers);
    }

    @PostMapping("/create/follow/relation/{username1}/{username2}")
    public ResponseEntity<String> createFollowRelation(@PathVariable String username1,@PathVariable String username2) {
        try {
            driverService.createFollowRelation(username1,username2);
            return ResponseEntity.status(HttpStatus.OK).body("Relation created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating relation");
        }
    }

    @GetMapping("/get/follow/relation/{username1}/{username2}")
    public ResponseEntity<String> getFollowRelation(@PathVariable String username1,@PathVariable String username2) {
        try{
            return ResponseEntity.ok().body(driverService.getFollowRelation(username1,username2));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting relation");
        }
    }

    @DeleteMapping("/delete/follow/relation/{username1}/{username2}")
    public ResponseEntity<?> deleteFollowRelation(@PathVariable String username1,@PathVariable String username2) {
        try{
            driverService.deleteFollowRelation(username1,username2);
            return ResponseEntity.status(HttpStatus.OK).body("Relation created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting relation");
        }
    }

    @PostMapping("/create/post")
    public ResponseEntity<String> createPost(@RequestBody GetPostDTO postDTO) {
        try {
            driverService.createPost(postDTO.getUsername(),postDTO.getContent());
            return ResponseEntity.status(HttpStatus.OK).body("Relation created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating relation");
        }
    }

    @GetMapping("/get/posts/{username}")
    public ResponseEntity<?> getPosts(@PathVariable String username) {
        try {
            List<SendPostDTO> foundDrivers = driverService.getPostsFromFollowedPersons(username);
            return ResponseEntity.ok().body(foundDrivers);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error extracting posts");
        }
    }

    @GetMapping("/get/recommendations/{username}")
    public ResponseEntity<?> getRecommendations(@PathVariable String username) {
        try {
            List<UserDataDTO> foundDrivers = driverService.findUserRecommendations(username);
            return ResponseEntity.ok().body(foundDrivers);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error extracting posts");
        }
    }

    @GetMapping("/get/followed/people/{username}")
    public ResponseEntity<?> getFollowedPeople(@PathVariable String username) {
        try {
            List<UserDataDTO> foundDrivers = driverService.findPeopleFollowedByUserAsDTOList(username);
            return ResponseEntity.ok().body(foundDrivers);
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error extracting posts");
        }
    }

    @PutMapping("/edit/data")
    public ResponseEntity<String> editUserData(@RequestBody GetEditUserDataDTO editUserDataDTO) {
        try {
            driverService.editUserData(editUserDataDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Driver updated successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating driver");
        }
    }

}