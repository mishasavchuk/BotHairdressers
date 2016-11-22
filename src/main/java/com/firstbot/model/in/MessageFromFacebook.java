package com.firstbot.model.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MessageFromFacebook {
    private String object;
    @JsonProperty("entry")
    private List<Entry> entryList;

}
