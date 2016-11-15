package com.firstbot.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class UserProfile {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "firs_name")
    private String firstName;
    @Column(name = "last_name")
    private String secondName;
    @Column
    private String gender;

    public UserProfile() {

    }
    
    public UserProfile(String firstName, String secondName, String gender) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.gender = gender;
    }
}
