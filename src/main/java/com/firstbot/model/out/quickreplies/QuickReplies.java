package com.firstbot.model.out.quickreplies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QuickReplies {
    @JsonProperty("content_type")
    private String contentType;
    private String title;
    private String payload;

    public QuickReplies() {

    }

    public QuickReplies(String contentType, String title, String payload) {
        this.contentType = contentType;
        this.title = title;
        this.payload = payload;
    }

    public QuickReplies(String title, String payload) {
        this.contentType = "text";
        this.title = title;
        this.payload = payload;
    }
}
