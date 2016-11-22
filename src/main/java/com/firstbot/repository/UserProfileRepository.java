package com.firstbot.repository;

import com.firstbot.constant.State;
import com.firstbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserProfileRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("UPDATE User u SET u.state = ?2 WHERE u.idFacebook = ?1")
    void updateUserState(long id, State state);

    @Query("SELECT u.state FROM User u WHERE u.idFacebook = ?1")
    State findStateByIdFacebook(long id);

    int findUserIdByIdFacebook(long id);

    @Query("SELECT u.idFacebook FROM User u")
    List<Long> findAllFacebookId();

    User findUserByIdFacebook(long id);

}
