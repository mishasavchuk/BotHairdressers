package com.firstbot.service.impl;

import com.firstbot.constant.Day;
import com.firstbot.entity.Hairdresser;
import com.firstbot.entity.User;
import com.firstbot.repository.HairdresserRepository;
import com.firstbot.service.HairdresserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HairdresserServiceImpl implements HairdresserService {

    private final HairdresserRepository hairdresserRepository;

    @Autowired
    public HairdresserServiceImpl(HairdresserRepository hairdresserRepository) {
        this.hairdresserRepository = hairdresserRepository;
    }

    @Override
    public Hairdresser addPerson(long idFacebook, User userProfile, Day dayHairCut, String typeHairCut, LocalDateTime localDateTime, boolean reminder) {
        return hairdresserRepository.saveAndFlush(new Hairdresser(userProfile, typeHairCut, dayHairCut, localDateTime, reminder));
    }

    @Override
    public List<Hairdresser> findByDayHairCut(Day day) {
        return hairdresserRepository.findByDayHairCut(day);
    }

    @Override
    public List<Hairdresser> findReminderDateHairCut() {
        return hairdresserRepository.findReminderByDateHairCut();
    }

    @Override
    public void updateReminder(boolean b, User user, LocalDateTime dateHairCut) {
        hairdresserRepository.updateReminder(b, user, dateHairCut);
    }

    @Override
    public List<Hairdresser> findAll() {
        return hairdresserRepository.findAll();
    }
}
