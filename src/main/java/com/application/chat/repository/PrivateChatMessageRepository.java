package com.application.chat.repository;


import com.application.chat.model.PrivateChatMessage;
import com.application.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PrivateChatMessageRepository extends JpaRepository<PrivateChatMessage, Integer> {


    @Query("select distinct c from PrivateChatMessage c where c.conversation.sender= :user or c.conversation.receiver=:user GROUP BY  c.conversation.roomId  order by c.conversation.id asc ")
    List<PrivateChatMessage> getDistinctByConversation_SenderOrderByIdDesc(User user);

    @Query("select c from PrivateChatMessage c  where c.conversation.sender=:user and c.conversation.receiver= :receiver or c.conversation.sender= :receiver and c.conversation.receiver= :user  and c.conversation.roomId=:uuid order by c.conversation.id asc ")
    List<PrivateChatMessage> fetchPrivateMessages(User user, User receiver, String uuid);


    PrivateChatMessage findPrivateChatMessagesById(int id);

    @Modifying
    @Transactional
    int countPrivateChatMessagesByConversation_ReceiverAndConversation_SenderAndConversation_Status(User receiver, User sender, boolean status);

    @Query("SELECT p from PrivateChatMessage p where p.conversation.sender=:sender and p.conversation.receiver=:receiver")
    List<PrivateChatMessage> getSenderAndReceiver(User sender,User receiver);

    @Query("delete from PrivateChatMessage  p where p.conversation.sender=:sender and  p.conversation.receiver= :receiver")
    void  deleteUser(User sender,User receiver);

    @Transactional
    PrivateChatMessage deleteById(int id);


}
