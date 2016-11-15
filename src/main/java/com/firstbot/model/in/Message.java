package com.firstbot.model.in;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class Message {
    @JsonProperty("is_echo")
    private Boolean isEcho;
    @JsonProperty("app_id")
    private BigDecimal appId;
    private String mid;
    @JsonProperty("sticker_id")
    private String stickerId;
    private BigDecimal seq;
    private String text;
    @JsonProperty("quick_reply")
    private QuickReplies quickReply;
    @JsonIgnore
    private Map<String, String> attachments;
}
