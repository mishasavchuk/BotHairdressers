package com.firstbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Recipient {
    @JsonProperty("id")
    private String id;

    public Recipient() {

    }

    public Recipient(String id) {
        this.id = id;
    }
}
