package com.application.chat.service;

import com.application.chat.model.PrivateChatMessage;
import com.application.chat.repository.PrivateChatMessageRepository;
import com.application.springboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivateMessageService {

    @Autowired
    private PrivateChatMessageRepository privateChatMessageRepository;


    public List<PrivateChatMessage> getAllMessagesBySender(User sender) {
        return privateChatMessageRepository.getDistinctByConversation_SenderOrderByIdDesc(sender);
    }

    public List<PrivateChatMessage> fetchPrivatesMessages(User sender, User receiver, String uuid) {
        return privateChatMessageRepository.fetchPrivateMessages(sender, receiver, uuid);
    }

    public int countUnreadMessages(User receiver, User sender, boolean status) {
        return privateChatMessageRepository.countPrivateChatMessagesByConversation_ReceiverAndConversation_SenderAndConversation_Status(receiver, sender, status);
    }

    public void deleteMessage(int id) {
        privateChatMessageRepository.deleteById(id);
    }

    public PrivateChatMessage save(PrivateChatMessage privateChatMessage) {
        return privateChatMessageRepository.save(privateChatMessage);
    }


    public List<PrivateChatMessage> getSenderAndReceiverMessages(User sender, User receiver) {
        return privateChatMessageRepository.getSenderAndReceiver(sender, receiver);
    }





}
