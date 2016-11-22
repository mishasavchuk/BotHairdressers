package com.firstbot.model.out.button;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Payload {
    @JsonProperty("template_type")
    private String templateType;
    private String text;
    private List<Button> buttons;

    public Payload() {

    }

    public Payload(String templateType, String text, List<Button> buttons) {
        this.templateType = templateType;
        this.text = text;
        this.buttons = buttons;
    }
}
