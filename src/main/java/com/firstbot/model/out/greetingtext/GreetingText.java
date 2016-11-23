package com.firstbot.model.out.greetingtext;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firstbot.constant.FacebookConstants;
import lombok.Data;

@Data
public class GreetingText {
    @JsonProperty("setting_type")
    private String settingType;
    private Greeting greeting;

    public static GreetingText greetingText() {
        GreetingText greetingText = new GreetingText();
        greetingText.setSettingType("greeting");
        Greeting greeting = new Greeting();
        greeting.setText(FacebookConstants.GREETING_TEXT);
        greetingText.setGreeting(greeting);
        return greetingText;
    }
}
