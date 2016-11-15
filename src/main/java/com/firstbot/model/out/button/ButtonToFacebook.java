package com.firstbot.model.out.button;

import com.firstbot.model.Recipient;

import java.util.ArrayList;
import java.util.List;

public class ButtonToFacebook {

	private Recipient recipient;
	private Message message;

	public ButtonToFacebook(){

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

	public static ButtonToFacebook firstButtonToFacebook(long id){
		List<Button> buttonList = new ArrayList<>();
		List<Elements> elements =  new ArrayList<>();

		Button btn1 = new Button("hair cut","HAIR CUT");
		Button btn2 = new Button("bear cut","BEAR CUT");
		Button btn3 = new Button("cut","CUT");

		buttonList.add(btn1);
		buttonList.add(btn2);
		buttonList.add(btn3);
		Payload payload = new Payload("button","Choose your style: ",buttonList);
		Attachment attachment = new Attachment("template",payload);
		return new ButtonToFacebook(new Recipient(String.valueOf(id)),new Message(attachment));
	}

}
