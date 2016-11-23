package com.firstbot.model.out.simplemessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Recipient {
    @JsonProperty("id")
    private long id;

    public Recipient() {

    }

    public Recipient(long id) {
        this.id = id;
    }

}
