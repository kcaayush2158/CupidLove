package com.application.chat.controller.rooms.allroom;

import com.application.chat.model.ChatRoom;
import com.application.chat.service.ChatRoomService;
import com.application.chat.service.ShoutOutService;
import com.application.springboot.model.User;
import com.application.springboot.service.NotificationService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@CrossOrigin(origins = "*")
public class ChatRoomAPIController {

    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    public ChatRoomService chatRoomService;
    @Autowired
    public NotificationService notificationService;
    @Autowired
    public UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ShoutOutService shoutOutService;


    @GetMapping("/chatroom")
    public String chatRoom(Model model) {
        model.addAttribute("activeuser", sessionRegistry.getAllPrincipals().size());
        return "/chatroom/chatroom";
    }


    @GetMapping("/chatroom/public/{chatRoomId}")
    public String chatRoom(@PathVariable("chatRoomId") String chatRoomId, Model model) {
        ChatRoom chatRoom = chatRoomService.findChatRoomByChatRoomId(chatRoomId);
        model.addAttribute("chatRoom", chatRoom);
        return "/chatroom/rooms";
    }

    @PostMapping("/chatroom/private/{chatRoomId}")
    public String privateChatroom(@PathVariable("chatRoomId") String chatRoomId, @RequestParam("password-chatroom") String password, Principal principal, Model model, HttpServletRequest request, HttpSession httpSession) {

        ChatRoom chatRoom = chatRoomService.findChatRoomByChatRoomId(chatRoomId);

        if (bCryptPasswordEncoder.matches(password, chatRoom.getPassword())) {
            httpSession.setAttribute("username", principal.getName());
            model.addAttribute("chatRoom", chatRoom);

            User users = userService.findExistingEmail(principal.getName());
            model.addAttribute("users", users);

            return "/chatroom/rooms";
        }
        model.addAttribute("error", "Invalid Password ");
        return "/chatroom/chatroom";
    }


}
