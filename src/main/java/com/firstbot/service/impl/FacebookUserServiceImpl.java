package com.firstbot.service.impl;

import com.firstbot.model.Hairdresser;
import com.firstbot.model.UserProfile;
import com.firstbot.repository.UserFacebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FacebookUserServiceImpl {
    @Autowired
    UserFacebookRepository userFacebookRepository;

    public Hairdresser addPerson(long idFacebook, String dayHairCut, String typeHairCut, LocalDateTime localDateTime, boolean reminder){
        return userFacebookRepository.saveAndFlush(new Hairdresser(idFacebook,dayHairCut,typeHairCut,localDateTime,reminder));
    }
    public UserProfile addUserProfile(String firstName, String secondName,String gender ){
        return null;
    }
    public List<Hairdresser> findByDayHairCut(String day){
        return userFacebookRepository.findByDayHairCut(day);
    }
    public List<Hairdresser> findByDateHairCut(){
        return userFacebookRepository.findByDateHairCut();
    }
    public void updateReminder(boolean b, long id){
        userFacebookRepository.updateReminder(b,id);
    }
}
