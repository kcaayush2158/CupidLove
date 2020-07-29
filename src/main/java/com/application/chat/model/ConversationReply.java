package com.application.chat.model;

import com.application.springboot.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationReply {
    @Id
    private int id;
    private String reply;
    @ManyToOne
    private User senderId;
    private String ipAddress;
    private Date time;
    @ManyToOne
    private Conversation conversationId;

}
