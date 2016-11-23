package com.firstbot.service.impl;

import com.firstbot.entity.Hairdresser;
import com.firstbot.service.FacebookService;
import com.firstbot.service.HairdresserService;
import com.firstbot.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final HairdresserService hairdresserService;
    private final FacebookService facebookService;

    @Autowired
    public ReminderServiceImpl(HairdresserService hairdresserService, FacebookService facebookService) {
        this.hairdresserService = hairdresserService;
        this.facebookService = facebookService;
    }

    @Override
    @Scheduled(fixedDelay = 5000)
    public void sendReminder() {
        hairdresserService.findReminderDateHairCut().stream()
                .filter(Hairdresser::isReminder)
                .peek(hairdresser -> {
                            facebookService.sendText(
                                    hairdresser.getUser().getIdFacebook(),
                                    "Do not forget come to hairdresser today on " + hairdresser.getDateHairCut().getHour() + ":00");
                        }
                )
                .forEach(hairdresser -> {
                    hairdresserService.updateReminder(false, hairdresser.getUser(), hairdresser.getDateHairCut());
                });
    }
}
