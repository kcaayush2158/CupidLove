package com.application.chat.repository;


import com.application.chat.model.PublicChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicChatMessageRepository extends JpaRepository<PublicChatMessage, Integer> {

    List<PublicChatMessage> getAllByChatRoomId(String chatroomId);

    void deletePublicChatMessageById(int id);

}
