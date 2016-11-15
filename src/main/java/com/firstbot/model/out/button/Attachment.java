package com.firstbot.model.out.button;

import lombok.Data;

@Data
public class Attachment {
	private String type;
	private Payload payload;

	public Attachment(){

	}

	public Attachment(String type, Payload payload) {
		this.type = type;
		this.payload = payload;
	}
}
