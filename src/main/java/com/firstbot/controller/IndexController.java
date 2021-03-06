package com.firstbot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.firstbot.model.in.MessageFromFacebook;
import com.firstbot.processor.MessagesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
public class IndexController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MessagesProcessor messagesProcessor;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexGet(@RequestParam("hub.challenge") String verifyToken) {
        //перевіка VERIFY_TOKEN
        return verifyToken;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void indexPost(@RequestBody MessageFromFacebook messageFromFacebook) throws JsonProcessingException {
            messagesProcessor.processMessage(messageFromFacebook);
    }
}
