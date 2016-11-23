package com.firstbot.service.impl;

import com.firstbot.constant.Gender;
import com.firstbot.constant.State;
import com.firstbot.entity.User;
import com.firstbot.repository.UserProfileRepository;
import com.firstbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static com.firstbot.constant.FacebookConstants.ACCESS_TOKEN;
import static com.firstbot.constant.FacebookConstants.PROFILE_URL;

@Service
public class UserServiceImpl implements UserService {
    private final UserProfileRepository userProfileRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserServiceImpl(UserProfileRepository userProfileRepository, RestTemplate restTemplate) {
        this.userProfileRepository = userProfileRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public User addUserProfile(long idFacebook, String firstName, String lastName, Gender gender, State state) {
        return userProfileRepository.saveAndFlush(new User(idFacebook, firstName, lastName, gender, state));
    }

    @Override
    public State findState(long id) {
        return userProfileRepository.findStateByIdFacebook(id);
    }

    @Override
    public void updateState(long id, State state) {
        userProfileRepository.updateUserState(id, state);
    }

    @Override
    public int findUserId(long id) {
        return userProfileRepository.findUserIdByIdFacebook(id);
    }

    @Override
    public List<Long> findAllUser() {
        return userProfileRepository.findAllFacebookId();
    }

    @Override
    public User findUserByFacebookId(long id) {
        return userProfileRepository.findUserByIdFacebook(id);
    }

    @Override
    public void createUserIfNotExist(long id) {
        if (!findAllUser().contains(id)) {
            getUser(id).ifPresent(user -> addUserProfile(id, user.getFirstName(), user.getLastName(),
                    user.getGender(),
                    State.TEXT));
        }
    }

    private Optional<User> getUser(long userId) {
        String requestUrl = PROFILE_URL.replace("$user_id$", String.valueOf(userId));
        return Optional.ofNullable(restTemplate.getForObject(requestUrl + ACCESS_TOKEN, User.class));
    }
}
