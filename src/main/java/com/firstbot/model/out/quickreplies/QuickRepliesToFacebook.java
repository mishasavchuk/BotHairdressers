package com.firstbot.model.out.quickreplies;

import com.firstbot.model.Recipient;

import lombok.Data;

@Data
public class QuickRepliesToFacebook {
    private Recipient recipient;
    private Message message;

    public QuickRepliesToFacebook() {

    }

    public QuickRepliesToFacebook(Recipient recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }

}
