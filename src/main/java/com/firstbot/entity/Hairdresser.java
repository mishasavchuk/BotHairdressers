package com.firstbot.entity;

import com.firstbot.constant.Day;
import com.firstbot.converter.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity

public class Hairdresser {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "type_hair_cut")
    private String typeHairCut;
    @Column(name = "day_hair_cut")
    @Enumerated(EnumType.STRING)
    private Day dayHairCut;
    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "date_hair_cut")
    private LocalDateTime dateHairCut;
    @Column
    private Boolean reminder;


    public Hairdresser() {

    }

    public Hairdresser(User user, String typeHairCut, Day dayHairCut, LocalDateTime dateHairCut, boolean reminder) {
        this.user = user;
        this.typeHairCut = typeHairCut;
        this.dayHairCut = dayHairCut;
        this.dateHairCut = dateHairCut;
        this.reminder = reminder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTypeHairCut() {
        return typeHairCut;
    }

    public void setTypeHairCut(String typeHairCut) {
        this.typeHairCut = typeHairCut;
    }

    public Day getDayHairCut() {
        return dayHairCut;
    }

    public void setDayHairCut(Day dayHairCut) {
        this.dayHairCut = dayHairCut;
    }

    public LocalDateTime getDateHairCut() {
        return dateHairCut;
    }

    public void setDateHairCut(LocalDateTime dateHairCut) {
        this.dateHairCut = dateHairCut;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }

}
