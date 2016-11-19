package com.firstbot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.firstbot.constant.State;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    private int userId;
    @Column(name = "firs_name")
    @JsonProperty("first_name")
    private String firstName;
    @Column(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;
    @Column(name = "gender")
    @JsonProperty("gender")
    private String gender;
    @Column
    private long idFacebook;
    @Column
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "id",fetch = FetchType.EAGER)
    List<Hairdresser> hairdressers = new ArrayList<>();

    public User() {

    }

    public User(long idFacebook, String firstName, String lastName, String gender, State state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.idFacebook = idFacebook;
        this.state = state;
    }
}
