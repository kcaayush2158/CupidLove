package com.application.chat.controller.message;

import com.application.chat.model.PublicChatMessage;
import com.application.chat.service.PublicChatroomService;
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

@RestController
@RequestMapping("/api")
public class PublicChatRoomAPI {

    @Autowired
    private PublicChatroomService publicChatroomService;
    @Autowired
    private UserService userService;


    @PostMapping("/chatroom/public/{chatroomId}/save")
    public ResponseEntity<Void> savePublicChatRoomMessage( @RequestParam("message") String message, @PathVariable("chatroomId") final String chatroomId, @RequestParam(value = "email",required = false) String email,Principal principal) throws UnknownHostException {
        InetAddress myIP=InetAddress.getLocalHost();
        User user = this.searchUser(email,principal);
        PublicChatMessage publicChatMessage = savePublicChats(message, chatroomId, myIP, user);
        publicChatroomService.savePublicChats(publicChatMessage);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/chatroom/public/{chatroomId}/all")
    public ResponseEntity<List<PublicChatMessage>> findAllPublicChats(@RequestParam(value = "email",required = false) String email,@PathVariable("chatroomId") final String chatroomId){
        return  ResponseEntity.ok().body(publicChatroomService.getAllPublicChats(chatroomId));
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> getAllUsers(){
            return ResponseEntity.ok(userService.listUser());
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

    private PublicChatMessage savePublicChats(String message,String chatroomId, InetAddress myIP, User user) {
        PublicChatMessage publicChatMessage = new PublicChatMessage();
        publicChatMessage.setDate(new Date());
        publicChatMessage.setMessage(message);
        publicChatMessage.setChatRoomId(chatroomId);
        publicChatMessage.setStatus(false);
        publicChatMessage.setType("PUBLIC");
        publicChatMessage.setSender(user);
        publicChatMessage.setIp(myIP.getHostAddress());
        return publicChatMessage;
    }



}
