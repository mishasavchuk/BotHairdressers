package com.firstbot.model.out.button;

import lombok.Data;

@Data
public class Button {
    private String type;
    private String title;
    private String payload;

    public Button() {

    }

    public Button(String title, String payload) {
        this.title = title;
        this.payload = payload;
        this.setType("postback");
    }
}
