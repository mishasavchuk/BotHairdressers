package com.firstbot.model.out.button;

import lombok.Data;

import java.util.List;
@Data
public class Elements {
    List<Button> buttons;

    public Elements(){

    }

    public Elements(List<Button> buttons){
        this.buttons = buttons;
    }

}
