package com.firstbot.service;

import com.firstbot.constant.Gender;
import com.firstbot.constant.State;
import com.firstbot.entity.User;

import java.util.List;

public interface UserService {
    User addUserProfile(long idFacebook, String firstName, String lastName, Gender gender, State state);

    State findState(long id);

    void updateState(long id, State state);

    int findUserId(long id);

    List<Long> findAllUser();

    User findUserByFacebookId(long id);

    void createUserIfNotExist(long id);
}
