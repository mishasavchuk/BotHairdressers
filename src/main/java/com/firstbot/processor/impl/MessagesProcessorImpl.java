package com.firstbot.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstbot.constant.Day;
import com.firstbot.constant.FacebookConstants;
import com.firstbot.constant.Hour;
import com.firstbot.constant.State;
import com.firstbot.entity.Hairdresser;
import com.firstbot.entity.User;
import com.firstbot.model.in.MessageFromFacebook;
import com.firstbot.model.out.button.ButtonToFacebook;
import com.firstbot.model.out.quickreplies.QuickReplies;
import com.firstbot.model.out.quickreplies.QuickRepliesToFacebook;
import com.firstbot.model.out.simpleMessage.Message;
import com.firstbot.model.out.simpleMessage.Recipient;
import com.firstbot.model.out.simpleMessage.SimpleMessage;
import com.firstbot.processor.MessagesProcessor;
import com.firstbot.service.HairdresserServiceImpl;
import com.firstbot.service.UserProfileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


@Service
public class MessagesProcessorImpl implements MessagesProcessor {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    HairdresserServiceImpl hairdresserService;
    @Autowired
    UserProfileServiceImpl userProfileService;

    private String typeCut;
    private String dayCut;
    private String timeCut;

    {
        typeCut = dayCut = typeCut = null;
    }

    public void sendText(long id, String text) {
        try {
            restTemplate.postForObject(FacebookConstants.FACEBOOK_POST_URL + FacebookConstants.ACCESS_TOKEN,new SimpleMessage(new Recipient(id),new Message(text)),SimpleMessage.class);
        }
        catch (HttpClientErrorException ex){
            System.err.println("HttpClientErrorException: "+ex.getResponseBodyAsString());
        }
        catch (Exception ex){
            System.err.println("Can not send simple text to messenger");
        }
    }
    public void sendButton(long id){
        try {
            restTemplate.postForObject(FacebookConstants.FACEBOOK_POST_URL + FacebookConstants.ACCESS_TOKEN, ButtonToFacebook.ButtonToFacebook(id), ButtonToFacebook.class);
        }
        catch (HttpClientErrorException ex){
            System.err.println("HttpClientErrorException: "+ex.getResponseBodyAsString());
        }
        catch (Exception ex){
            System.err.println("Can not send button to messenger");
        }
    }
    public void sendQuickDayReplies(long id){
        String text = "Choose day when you wont to do haircut: ";
        try {
            restTemplate.postForObject(FacebookConstants.FACEBOOK_POST_URL + FacebookConstants.ACCESS_TOKEN, new QuickRepliesToFacebook(new com.firstbot.model.Recipient(String.valueOf(id)),new com.firstbot.model.out.quickreplies.Message(text, createDayQuickReplies())), QuickRepliesToFacebook.class);
        }
        catch (HttpClientErrorException ex){
            System.err.println("HttpClientErrorException: "+ex.getResponseBodyAsString());
        }
        catch (Exception ex){
            System.err.println("Can not send quickDayReplies to messenger");
        }
    }
    public void sendQuickHourReplies(long id){
       try {
           List<QuickReplies> hourQuickReplies = createHourQuickReplies(dayCut,id);
           if (hourQuickReplies != null) {
               String text = "Choose hour when you wont to do haircut: ";
               restTemplate.postForObject(FacebookConstants.FACEBOOK_POST_URL + FacebookConstants.ACCESS_TOKEN, new QuickRepliesToFacebook(
                       new com.firstbot.model.Recipient(String.valueOf(id)), new com.firstbot.model.out.quickreplies.Message(text, hourQuickReplies)), QuickRepliesToFacebook.class);
           }
       }
       catch(HttpClientErrorException ex){
           System.err.println("HttpClientErrorException: " + ex.getResponseBodyAsString());
       }
       catch(Exception ex){
           System.err.println("Can not send quickHourReplies to messenger");
       }
    }

