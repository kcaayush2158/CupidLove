package com.application.chat.service;

import com.application.chat.model.PublicChatMessage;
import com.application.chat.repository.PublicChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicChatroomService {

@Autowired
private PublicChatMessageRepository publicChatMessageRepository;

    public List<PublicChatMessage> getAllPublicChats(String chatroomId) {
        return publicChatMessageRepository.getAllByChatRoomId(chatroomId);
    }


    public PublicChatMessage savePublicChats( PublicChatMessage publicChatMessage){
        return  publicChatMessageRepository.save(publicChatMessage);
    }
    public void deletePublicChat(int id){
        publicChatMessageRepository.deleteById(id);
    }


}
