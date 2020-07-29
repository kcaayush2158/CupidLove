package com.application.springboot.model.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userSender;
    private String userReceiver;
    @Temporal(TemporalType.TIME)
    private Date datetime_added;
    private String message;
    private boolean status;

}
