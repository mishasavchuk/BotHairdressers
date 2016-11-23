package com.firstbot.service;

import com.firstbot.entity.Hairdresser;
import com.firstbot.model.out.button.Button;
import com.firstbot.model.out.quickreplies.QuickReplies;

import java.util.List;

public interface MessageBuilderService {
    public List<Button> createButtons();

    public List<QuickReplies> createDayQuickReplies();

    public List<QuickReplies> createHourQuickReplies(List<Hairdresser> hairdressersList, long id);

    public boolean isFreeTimeToDoHairCut(List<QuickReplies> quickReplies);
}
