package com.firstbot.service;

import com.firstbot.constant.Gender;
import com.firstbot.constant.State;
import com.firstbot.entity.User;
import com.firstbot.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public User addUserProfile(long idFacebook, String firstName, String lastName, Gender gender, State state) {
        return userProfileRepository.saveAndFlush(new User(idFacebook, firstName, lastName, gender, state));
    }

    public State findState(long id) {
        return userProfileRepository.findStateByIdFacebook(id);
    }

    public void updateState(long id, State state) {
        userProfileRepository.updateUserState(id, state);
    }

    public int findUserId(long id) {
        return userProfileRepository.findUserIdByIdFacebook(id);
    }

    public List<Long> findAllUser() {
        return userProfileRepository.findAllFacebookId();
    }

    public User findUserByFacebookId(long id) {
        return userProfileRepository.findUserByIdFacebook(id);
    }
}
