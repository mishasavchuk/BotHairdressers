package com.firstbot.constant;

public class FacebookConstants {
    public final static String FACEBOOK_POST_URL = "https://graph.facebook.com/v2.6/me/messages?access_token=";
    public final static String ACCESS_TOKEN = "EAAB4BNazWi0BAALW5FLulDGB8QL9Uk7Wr8u7qCXvX0VerjJk0ZCpuSltRIYogL6qO7JTJtGZCSps97FZCvO58kseQXU4ZBJZAqEEYgzNtiyqexY75dZA69pzWZBDpROIoCT9R3AnvM7XAnZCNIOH8T8RQEOQpTq2weeZASSxmoMrV3xHWxXlT8cMA";
    public final static String VERIFY_TOKEN = "verify";
    public final static String PROFILE_URL = "https://graph.facebook.com/v2.6/$user_id$?fields=first_name,last_name,gender,locale&access_token=";
    public final static String GREETING_URL = "https://graph.facebook.com/v2.6/me/thread_settings?access_token=";
    public final static String GREETING_TEXT = "Welcome {{user_first_name}} {{user_last_name}} to the hairdresser,TEST";
    public final static String CHOOSE_STYLE = "Choose your style: ";
    public final static String CHOOSE_DAY = "Choose day hair cut";
    public final static String CHOOSE_HOUR = "Choose hour hair cut";
    public final static String FORGET = "Do not forget come to hairdresser today on ";
}
