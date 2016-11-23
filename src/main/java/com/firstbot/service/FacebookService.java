package com.firstbot.service;

import com.firstbot.model.out.button.Button;
import com.firstbot.model.out.quickreplies.QuickReplies;

import java.util.List;

public interface FacebookService {

    void sendButton(long id, List<Button> buttons);

    void sendText(long id, String text);

    void sendQuickReplies(long id, String text, List<QuickReplies> quickReplies);
}
