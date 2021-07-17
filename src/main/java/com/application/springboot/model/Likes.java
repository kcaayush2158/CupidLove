package com.application.springboot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@Getter
@Setter
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private User likedBy;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private User likedTo;

    private boolean status;

}
