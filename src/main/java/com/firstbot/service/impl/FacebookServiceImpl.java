package com.firstbot.service.impl;

import com.firstbot.model.Recipient;
import com.firstbot.model.out.button.Button;
import com.firstbot.model.out.button.ButtonToFacebook;
import com.firstbot.model.out.quickreplies.Message;
import com.firstbot.model.out.quickreplies.QuickReplies;
import com.firstbot.model.out.quickreplies.QuickRepliesToFacebook;
import com.firstbot.model.out.simplemessage.SimpleMessageToFacebook;
import com.firstbot.service.FacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.firstbot.constant.FacebookConstants.ACCESS_TOKEN;
import static com.firstbot.constant.FacebookConstants.FACEBOOK_POST_URL;
import static com.firstbot.model.out.button.ButtonToFacebook.buttonToFacebook;

@Service
public class FacebookServiceImpl implements FacebookService {
    private final RestTemplate restTemplate;

    @Autowired
    public FacebookServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendButton(long id, List<Button> buttons) {
        restTemplate.postForObject(FACEBOOK_POST_URL + ACCESS_TOKEN, buttonToFacebook(id, buttons), ButtonToFacebook.class);
    }

    @Override
    public void sendQuickReplies(long id, String text, List<QuickReplies> quickReplies) {
        restTemplate.postForObject(FACEBOOK_POST_URL + ACCESS_TOKEN, new QuickRepliesToFacebook(new Recipient(String.valueOf(id)), new Message(text, quickReplies)), QuickRepliesToFacebook.class);
    }

    @Override
    public void sendText(long id, String text) {
        restTemplate.postForObject(
                FACEBOOK_POST_URL + ACCESS_TOKEN,
                new SimpleMessageToFacebook(new Recipient(String.valueOf(id)), new com.firstbot.model.out.simplemessage.Message(text)), SimpleMessageToFacebook.class
        );
    }
}
