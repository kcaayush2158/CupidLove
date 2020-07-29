package com.application.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AboutMe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String liveIn;
    private String workAs;
    private String education;
    private String haveKids;
    private String known;
    private String lookingFor;
    private String smoke;
    private String drink;
    private String height;
    private String bodyType;
    private String eyes;
    private String hair;
    private String relationship;
    private String languages;
    private String gender;
    private int age;
    private String bio;
    private String country;


}
