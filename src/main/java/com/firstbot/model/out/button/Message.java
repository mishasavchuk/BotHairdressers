package com.firstbot.model.out.button;

import lombok.Data;

@Data
public class Message {
    private Attachment attachment;

    public Message() {

    }

    public Message(Attachment attachment) {
        this.attachment = attachment;
    }
}
