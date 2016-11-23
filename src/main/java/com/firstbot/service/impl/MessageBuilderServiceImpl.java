package com.firstbot.service.impl;

import com.firstbot.constant.Day;
import com.firstbot.constant.FacebookConstants;
import com.firstbot.constant.Hour;
import com.firstbot.constant.State;
import com.firstbot.entity.Hairdresser;
import com.firstbot.model.out.button.Button;
import com.firstbot.model.out.quickreplies.QuickReplies;
import com.firstbot.service.FacebookService;
import com.firstbot.service.MessageBuilderService;
import com.firstbot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageBuilderServiceImpl implements MessageBuilderService {

    private final FacebookService facebookService;

    private final UserService userService;

    @Autowired
    public MessageBuilderServiceImpl(FacebookService facebookService, UserService userService) {
        this.facebookService = facebookService;
        this.userService = userService;
    }

    @Override
    public List<Button> createButtons() {
        List<Button> buttons = new ArrayList<>();

        Button btnHairCut = new Button("hair cut", "HAIR CUT");
        Button btnBearCut = new Button("bear cut", "BEAR CUT");
        Button btnHairBearCut = new Button("hair&bear cut", "HAIR&BEAR CUT");

        buttons.add(btnHairCut);
        buttons.add(btnBearCut);
        buttons.add(btnHairBearCut);
        return buttons;
    }

    @Override
    public List<QuickReplies> createDayQuickReplies() {
        List<QuickReplies> replies = new ArrayList<>(Day.values().length);
        for (Day day : Day.values()) replies.add(new QuickReplies("text", day.name().toLowerCase(), day.name()));
        return replies;
    }

    @Override
    public List<QuickReplies> createHourQuickReplies(List<Hairdresser> hairdressers, long id) {
        List<LocalTime> listHour = new ArrayList<>();
        List<QuickReplies> replies = new ArrayList<>();

        for (Hairdresser hairdresser : hairdressers) {
            listHour.add(LocalTime.from(hairdresser.getDateHairCut()));
        }

        for (Hour hour : Hour.values()) {
            if (!listHour.contains(LocalTime.parse(hour.getTime()))) {
                replies.add(new QuickReplies(hour.getTime(), hour.getTime().substring(0, 2)));
            }
        }
        if (isFreeTimeToDoHairCut(replies)) {
            return replies;
        } else {
            facebookService.sendText(id, "Sorry is not free seats.");
            facebookService.sendQuickReplies(id, FacebookConstants.CHOOSE_DAY, createDayQuickReplies());
            userService.updateState(id, State.DAYQUICKREPLIES);
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isFreeTimeToDoHairCut(List<QuickReplies> quickReplies) {
        System.out.println("quickReplies: " + quickReplies);
        if (quickReplies != null && quickReplies.size() != 0) return true;
        else return false;
    }

}
