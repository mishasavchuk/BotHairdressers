package com.firstbot.service;

import com.firstbot.entity.Hairdresser;
import com.firstbot.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface HairdresserService {
    Hairdresser addPerson(long idFacebook, User userProfile, String dayHairCut, String typeHairCut, LocalDateTime localDateTime, boolean reminder);

    List<Hairdresser> findByDayHairCut(String day);

    List<Hairdresser> findReminderDateHairCut();

    void updateReminder(boolean b, User user, LocalDateTime dateHairCut);

    List<Hairdresser> findAll();
}
