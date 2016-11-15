package com.firstbot.model.out.simpleMessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Recipient {
     @JsonProperty("id")
     long id;

     public Recipient(){

     }
     public Recipient(long id) {
         this.id = id;
     }

 }
