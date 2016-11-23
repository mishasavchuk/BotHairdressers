package com.firstbot.model.out.button;

import com.firstbot.model.Recipient;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.firstbot.constant.FacebookConstants.CHOOSE_STYLE;

@Data
public class ButtonToFacebook {
    private Recipient recipient;
    private Message message;

    public ButtonToFacebook() {

    }

    public ButtonToFacebook(Recipient recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }

    public static ButtonToFacebook buttonToFacebook(long id, List<Button> buttons) {
        List<Elements> elements = new ArrayList<>();
        Payload payload = new Payload("button", CHOOSE_STYLE, buttons);
        Attachment attachment = new Attachment("template", payload);
        return new ButtonToFacebook(new Recipient(String.valueOf(id)), new Message(attachment));
    }

}
