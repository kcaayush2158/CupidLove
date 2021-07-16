package com.application.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LookingFor {
    @Id
    @GeneratedValue
    private int id;
    private int fromAge;
    private int toAge;
    private String description;

}
