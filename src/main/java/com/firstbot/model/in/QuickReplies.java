package com.firstbot.model.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuickReplies {
    @JsonProperty("content_type")
    String contentType;
    String title;
    String payload;

    public QuickReplies() {

    }

    public QuickReplies(String contentType, String title, String payload) {
        this.contentType = contentType;
        this.title = title;
        this.payload = payload;
    }

}
