package com.firstbot.constant;

public enum Hour {
    TEN("10:00"),
    ELEVEN("11:00"),
    TWELVE("12:00"),
    THIRTEEN("13:00"),
    FOURTEEN("14:00"),
    FIFTEEN("15:00"),
    SIXTEEN("16:00"),
    SEVENTEEN("17:00");

    private String time;

    private Hour(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
