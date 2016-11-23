package com.firstbot.processor.impl;

import com.firstbot.constant.Day;
import com.firstbot.constant.FacebookConstants;
import com.firstbot.constant.State;
import com.firstbot.entity.Hairdresser;
import com.firstbot.model.in.MessageFromFacebook;
import com.firstbot.model.in.Postback;
import com.firstbot.processor.MessagesProcessor;
import com.firstbot.service.FacebookService;
import com.firstbot.service.HairdresserService;
import com.firstbot.service.MessageBuilderService;
import com.firstbot.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@Service
@Data
public class MessagesProcessorImpl implements MessagesProcessor {

    private final HairdresserService hairdresserService;

    private final UserService userService;

    private final FacebookService facebookService;

    private final MessageBuilderService messageBuilderService;

    private String typeCut;

    private String dayCut;

    private String timeCut;

    @Autowired
    public MessagesProcessorImpl(HairdresserService hairdresserService, UserService userService, FacebookService facebookService, MessageBuilderService messageBuilderService) {
        this.hairdresserService = hairdresserService;
        this.userService = userService;
        this.facebookService = facebookService;
        this.messageBuilderService = messageBuilderService;
    }

    private LocalDateTime cutTime(String dayHairCut, String timeCut) {
        LocalDateTime date = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(dayHairCut)));
        LocalDateTime recordDate = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), Integer.parseInt(timeCut), 00);
        return recordDate;
    }

    private boolean isPressButton(MessageFromFacebook messageFromFacebook) {
        Postback postback = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback();
        return postback != null && postback.getPayload() != null;
    }

    private boolean isPressQuickReplies(MessageFromFacebook messageFromFacebook) {
        com.firstbot.model.in.Message message = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage();
        return message != null && message.getQuickReply() != null && message.getQuickReply().getPayload() != null;
    }

    private String pressQuickReplies(MessageFromFacebook messageFromFacebook) {
        return messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply().getPayload();
    }

    private void processMessage(long id) {
        facebookService.sendText(id, "Welcome to hairdresser, I help you record on hair cut ");
        facebookService.sendButton(id, messageBuilderService.createButtons());
        userService.updateState(id, State.BUTTON);
    }

    private void processButton(long id, MessageFromFacebook messageFromFacebook) {
        if (isPressButton(messageFromFacebook)) {
            typeCut = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback().getPayload();
            facebookService.sendQuickReplies(id, "hello", messageBuilderService.createDayQuickReplies());
            userService.updateState(id, State.DAYQUICKREPLIES);
        } else {
            facebookService.sendButton(id, messageBuilderService.createButtons());
        }
    }

    private void processDayQuickReplies(long id, MessageFromFacebook messageFromFacebook) {
        if (isPressQuickReplies(messageFromFacebook)) {
            dayCut = pressQuickReplies(messageFromFacebook);
            userService.updateState(id, State.HOURQUICKREPLIES);
            List<Hairdresser> hairdresserList = hairdresserService.findByDayHairCut(Day.valueOf(dayCut));
            facebookService.sendQuickReplies(id, FacebookConstants.CHOOSE_HOUR, messageBuilderService.createHourQuickReplies(hairdresserList, id));
        } else {
            facebookService.sendQuickReplies(id, FacebookConstants.CHOOSE_DAY, messageBuilderService.createDayQuickReplies());
        }
    }

    private void processHourQuickReplies(long id, MessageFromFacebook messageFromFacebook) {
        if (isPressQuickReplies(messageFromFacebook)) {
            timeCut = pressQuickReplies(messageFromFacebook);
            userService.updateState(id, State.TEXT);
            facebookService.sendText(id, "Wait for you on " + dayCut + " " + timeCut + ":00");
            hairdresserService.addPerson(id, userService.findUserByFacebookId(id), Day.valueOf(dayCut), typeCut, cutTime(dayCut, timeCut), true);
        } else {
            facebookService.sendQuickReplies(id, FacebookConstants.CHOOSE_HOUR, messageBuilderService.createHourQuickReplies(hairdresserService.findByDayHairCut(Day.valueOf(dayCut)), id));
        }
    }

    private long getId(MessageFromFacebook messageFromFacebook) {
        return Long.parseLong(messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getSender().getId());
    }

    @Override
    public void processMessage(MessageFromFacebook messageFromFacebook) {
        long id = getId(messageFromFacebook);
        userService.createUserIfNotExist(id);
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
            throw new RuntimeException("An error occurred when trying to process message from Facebook", ex);
        }
    }
}
