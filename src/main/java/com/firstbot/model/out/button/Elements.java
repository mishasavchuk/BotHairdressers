package com.firstbot.model.out.button;

import lombok.Data;

import java.util.List;

@Data
public class Elements {
    private List<Button> buttons;

    public Elements() {

    }

    public Elements(List<Button> buttons) {
        this.buttons = buttons;
    }

}
