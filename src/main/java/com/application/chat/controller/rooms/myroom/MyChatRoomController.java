package com.application.chat.controller.rooms.myroom;

import com.application.chat.model.ChatRoom;
import com.application.chat.service.ChatRoomService;
import com.application.springboot.model.User;
import com.application.springboot.service.NotificationService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyChatRoomController {

    @Autowired
    public ChatRoomService chatRoomService;
    @Autowired
    public NotificationService notificationService;
    @Autowired
    public UserService userService;

    @GetMapping("/my/chatroom")
    public @ResponseBody
    ResponseEntity<List<ChatRoom>> myChatRooms(Model model, @RequestParam(value = "email", required = false) String email, Principal principal) {
        User findUser;
        if (email != null) {
            findUser = userService.findExistingEmail(email);
        } else {
            findUser = userService.findExistingEmail(principal.getName());
        }
        return new ResponseEntity<>(chatRoomService.findChatRoomCreatedBy(findUser), HttpStatus.OK);

    }


    //counts all the room in the principal chatroom
    @GetMapping(value = "/chatroom/my/count")
    public ResponseEntity<Integer> countsAllThePrincipalChatroom(@AuthenticationPrincipal Principal principal, @RequestParam(value = "email", required = false) String email) {
        User user;
        if (email != null) {
            user = userService.findExistingEmail(email);
        } else {
            user = userService.findExistingEmail(principal.getName());
        }
        return new ResponseEntity<>(chatRoomService.countAllChatroom(user), HttpStatus.OK);
    }


}
