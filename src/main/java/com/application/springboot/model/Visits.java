package com.application.springboot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Getter
@Setter
public class Visits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @NotNull
    private User visitedUser;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @NotNull
    private User receivedUser;
    @NotNull
    private boolean status;

}
