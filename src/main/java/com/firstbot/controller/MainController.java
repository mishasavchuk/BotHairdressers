package com.firstbot.controller;

import com.firstbot.constant.FacebookConstants;
import com.firstbot.model.UserProfile;
import com.firstbot.model.in.MessageFromFacebook;
import com.firstbot.processor.impl.MessagesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@RestController
public class MainController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MessagesProcessor messagesProcessor;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexGet(@RequestParam("hub.challenge") String str ){
        //перевіка VERIFY_TOKEN
        return str;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void indexPost(@RequestBody(required = false)MessageFromFacebook messageFromFacebook /*String jsonFromFacebook*/) throws IOException {
        messagesProcessor.processMessage(messageFromFacebook);
    }
}
