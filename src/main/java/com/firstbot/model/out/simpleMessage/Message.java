package com.firstbot.model.out.simpleMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Message {
    @JsonProperty("text")
    String text;

     Message(){

     }
     public Message(String text) {
         this.text = text;
     }
}
