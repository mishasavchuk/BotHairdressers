package com.firstbot.constant;

public enum Gender {
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("unknown");

    private String gender;

    Gender(String male) {
        this.gender = male;
    }
}
