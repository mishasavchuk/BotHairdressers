package com.firstbot.processor.impl;

import com.firstbot.constant.Day;
import com.firstbot.constant.Hour;
import com.firstbot.constant.State;
import com.firstbot.entity.Hairdresser;
import com.firstbot.model.in.MessageFromFacebook;
import com.firstbot.model.in.Postback;
import com.firstbot.model.out.quickreplies.QuickReplies;
import com.firstbot.processor.MessagesProcessor;
import com.firstbot.service.FacebookService;
import com.firstbot.service.HairdresserService;
import com.firstbot.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


@Service
@Data
public class MessagesProcessorImpl implements MessagesProcessor {
    @Autowired
    private UserService userService;
    @Autowired
    private HairdresserService hairdresserService;
    @Autowired
    FacebookService facebookService;

    private String typeCut;
    private String dayCut;
    private String timeCut;

    public static LocalDateTime cutTime(String dayHairCut, String timeCut) {
        LocalDateTime date = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(dayHairCut)));
        LocalDateTime recordDate = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), Integer.parseInt(timeCut), 00);
        return recordDate;
    }

    public boolean isPressButton(MessageFromFacebook messageFromFacebook) {
        Postback postback = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback();
        return postback != null && postback.getPayload() != null;
    }

    public boolean isPressQuickReplies(MessageFromFacebook messageFromFacebook) {
        com.firstbot.model.in.Message message = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage();
        return message != null && message.getQuickReply() != null && message.getQuickReply().getPayload() != null;
    }

    public String pressQuickReplies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply().getPayload();
    }

    public void processMessage(long id) {
        facebookService.sendText(id, "Welcome to hairdresser, I help you record on hair cut ");
        facebookService.sendButton(id);
        userService.updateState(id, State.BUTTON);
    }

    public void processButton(long id, MessageFromFacebook messageFromFacebook) {
        if (isPressButton(messageFromFacebook)) {
            typeCut = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback().getPayload();
            facebookService.sendQuickDayReplies(id);
            userService.updateState(id, State.DAYQUICKREPLIES);
        } else {
            facebookService.sendButton(id);
        }
    }

    public void processDayQuickReplies(long id, MessageFromFacebook messageFromFacebook) {
        if (isPressQuickReplies(messageFromFacebook)) {
            dayCut = pressQuickReplies(messageFromFacebook);
            userService.updateState(id, State.HOURQUICKREPLIES);
            facebookService.sendQuickHourReplies(id);
        } else {
            facebookService.sendQuickDayReplies(id);
        }
    }

    public void processHourQuickReplies(long id, MessageFromFacebook messageFromFacebook) {
        if (isPressQuickReplies(messageFromFacebook)) {
            timeCut = pressQuickReplies(messageFromFacebook);
            userService.updateState(id, State.TEXT);
            facebookService.sendText(id, "Wait for you on " + dayCut.toLowerCase() + " " + timeCut + ":00");
            hairdresserService.addPerson(id, userService.findUserByFacebookId(id), dayCut, typeCut, cutTime(dayCut, timeCut), true);
        } else {
            facebookService.sendQuickHourReplies(id);
        }
    }

    public boolean isFreeTimeToDoHairCut(List<QuickReplies> quickReplies) {
        System.out.println("quickReplies: " + quickReplies);
        if (quickReplies != null && quickReplies.size() != 0) return true;
        else return false;
    }

    public List<QuickReplies> createDayQuickReplies() {
        List<QuickReplies> replies = new ArrayList<>(Day.values().length);
        for (Day day : Day.values()) replies.add(new QuickReplies("text", day.name().toLowerCase(), day.name()));
        return replies;
    }

    public List<QuickReplies> createHourQuickReplies(String chooseDay, long id) {
        List<Hairdresser> hairdressers = hairdresserService.findByDayHairCut(chooseDay);
        List<LocalTime> listHour = new ArrayList<>();
        List<QuickReplies> replies = new ArrayList<>();
        for (Hairdresser h : hairdressers) listHour.add(LocalTime.from(h.getDateHairCut()));

        for (Hour hour : Hour.values()) {
            if (!listHour.contains(LocalTime.parse(hour.getTime())))
                replies.add(new QuickReplies(hour.getTime(), hour.getTime().substring(0, 2)));
        }
        if (isFreeTimeToDoHairCut(replies)) return replies;
        else {
            facebookService.sendText(id, "Sorry,On " + dayCut.toLowerCase() + " is not free seats.");
            facebookService.sendQuickDayReplies(id);
            userService.updateState(id, State.DAYQUICKREPLIES);
            return null;
        }
    }

    @Override
    public void processMessage(MessageFromFacebook messageFromFacebook) {
        long id = Long.parseLong(messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getSender().getId());
        facebookService.createUserIfNoInDB(id);
        try {
            switch (userService.findState(id)) {
                case TEXT:
                    processMessage(id);
                    break;

                case BUTTON:
                    processButton(id, messageFromFacebook);
                    break;

                case DAYQUICKREPLIES:
                    processDayQuickReplies(id, messageFromFacebook);
                    break;

                case HOURQUICKREPLIES:
                    processHourQuickReplies(id, messageFromFacebook);
                    break;
            }
        } catch (HttpClientErrorException ex) {
            System.out.println("HttpClientErrorException " + ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
