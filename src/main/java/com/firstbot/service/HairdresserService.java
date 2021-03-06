package com.firstbot.service;

import com.firstbot.constant.Day;
import com.firstbot.entity.Hairdresser;
import com.firstbot.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface HairdresserService {
    Hairdresser addPerson(long idFacebook, User userProfile, Day dayHairCut, String typeHairCut, LocalDateTime localDateTime, boolean reminder);

    List<Hairdresser> findByDayHairCut(Day day);

    List<Hairdresser> findReminderDateHairCut();

    void updateReminder(boolean b, User user, LocalDateTime dateHairCut);

    List<Hairdresser> findAll();
}
