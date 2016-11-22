package com.firstbot.service;

import com.firstbot.constant.State;
import com.firstbot.entity.Hairdresser;
import com.firstbot.entity.User;
import com.firstbot.model.out.button.ButtonToFacebook;
import com.firstbot.model.out.greetingtext.GreetingText;
import com.firstbot.model.out.quickreplies.QuickReplies;
import com.firstbot.model.out.quickreplies.QuickRepliesToFacebook;
import com.firstbot.model.out.simplemessage.Message;
import com.firstbot.model.out.simplemessage.Recipient;
import com.firstbot.model.out.simplemessage.SimpleMessageToFacebook;
import com.firstbot.model.out.startedbutton.StartedMessage;
import com.firstbot.processor.impl.MessagesProcessorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static com.firstbot.constant.FacebookConstants.*;

@Service
public class FacebookService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private HairdresserService hairdresserService;
    @Autowired
    private MessagesProcessorImpl messagesProcessor;

    public void sendText(long id, String text) {
        restTemplate.postForObject(FACEBOOK_POST_URL + ACCESS_TOKEN, new SimpleMessageToFacebook(new Recipient(id), new Message(text)), SimpleMessageToFacebook.class);
    }

    public void sendButton(long id) {
        try {
            restTemplate.postForObject(FACEBOOK_POST_URL + ACCESS_TOKEN, ButtonToFacebook.buttonToFacebook(id), ButtonToFacebook.class);
        } catch (HttpClientErrorException ex) {
            System.err.println("HttpClientErrorException: " + ex.getResponseBodyAsString());
        } catch (Exception ex) {
            System.err.println("Can not send button to messenger");
        }
    }

    public void sendQuickDayReplies(long id) {
        String text = "Choose day when you wont to do haircut: ";
        restTemplate.postForObject(FACEBOOK_POST_URL + ACCESS_TOKEN, new QuickRepliesToFacebook(new com.firstbot.model.Recipient(String.valueOf(id)), new com.firstbot.model.out.quickreplies.Message(text, messagesProcessor.createDayQuickReplies())), QuickRepliesToFacebook.class);
    }

    public void sendQuickHourReplies(long id) {
        List<QuickReplies> hourQuickReplies = messagesProcessor.createHourQuickReplies(messagesProcessor.getDayCut(), id);
        if (hourQuickReplies != null) {
            String text = "Choose hour when you wont to do haircut: ";
            restTemplate.postForObject(FACEBOOK_POST_URL + ACCESS_TOKEN, new QuickRepliesToFacebook(
                    new com.firstbot.model.Recipient(String.valueOf(id)), new com.firstbot.model.out.quickreplies.Message(text, hourQuickReplies)), QuickRepliesToFacebook.class);
            }
    }

    public void sendStartedButton() {
            StartedMessage startedMessage = StartedMessage.createStartMessage();
            restTemplate.postForObject(GREETING_URL + ACCESS_TOKEN, startedMessage, StartedMessage.class);
    }

    public void sendGreetingText() {
            System.out.println("GREETING");
            GreetingText greetingText = GreetingText.greetingText();
            System.out.println(greetingText);
            restTemplate.postForObject(GREETING_URL + ACCESS_TOKEN, greetingText, GreetingText.class);
    }

    Optional<User> getUser(String userId) {
            String requestUrl = PROFILE_URL.replace("$user_id$", userId);
        return Optional.ofNullable(restTemplate.getForObject(requestUrl + ACCESS_TOKEN, User.class));
    }

    public void createUserIfNoInDB(long id) {
        if (!userService.findAllUser().contains(id)) {
            Optional<User> user = getUser(String.valueOf(id));
            userService.addUserProfile(id, user.get().getFirstName(), user.get().getLastName(), user.get().getGender(), State.TEXT);
            } else {
                System.out.println("Can not create user");
        }
    }
    //cron = "*/30 * * * *"
    @Scheduled(fixedDelay = 5000)
    public void sendReminder() {
        List<Hairdresser> hairdressers = hairdresserService.findReminderDateHairCut();
        for (Hairdresser h : hairdressers) {
            if (h.isReminder()) {
                sendText(h.getUser().getIdFacebook(), "Do not forget come to hairdresser today on " + h.getDateHairCut().getHour() + ":00");
                hairdresserService.updateReminder(false, h.getUser(), h.getDateHairCut());
            }
        }
    }
}
