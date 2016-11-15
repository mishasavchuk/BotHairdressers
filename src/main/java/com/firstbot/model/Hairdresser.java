package com.firstbot.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Hairdresser {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private long idFacebook;
    @Column(name = "type_hair_cut")
    private String typeHairCut;
    @Column(name = "day_hair_cut")
    private String dayHairCut;
   /* @Column(name = "time_hair_cut")
    //private String timeHairCut;
    @Convert(converter = TimeConverter.class)
    private LocalDateTime timeHairCut;*/
    @Convert(converter = TimeConverter.class)
    @Column(name = "date_hair_cut")
    private LocalDateTime dateHairCut;
    @Column
    private boolean reminder;

    public Hairdresser(){

    }

    public Hairdresser(long idFacebook, String typeHairCut, String dayHairCut, LocalDateTime dateHairCut, boolean reminder) {
        this.idFacebook = idFacebook;
        this.typeHairCut = typeHairCut;
        this.dayHairCut = dayHairCut;
        this.dateHairCut = dateHairCut;
        this.reminder = reminder;
    }
}
