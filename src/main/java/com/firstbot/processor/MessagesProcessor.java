package com.firstbot.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.firstbot.model.in.MessageFromFacebook;
import org.springframework.scheduling.annotation.Async;

public interface MessagesProcessor {
    @Async
    void processMessage(MessageFromFacebook message) throws JsonProcessingException;
}
