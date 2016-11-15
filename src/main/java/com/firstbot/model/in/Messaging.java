package com.firstbot.model.in;

import lombok.Data;

@Data
public class Messaging {
    private Sender sender;
    private Recipient recipient;
    private long timestamp;
    private Message message;
    private Read read;
    private Postback postback;

}
