package com.firstbot.processor.impl.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstbot.constant.Day;
import com.firstbot.constant.FacebookConstants;
import com.firstbot.model.Hairdresser;
import com.firstbot.model.UserProfile;
import com.firstbot.model.in.MessageFromFacebook;
import com.firstbot.model.out.button.ButtonToFacebook;
import com.firstbot.model.out.quickreplies.QuickReplies;
import com.firstbot.model.out.quickreplies.QuickRepliesToFacebook;
import com.firstbot.model.out.simpleMessage.Message;
import com.firstbot.model.out.simpleMessage.Recipient;
import com.firstbot.model.out.simpleMessage.SimpleMessage;
import com.firstbot.processor.impl.MessagesProcessor;
import com.firstbot.service.impl.FacebookUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class MessagesProcessorImpl implements MessagesProcessor {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    FacebookUserServiceImpl facebookUserService;

    String typeCut;
    String dayCut;
    String timeCut;
    boolean flagB;
    boolean flagQD;
    boolean flagQT;
    boolean flagT;

    {
        typeCut = dayCut = typeCut = null;
        flagT = flagB = true;
        flagQD = flagQT = false;
    }

	public void sendText(long id, String text) throws JsonProcessingException {
		//headers.add("Content-Type", "application/json");
		try {
			restTemplate.postForObject(FacebookConstants.FACEBOOK_POST_URL + FacebookConstants.ACCESS_TOKEN,new SimpleMessage(new Recipient(id),new Message(text)),SimpleMessage.class);
		}
		catch (HttpClientErrorException ex){
			System.out.println(ex.getResponseBodyAsString());
		}
	}
	public void sendButton(long id) throws JsonProcessingException {
		//headers.add("Content-Type", "application/json");
		try {
			restTemplate.postForObject(FacebookConstants.FACEBOOK_POST_URL + FacebookConstants.ACCESS_TOKEN, ButtonToFacebook.firstButtonToFacebook(id), ButtonToFacebook.class);
		}
		catch (HttpClientErrorException ex){
			System.out.println(ex.getResponseBodyAsString());
		}
	}
    public void sendQuickDayReplies(long id) throws JsonProcessingException {
       // headers.add("Content-Type", "application/json");
        String text = "Choose day when you wont to do haircut: ";
        try {
            restTemplate.postForObject(FacebookConstants.FACEBOOK_POST_URL + FacebookConstants.ACCESS_TOKEN, new QuickRepliesToFacebook(new com.firstbot.model.Recipient(String.valueOf(id)),new com.firstbot.model.out.quickreplies.Message(text, madeDayQuickReplies())), QuickRepliesToFacebook.class);
        }
        catch (HttpClientErrorException ex){
            System.out.println(ex.getResponseBodyAsString());
        }
    }
    public void sendQuickHourReplies(long id) throws JsonProcessingException {
       // headers.add("Content-Type", "application/json");
        List<QuickReplies> list = madeHourQuickReplies(dayCut,id);
        System.out.println("isFreeTimeToDoHairCut(id,list): "+isFreeTimeToDoHairCut(id,list));
        if(list!=null) {
            String text = "Choose hour when you wont to do haircut: ";
            try {
                restTemplate.postForObject(FacebookConstants.FACEBOOK_POST_URL + FacebookConstants.ACCESS_TOKEN, new QuickRepliesToFacebook(
                        new com.firstbot.model.Recipient(String.valueOf(id)), new com.firstbot.model.out.quickreplies.Message(text, list)), QuickRepliesToFacebook.class);
            }
            catch (HttpClientErrorException ex){
                System.out.println(ex.getResponseBodyAsString());
            }
        }
        else {
            dayCut=null;
            sendQuickDayReplies(id);
        }

    }
    public List<QuickReplies> madeDayQuickReplies(){
        List<QuickReplies> replies = new ArrayList<>();
        QuickReplies monday = new QuickReplies("text",Day.Monday.name(),Day.Monday.name().toUpperCase());
        QuickReplies tuesday = new QuickReplies("text",Day.Tuesday.name(),Day.Tuesday.name().toUpperCase());
        QuickReplies wednesday = new QuickReplies("text",Day.Wednesday.name(),Day.Wednesday.name().toUpperCase());
        QuickReplies thursday = new QuickReplies("text",Day.Thursday.name(),Day.Thursday.name().toUpperCase());
        QuickReplies friday = new QuickReplies("text",Day.Friday.name(),Day.Friday.name().toUpperCase());

        replies.addAll(Arrays.asList(monday,tuesday,thursday,wednesday,thursday,friday));
        return replies;
    }
    public List<QuickReplies> madeHourQuickReplies(String chooseDay,long id) throws JsonProcessingException {

        List<Hairdresser> hairdresserList = facebookUserService.findByDayHairCut(chooseDay);

        List<LocalTime> listHour = new ArrayList<>();
        for(Hairdresser f: hairdresserList) listHour.add(LocalTime.from(f.getDateHairCut()));
        //System.out.println("listhour: "+listHour);
        List<QuickReplies> replies = new ArrayList<>();
        for(int i=10; i<17; i++){
            LocalTime localTime = LocalTime.parse(Integer.toString(i)+":00", DateTimeFormatter.ISO_LOCAL_TIME);
            System.out.println("localTime: "+localTime);
            System.out.println("listHour: "+listHour);
            //System.out.println(localTime);
            //System.out.println("listHour.contains: "+listHour.contains(localTime));
            if(!listHour.contains(localTime))
               replies.add(new QuickReplies(Integer.toString(i) + ":00", Integer.toString(i)));
        }
        if(isFreeTimeToDoHairCut(id,replies)) return replies;
        else return null;
    }
    public boolean isText(MessageFromFacebook messageFromFacebook){
        return messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage()!=null && messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getText()!=null && messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply()==null;
    }
    public boolean isButton(MessageFromFacebook messageFromFacebook){
        return messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback()!=null && messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback().getPayload()!=null;
    }
    public boolean isQuickReplies(MessageFromFacebook messageFromFacebook){
        return messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage()!=null && messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply()!=null && messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply().getPayload()!=null;
    }
    public boolean isFreeTimeToDoHairCut(long id, List<QuickReplies> quickReplies) throws JsonProcessingException {
        if(quickReplies!=null && quickReplies.size() == 0) {
            sendText(id,"Sorry,On "+dayCut.toLowerCase()+" is not free seats.");
            dayCut = null;
            return false;
        }
        else return true;
    }
	public static  LocalDateTime cutTime(String dayCut, String timeCut){
        LocalDateTime localDate = LocalDateTime.now();
        LocalDateTime date = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(dayCut.toUpperCase())));
        LocalDateTime ldt = LocalDateTime.of(date.getYear(),date.getMonth(),date.getDayOfMonth(),(int)Long.parseLong(timeCut),00);
        System.out.println(ldt);
        return ldt;
        //Long.parseLong(timeCut));
    }


    //cron = "*/30 * * * *"
    @Scheduled(fixedDelay = 5000)
    public void doSomething() throws JsonProcessingException {
        List<Hairdresser> hairdresserList = facebookUserService.findByDateHairCut();
        System.out.println(hairdresserList);
        for(Hairdresser f: hairdresserList){
            if(f.isReminder()) {
                sendText(f.getIdFacebook(),"Dont forget...");
                facebookUserService.updateReminder(false,f.getIdFacebook());
                //System.out.println(f.isReminder());
            }
        }
    }


    UserProfile getCustomerProfile(String userId) {
        try {
            String requestUrl = FacebookConstants.PROFILE_URL.replace("$user_id$", userId);
            return restTemplate.getForObject(requestUrl + FacebookConstants.ACCESS_TOKEN, UserProfile.class);
        } catch (HttpClientErrorException e) {
            System.out.println("Can not get profile info"+e);
        } catch (Exception e){
            System.out.println("Can not get profile info" + e);
        }
        return null;
    }


    @Override
	public void processMessage(MessageFromFacebook messageFromFacebook) throws JsonProcessingException {
        long id = Long.parseLong(messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getSender().getId());
        UserProfile userProfile = getCustomerProfile(String.valueOf(id));
        facebookUserService.addUserProfile("sf","dsf","fsdf");





        //String m = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getText();
        try {
            if(isText(messageFromFacebook)) {
                //System.out.println("flagT: "+ flagT + " flagB: "+flagB + " flagQT: " + flagQT + " flagQD: "+flagQD);
                if(flagT==true){
                    timeCut = dayCut = typeCut = null;
                    sendText(id, "Welcome to our hairdressers");
                    flagT = false;
                }
                if(flagB ==true) sendButton(id);
                if(flagQD==true) sendQuickDayReplies(id);
                if(flagQT==true) sendQuickHourReplies(id);
            }
            if(isButton(messageFromFacebook)) {
                typeCut = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getPostback().getPayload();
                sendText(id, "Okkkk, you choose " + typeCut.toLowerCase());
                sendQuickDayReplies(id);
                flagB=false;
                flagQD=true;
            }
            if(isQuickReplies(messageFromFacebook)) {
                if (dayCut!=null && timeCut == null){
                    timeCut = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply().getPayload();
                    sendText(id,"Okkk, you choose "+timeCut+":00");
                    sendText(id,"You record for a haircut on: "+dayCut.toLowerCase()+" at "+timeCut+": 00"+"\n"+"Wait for you...");
                    flagT = flagB =true;


                    facebookUserService.addPerson(id,dayCut,typeCut,cutTime(dayCut,timeCut),true);
                }
                else if(dayCut==null) {
                    dayCut = messageFromFacebook.getEntryList().get(0).getMessagingList().get(0).getMessage().getQuickReply().getPayload();
                    sendText(id,"Okkk, you choose "+dayCut.toLowerCase());
                    flagQD = false;
                    flagQT = true;
                    sendQuickHourReplies(id);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}