package com.firstbot.model.out.simpleMessage;

import lombok.Data;

@Data
public class SimpleMessage {
	private Recipient recipient;
	private Message message;

	public SimpleMessage(){

	}

	public SimpleMessage(Recipient recipient, Message message) {
		this.recipient = recipient;
		this.message = message;
	}
}
