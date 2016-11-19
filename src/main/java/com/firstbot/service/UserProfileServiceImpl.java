package com.firstbot.service;

import com.firstbot.constant.State;
import com.firstbot.entity.User;
import com.firstbot.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileServiceImpl {
    @Autowired
    UserProfileRepository userProfileRepository;

    public User addUserProfile(long idFacebook, String firstName, String lastName, String gender, State state){
        return userProfileRepository.saveAndFlush(new User(idFacebook,firstName,lastName,gender,state));
    }
    public State findState(long id) {
        return userProfileRepository.findState(id);
    }
    public void updateState(long id,State state){
        userProfileRepository.updateUserState(id,state);
    }
    public int findUserId(long id){
        return userProfileRepository.findUserId(id);
    }
    public List<Long> findAllFacebookId(){
        return userProfileRepository.findAllFacebookId();
    }
    public User findUserByFacebookId(long id){
        return userProfileRepository.findUserByFacebookId(id);
    }
}
