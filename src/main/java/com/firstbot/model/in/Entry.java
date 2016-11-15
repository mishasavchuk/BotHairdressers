package com.firstbot.model.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Entry {
    private String id;
    private long time;
    @JsonProperty("messaging")
    private List<Messaging> messagingList;


}
