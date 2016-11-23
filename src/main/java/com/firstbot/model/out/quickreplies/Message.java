package com.firstbot.model.out.quickreplies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Message {
    private String text;
    @JsonProperty("quick_replies")
    private List<QuickReplies> quickRepliesList;

    public Message(String text, List<QuickReplies> replies) {
        this.text = text;
        this.quickRepliesList = replies;
    }
}
