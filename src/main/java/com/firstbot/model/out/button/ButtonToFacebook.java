package com.firstbot.model.out.button;

import com.firstbot.model.Recipient;

import java.util.ArrayList;
import java.util.List;

public class ButtonToFacebook {

    private Recipient recipient;
    private Message message;

    public ButtonToFacebook() {

    }

    public ButtonToFacebook(Recipient recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static ButtonToFacebook buttonToFacebook(long id) {
        List<Button> buttonList = new ArrayList<>();
        List<Elements> elements = new ArrayList<>();

        Button btnHairCut = new Button("hair cut", "HAIR CUT");
        Button btnBearCut = new Button("bear cut", "BEAR CUT");
        Button btnHairBearCut = new Button("hair&bear cut", "HAIR&BEAR CUT");
        //Button btnCancel = new Button("Cancel hair cut","CANCEL HAIR CUT");

        buttonList.add(btnHairCut);
        buttonList.add(btnBearCut);
        buttonList.add(btnHairBearCut);
        //buttonList.add(btnCancel);

        Payload payload = new Payload("button", "Choose your style: ", buttonList);
        Attachment attachment = new Attachment("template", payload);
        return new ButtonToFacebook(new Recipient(String.valueOf(id)), new Message(attachment));
    }

}
