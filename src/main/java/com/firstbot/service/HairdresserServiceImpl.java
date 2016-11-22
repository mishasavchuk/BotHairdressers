package com.firstbot.service;

import com.firstbot.entity.Hairdresser;
import com.firstbot.entity.User;
import com.firstbot.repository.HairdresserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HairdresserServiceImpl {
    @Autowired
    HairdresserRepository hairdresserRepository;

    public Hairdresser addPerson(long idFacebook, User userProfile, String dayHairCut, String typeHairCut, LocalDateTime localDateTime, boolean reminder) {
        return hairdresserRepository.saveAndFlush(new Hairdresser(userProfile, typeHairCut, dayHairCut, localDateTime, reminder));
    }

    public List<Hairdresser> findByDayHairCut(String day) {
        return hairdresserRepository.findByDayHairCut(day);
    }

    /*public List<Hairdresser> findByUser(User user){
        return hairdresserRepository.findByUser(user);
    }*/
    public List<Hairdresser> findReminderDateHairCut() {
        return hairdresserRepository.findReminderByDateHairCut();
    }

    public void updateReminder(boolean b, User user, LocalDateTime dateHairCut) {
        hairdresserRepository.updateReminder(b, user, dateHairCut);
    }

    public List<Hairdresser> findAll() {
        return hairdresserRepository.findAll();
    }
}
