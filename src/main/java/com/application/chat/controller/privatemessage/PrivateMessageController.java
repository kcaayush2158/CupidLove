package com.application.chat.controller.privatemessage;

import com.application.chat.model.Conversation;
import com.application.chat.model.PrivateChatMessage;
import com.application.chat.service.PrivateMessageService;
import com.application.springboot.model.User;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PrivateMessageController {
    @Autowired
    private PrivateMessageService privateMessageService;
    @Autowired
    private UserService userService;

    @GetMapping("/direct/inbox")
    public ResponseEntity<List<PrivateChatMessage>> getAllUsersMessage(@RequestParam(value = "email", required = false) String email, Principal principal) {
        User user = this.searchUser(email, principal);
        return ResponseEntity.ok().body(privateMessageService.getAllMessagesBySender(user));
    }

    @GetMapping("/direct/u/{userId}/count")
    public ResponseEntity<Integer> countUnreadMessages(@RequestParam(value = "email", required = false) String email, Principal principal, @PathVariable("userId") int userId) {
        User user = this.searchUser(email, principal);
        return ResponseEntity.ok().body(privateMessageService.countUnreadMessages(user, userService.findUserById(userId), false));
    }

    @GetMapping("/direct/all")
    public ResponseEntity<List<PrivateChatMessage>> getMessages(@RequestParam(value = "email", required = false) String email, @RequestParam("uuid") String uuid, Principal principal, @RequestParam("userId") int userId)  {
        User authenticatedUser = this.searchUser(email, principal);
        User user = userService.findUserById(userId);
        return ResponseEntity.ok().body(privateMessageService.fetchPrivatesMessages(authenticatedUser, user, uuid));
    }

    @PostMapping("/private/user/delete")
    public ResponseEntity<Void> deletePrivateMessage(@RequestParam("id") int id, @RequestParam("email") String email,Principal principal) {
        User authenticatedUser = searchUser(email,principal);
        User  user = userService.findUserById(id);
        List<PrivateChatMessage> messagesList = privateMessageService.getSenderAndReceiverMessages(authenticatedUser,user);
        for(PrivateChatMessage message : messagesList){
            privateMessageService.deleteMessage(message.getId());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/private/message/delete")
    public ResponseEntity<Void> deleteMessage(@RequestParam("id") int id) {
        privateMessageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/direct/u/{messageId}/send")
    public ResponseEntity<PrivateChatMessage> save(@RequestParam(value = "email", required = false) String email, @RequestParam("message") String message, Principal principal, @RequestParam("roomId") String roomId, @PathVariable("messageId") final int userId) throws UnknownHostException {
        User user = this.searchUser(email, principal);

        User receiverUser = userService.findUserById(userId);

        InetAddress myIP = InetAddress.getLocalHost();

        PrivateChatMessage privateChatMessage = new PrivateChatMessage();
        privateChatMessage.setStatus(false);
        privateChatMessage.setIp(myIP.getHostAddress());

        Conversation conversation = new Conversation();
        conversation.setMessage(message);
        conversation.setStatus(false);
        conversation.setSender(user);
        conversation.setDate(new Date());
        conversation.setIp(myIP.getHostAddress());
        conversation.setReceiver(receiverUser);

        UUID uuid2 = UUID.randomUUID();
        String uuidAsString = uuid2.toString();

        if(roomId == null){
            conversation.setRoomId(uuidAsString);
        }else {
            for (PrivateChatMessage message1 : privateMessageService.fetchPrivatesMessages(user, receiverUser, roomId)) {
                if (message1.getConversation().getRoomId() != null) {
                    conversation.setRoomId(message1.getConversation().getRoomId());
                    privateChatMessage.setConversation(conversation);
                    privateMessageService.save(privateChatMessage);
                    System.out.println("iniizing new  key at line 96");
                    return ResponseEntity.ok().body(privateMessageService.save(privateChatMessage));
                }
            }
            conversation.setRoomId(uuidAsString);
            privateChatMessage.setConversation(conversation);
        }
        return ResponseEntity.ok().body(privateMessageService.save(privateChatMessage));

    }


    public User searchUser(String email, Principal principal) {
        User user;
        if (email != null) {
            user = this.userService.findExistingEmail(email);
        } else {
            user = this.userService.findExistingEmail(principal.getName());
        }
        return user;
    }

}
