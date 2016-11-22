package com.firstbot.model.out.startedbutton;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class StartedMessage {
    @JsonProperty("setting_type")
    private String settingsType;
    @JsonProperty("thread_state")
    private String threadState;
    @JsonProperty("call_to_actions")
    private List<Action> callToActions;

    public static StartedMessage createStartMessage() {
        StartedMessage request = new StartedMessage();
        request.setSettingsType("call_to_actions");
        request.setThreadState("new_thread");
        Action action = new Action();
        action.setPayload("USER_DEFINED_PAYLOAD");
        request.setCallToActions(Arrays.asList(action));
        return request;
    }
}
