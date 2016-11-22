package com.firstbot.model.out.greetingtext;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GreetingText {
    @JsonProperty("setting_type")
    String settingType;
    Greeting greeting;

    public static GreetingText greetingText() {
        GreetingText greetingText = new GreetingText();
        greetingText.setSettingType("greeting");
        Greeting greeting = new Greeting();
        greeting.setText("Welcome {{user_first_name}} {{user_last_name}} to the hairdresser,TEST");
        greetingText.setGreeting(greeting);
        return greetingText;
    }
}
