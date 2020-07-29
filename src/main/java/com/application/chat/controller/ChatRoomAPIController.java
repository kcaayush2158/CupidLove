package com.application.chat.controller;

import com.application.chat.model.ChatRoom;
import com.application.chat.service.ChatRoomService;
import com.application.springboot.model.notification.Notification;
import com.application.springboot.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatRoomAPIController {
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    public ChatRoomService chatRoomService;
    @Autowired
    public NotificationService notificationService;

    @GetMapping("/chatroom")
    public String chatRoom(Model model, Principal principal){
        model.addAttribute("activeuser",  sessionRegistry.getAllPrincipals().size());
        List<Notification> notification = notificationService.getNotification(principal.getName());
        for (Notification notify : notification) {
            model.addAttribute("newNotification", notificationService.countNewMessages(false,notify.getUserReceiver()));
            model.addAttribute("notifications", notification);
        }
        model.addAttribute("chatRooms",chatRoomService.findAllChatRoom());
        return "/chatroom/chatroom";
    }

    @GetMapping("/chatRooms")
    public @ResponseBody  List<ChatRoom> chatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.findAllChatRoom();
        return  chatRooms;
    }


    @GetMapping("/chatroom/{id}")
     public String chatRoom(@PathVariable("id")int id, Model model){
        ChatRoom chatRoom = chatRoomService.findByChatRoomId(id);
        System.out.println(chatRoom.getChatRoomName());
            if(chatRoom != null){
                model.addAttribute("chatRoom",chatRoom);
                return "/chatroom/rooms";
            }
        model.addAttribute("message","Room not found");
        return "redirect:/chatroom?error=true";
    }

}