    public List<QuickReplies> createDayQuickReplies(){
        List<QuickReplies> replies = new ArrayList<>(Day.values().length);
        for (Day day: Day.values()) replies.add(new QuickReplies("text", day.name().toLowerCase(),day.name()));
        System.out.println(replies);
        return replies;
    }
    public List<QuickReplies> createHourQuickReplies(String chooseDay, long id){
        List<Hairdresser> hairdressers = hairdresserService.findByDayHairCut(chooseDay);
        List<LocalTime> listHour = new ArrayList<>();
        List<QuickReplies> replies = new ArrayList<>();
        for(Hairdresser h: hairdressers) listHour.add(LocalTime.from(h.getDateHairCut()));

        for(Hour hour: Hour.values()){
            if(!listHour.contains(LocalTime.parse(hour.getTime()))) replies.add(new QuickReplies(hour.getTime(),hour.getTime().substring(0,2)));
        }
        if(isFreeTimeToDoHairCut(replies)) return replies;
        else {
            sendText(id,"Sorry,On "+dayCut.toLowerCase()+" is not free seats.");
            sendQuickDayReplies(id);
            userProfileService.updateState(id, State.DAYQUICKREPLIES);
            return null;
        }
    }
    public boolean isFreeTimeToDoHairCut(List<QuickReplies> quickReplies){
        System.out.println("quickReplies: "+quickReplies);
        if(quickReplies != null && quickReplies.size() != 0) return true;
        else return false;
    }
    public static  LocalDateTime cutTime(String dayHairCut, String timeCut){
        LocalDateTime date = LocalDateTime.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(dayHairCut)));
        LocalDateTime recordDate = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),Integer.parseInt(timeCut),00);
        return recordDate;
    }

    //cron = "*/30 * * * *"
    @Scheduled(fixedDelay = 5000)
    public void sendReminder(){
        List<Hairdresser> hairdressers = hairdresserService.findReminderDateHairCut();
        //System.out.println("hairdresser"+list.toString());
        for(Hairdresser h: hairdressers){
            if(h.isReminder()) {
                sendText(h.getUser().getIdFacebook(),"Do not forget come to hairdresser");
                hairdresserService.updateReminder(false,h.getUser(),h.getDateHairCut());
            }
        }
    }
    User getUserProfile(String userId) {
    try {
        String requestUrl = FacebookConstants.PROFILE_URL.replace("$user_id$", userId);
        return restTemplate.getForObject(requestUrl + FacebookConstants.ACCESS_TOKEN, User.class);
    } catch (HttpClientErrorException e) {
        System.out.println("Can not get profile info "+e);
    } catch (Exception e){
        System.out.println("Can not get profile info " + e);
    }
    return null;
}
    void createUserIfNoInDB(long id) {
        if (!userProfileService.findAllFacebookId().contains(id)) {
            User userProfile = getUserProfile(String.valueOf(id));
            userProfileService.addUserProfile(id, userProfile.getFirstName(), userProfile.getLastName(), userProfile.getGender(), State.TEXT);
        }
    }

    public boolean isPressButton(MessageFromFacebook messageFromFacebook){
        return messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback()!=null && messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback().getPayload()!=null;
    }
    public boolean isPressQuickReplies(MessageFromFacebook messageFromFacebook){
        return messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage()!=null && messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply()!=null && messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply().getPayload()!=null;
    }

    @Override
	public void processMessage(MessageFromFacebook messageFromFacebook){
        long id = Long.parseLong(messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getSender().getId());
        createUserIfNoInDB(id);
        try {
            System.out.println("TRY");
            if(State.TEXT.equals(userProfileService.findState(id))){
                System.out.println("TEXT");
                sendText(id, "Welcome to hairdresser, I help you record on hair cut ");
                sendButton(id);
                userProfileService.updateState(id,State.BUTTON);
            }
            else if(State.BUTTON.equals(userProfileService.findState(id))){
                if(isPressButton(messageFromFacebook)) {
                    typeCut = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback().getPayload();
                    sendQuickDayReplies(id);
                    userProfileService.updateState(id, State.DAYQUICKREPLIES);
                }
                else sendButton(id);
            }
            else if(State.DAYQUICKREPLIES.equals(userProfileService.findState(id))){
                if(isPressQuickReplies(messageFromFacebook)){
                    dayCut = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply().getPayload();
                    userProfileService.updateState(id, State.HOURQUICKREPLIES);
                    sendQuickHourReplies(id);
                }
                else sendQuickDayReplies(id);
            }
            else if(State.HOURQUICKREPLIES.equals(userProfileService.findState(id))){
                if(isPressQuickReplies(messageFromFacebook)){
                    timeCut = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply().getPayload();
                    userProfileService.updateState(id, State.TEXT);
                    sendText(id,"Waite for you on "+dayCut.toLowerCase()+" "+timeCut+":00");
                    hairdresserService.addPerson(id,userProfileService.findUserByFacebookId(id),dayCut, typeCut, cutTime(dayCut,timeCut), true);
                }
                else sendQuickHourReplies(id);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}