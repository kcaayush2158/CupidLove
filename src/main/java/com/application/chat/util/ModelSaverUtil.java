package com.application.chat.util;

import com.application.chat.model.Conversation;
import com.application.chat.model.PrivateChatMessage;
import com.application.springboot.model.User;

import java.net.InetAddress;
import java.util.Date;

public class ModelSaverUtil {

    public Conversation saveChatRoom(String message, boolean status, InetAddress ipAddress, User sender, User receiverUser) {
        Conversation conversation = new Conversation();
        conversation.setMessage(message);
        conversation.setStatus(status);
        conversation.setSender(sender);
        conversation.setDate(new Date());
        conversation.setIp(ipAddress.getHostAddress());
        conversation.setReceiver(receiverUser);
        return conversation;
    }

    public User saveUser() {
        User user = new User();

        return user;
    }

    public PrivateChatMessage savePrivateChatMessage(boolean status, InetAddress ipAddress, Conversation conversation) {
        PrivateChatMessage privateChatMessage = new PrivateChatMessage();
        privateChatMessage.setStatus(false);
        privateChatMessage.setIp(ipAddress.getHostAddress());
        privateChatMessage.setConversation(conversation);
        return privateChatMessage;
    }


}
