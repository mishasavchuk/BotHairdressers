package com.firstbot.model.out.simplemessage;

import com.firstbot.model.Recipient;
import lombok.Data;

@Data
public class SimpleMessageToFacebook {
    private Recipient recipient;
    private Message message;

    public SimpleMessageToFacebook() {

    }

    public SimpleMessageToFacebook(Recipient recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }
}
