package com.application.springboot.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String topicName;
    private String topicQuestion;
    private String topicAnswer;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private User user;


}
