package org.trhelper.service.user.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.trhelper.domain.order.dto.GetEditUserDataDTO;
import org.trhelper.domain.order.dto.UserDataDTO;
import org.trhelper.domain.order.dto.SendPostDTO;
import org.trhelper.domain.user.*;
import org.trhelper.repository.DriverRepository;
import org.trhelper.repository.FollowingRelationRepository;
import org.trhelper.repository.PostRepository;
import org.trhelper.repository.UserAvatarRepository;
import org.trhelper.service.user.DriverService;
import org.trhelper.service.utils.ListOperations;
import org.trhelper.service.utils.TimeFormatterUtils;
import org.trhelper.service.utils.graphs.BFSAlgorithmForUserRelations;
import org.trhelper.service.utils.graphs.KosarajuSCCofUsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserAvatarRepository userAvatarRepository;

    @Autowired
    private FollowingRelationRepository followingRelationRepository;

    @Autowired
    private PostRepository postRepository;


    public List<Driver> findAll(){return driverRepository.findAll();}

    public Driver findByUsername(String username) throws Exception{
        Driver driver=driverRepository.findDriverByUsername(username);
        if(driver==null)
            throw new Exception("No driver with this username could be found");
        return driver;
    }

    public UserDataDTO convertDriverToDTO(Driver driver){
        UserDataDTO driverData= new UserDataDTO();
        driverData.setUsername(driver.getUsername());
        driverData.setFirstName(driver.getFirstName());
        driverData.setLastName(driver.getLastName());
        driverData.setEmail(driver.getEmail());
        driverData.setPhoneNumber(driver.getPhoneNumber());
        driverData.setNationality(driver.getNationality());
        driverData.setVehiclesOwned(driver.getVehiclesOwned());
        UserAvatar userAvatar = driver.getUserAvatar();
        if (userAvatar != null) {
            String avatarBase64 = Base64.getEncoder().encodeToString(userAvatar.getData());
            driverData.setAvatarBase64(avatarBase64);
            driverData.setAvatarContentType(userAvatar.getContentType());
        }
        return driverData;
    }

    public UserDataDTO findDriverDataByUsername(String username) {
        Driver driver=driverRepository.findDriverByUsername(username);
        UserDataDTO driverData= convertDriverToDTO(driver);
        return driverData;
    }

    public UserAvatar findUserAvatar(String username){
        return driverRepository.findDriverByUsername(username).getUserAvatar();
    }

    public void uploadDriverAvatar(MultipartFile photo,String username) throws IOException {
        UserAvatar newPhoto = new UserAvatar();
        newPhoto.setName(photo.getOriginalFilename());
        newPhoto.setContentType(photo.getContentType());
        newPhoto.setData(photo.getBytes());
        userAvatarRepository.save(newPhoto);
        driverRepository.updateUserAvatar(newPhoto,username);
    }

    public List<UserDataDTO> searchUsers(String username, String searchString) {
        Driver currentDriver=driverRepository.findDriverByUsername(username);
        String[] parts = searchString.split("\\s+");
        List<Driver> finalResult=new ArrayList<>();
        BFSAlgorithmForUserRelations bfsAlgorithmForUserRelations=new BFSAlgorithmForUserRelations();
        for(String part: parts){
            finalResult=ListOperations.union(finalResult,bfsAlgorithmForUserRelations.bfsTraversal(followingRelationRepository.findAll(),currentDriver,part));
        }
        for (String part : parts) {
            List<Driver> users = driverRepository.findByLastNameContainingOrFirstNameContaining(part, part);
            finalResult=ListOperations.union(finalResult,users);
        }
        List<UserDataDTO> finalResultDTOs=new ArrayList<>();
        for(Driver driver:finalResult){
            finalResultDTOs.add(convertDriverToDTO(driver));
        }
        return finalResultDTOs;
    }

    public void createFollowRelation(String username1, String username2) throws Exception {
        Driver driver1=findByUsername(username1);
        Driver driver2=findByUsername(username2);
        FollowingRelation relation=new FollowingRelation(driver1,driver2);
        followingRelationRepository.save(relation);
    }

    public String getFollowRelation(String username1, String username2) throws Exception {
        Driver driver1=findByUsername(username1);
        Driver driver2=findByUsername(username2);
        if(followingRelationRepository.findFollowingRelationByFollowerAndFollowed(driver1,driver2)!=null)
            return "following";
        else
            return "not following";
    }

    public void deleteFollowRelation(String username1, String username2) throws Exception {
        Driver driver1=findByUsername(username1);
        Driver driver2=findByUsername(username2);
        FollowingRelation relation=followingRelationRepository.findFollowingRelationByFollowerAndFollowed(driver1,driver2);
        if(relation==null)
            throw new Exception("Relation between these 2 drivers does not exist");
        else
            followingRelationRepository.delete(relation);
    }

    public void createPost(String username, String content) throws Exception {
        Driver driver=findByUsername(username);
        String timestamp= TimeFormatterUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss");
        UserPost post=new UserPost(driver,content,timestamp);
        postRepository.save(post);
    }

    public List<Driver> findPeopleFollowedByUser(Driver driver){
        List<FollowingRelation> usersRelations=followingRelationRepository.findByFollower(driver);
        List<Driver> peopleFollowed=new ArrayList<>();
        for(FollowingRelation relation: usersRelations)
            peopleFollowed.add(relation.getFollowed());
        return peopleFollowed;
    }

    public List<UserDataDTO> findPeopleFollowedByUserAsDTOList(String username) throws Exception {
        Driver driver=findByUsername(username);
        List<UserDataDTO> result=new ArrayList<>();
        List<Driver> followedPeople=findPeopleFollowedByUser(driver);
        for(Driver followedPerson:followedPeople){
            UserDataDTO driverDataDTO=convertDriverToDTO(followedPerson);
            result.add(driverDataDTO);
        }
        return result;
    }

    public List<SendPostDTO> getPostsFromFollowedPersons(String username) throws Exception {
        Driver driver=findByUsername(username);
        List<Driver> followedPeople=findPeopleFollowedByUser(driver);
        followedPeople.add(driver);
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");
        List<UserPost> posts= postRepository.findByPostCreatorIn(followedPeople, sort);
        List<SendPostDTO> dtoPostsList=new ArrayList<>();
        for(UserPost post:posts) {
            UserDataDTO driverData=convertDriverToDTO(post.getPostCreator());
            dtoPostsList.add(new SendPostDTO(post.getId(),driverData, post.getContent(), post.getTimestamp()));
        }
        return dtoPostsList;
    }

    public List<UserDataDTO> findUserRecommendations(String username) throws Exception{
        Driver currentDriver=findByUsername(username);
        KosarajuSCCofUsers kosarajuSCCofUsers=new KosarajuSCCofUsers();
        Set<Driver> recommendations=kosarajuSCCofUsers.findSCCs(followingRelationRepository.findAll(),currentDriver);
        List<Driver> peopleFollowedByUser=findPeopleFollowedByUser(currentDriver);
        peopleFollowedByUser.add(currentDriver);
        List<UserDataDTO> recommendationsDTOList=new ArrayList<>();
        for(Driver driver:recommendations ){
            if(!peopleFollowedByUser.contains(driver)){
                recommendationsDTOList.add(convertDriverToDTO(driver));
            }
        }
        return recommendationsDTOList;
    }

    public void editUserData(GetEditUserDataDTO editUserDataDTO) {
        Driver driver=driverRepository.findDriverByUsername(editUserDataDTO.getUsername());
        driver.setEmail(editUserDataDTO.getEmail());
        driver.setVehiclesOwned(editUserDataDTO.getVehiclesOwned());
        driver.setPhoneNumber(editUserDataDTO.getPhoneNumber());
        driver.setFirstName(editUserDataDTO.getFirstName());
        driver.setLastName(editUserDataDTO.getLastName());
        driverRepository.save(driver);
    }
}
