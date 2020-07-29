package com.application.chat.service;

import com.application.chat.model.ChatRoom;
import com.application.chat.repository.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChatRoomService {

    @Autowired
    private ChatroomRepository chatroomRepository;

    public void createChatRoom(ChatRoom chatRoom){
        chatroomRepository.save(chatRoom);
    }

    public List<ChatRoom> findAllChatRoom( ){
        return chatroomRepository.findAll();
    }

    public List<ChatRoom> searchChatRooms(String name){
        return  chatroomRepository.findChatRoomByChatRoomNameStartingWith(name);
    }
    public ChatRoom findByChatRoomName(String name){
        return  chatroomRepository.findByChatRoomName(name);
    }

    public ChatRoom findByChatRoomId(int id) {
        return chatroomRepository.findChatRoomById(id);
    }

    public void updateChatRoom(String chatRoomName, String chatRoomDescription, String chatRoomType, Date createdTime){
        chatroomRepository.updateChatRoom(chatRoomName, chatRoomDescription, chatRoomType, createdTime);
    }
    public ChatRoom deleteChatRoom(int id){
        return chatroomRepository.deleteChatRoomById(id);
    }
}
