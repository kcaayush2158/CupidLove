package com.application.chat.controller.rooms.allroom;

import com.application.chat.model.ChatRoom;
import com.application.chat.model.Conversation;
import com.application.chat.service.ChatRoomService;
import com.application.springboot.model.User;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")
@CrossOrigin("*")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/chatroom")
    public String chatRoom(Model model, Principal principal) {
        model.addAttribute("activeuser", sessionRegistry.getAllPrincipals().size());
        return "/chatroom/chatroom";
    }

    @GetMapping("/chatrooms/all")
    public ResponseEntity<List<ChatRoom>> chatRooms() {
        return new ResponseEntity<>(chatRoomService.findAllChatRoom(), HttpStatus.OK);
    }

    @PostMapping("/chatroom/create")
    public String createChatRoom( @RequestParam("email") String email, @RequestParam(value = "chatRoomName", required = false) String chatRoomName, @RequestParam("password") String password, @RequestParam("roomDescription") String roomDescription, @RequestParam("roomType") String roomType, Model model, Principal principal) throws Exception {
        User user= this.searchUser(email, principal);

        ChatRoom existedChatRoom = chatRoomService.findByChatRoomName(chatRoomName);

        if (existedChatRoom != null) {
            if (existedChatRoom.getChatRoomName().equals(chatRoomName)) {

                //updates the chatroom by the principal
                if (existedChatRoom.getCreatedBy().getEmail().equals(principal.getName())) {

                    ChatRoom chatroom = chatRoomService.findChatRoomByChatRoomId(existedChatRoom.getChatRoomId());

                    chatRoomService.updateChatRoom(chatRoomName, roomDescription, roomType, new Date(), existedChatRoom.getChatRoomId());

                } else {
                    throw new Exception("You are unable to change the room");
                }


            }
        }

        ChatRoom chatRoom1 = creatChatRoom( chatRoomName, password, roomDescription, roomType, user);
        chatRoomService.createChatRoom(chatRoom1);

        return "redirect:/chatroom";

    }



    @PostMapping("/chatroom/{chatRoomId}/update")
    public ResponseEntity<Void> updateChatRooms(@PathVariable("chatRoomId") String ChatRoomId, @RequestParam("chatRoomName") String chatRoomName, @RequestParam("password") String password, @RequestParam("roomDescription") String roomDescription, @RequestParam("roomType") String roomType, @RequestParam("email")String email, Model model, Principal principal)  {
        User user = this.searchUser(email,principal);
        ChatRoom existedChatRoom = chatRoomService.findChatRoomByChatRoomId(ChatRoomId);
        if (existedChatRoom.getCreatedBy().getEmail().equals(user.getEmail())) {
            chatRoomService.updateChatRoom(chatRoomName, roomDescription, roomType, new Date(), existedChatRoom.getChatRoomId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/chatroom/search/{name}")
    @ResponseBody
    public List<ChatRoom> searchChatRooms(@PathVariable("name") String name) {
        return chatRoomService.searchChatRooms(name);

    }

    @GetMapping("/chatroom/{ChatroomId}")
    public @ResponseBody
    ChatRoom rooms(@PathVariable("ChatroomId") String ChatroomId, Model model) {
        return chatRoomService.findChatRoomByChatRoomId(ChatroomId);
    }

    @PostMapping("/chatroom/{chatRoomId}/delete")
    @ResponseBody
    public int deleteChatRoom(@PathVariable("chatRoomId") String chatRoomId) {
        return chatRoomService.deleteChatRoom(chatRoomId);
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

    private ChatRoom creatChatRoom(String chatRoomName,String password,  String roomDescription, String roomType, User user){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomName(chatRoomName);
        chatRoom.setType(roomType);
        chatRoom.setChatRoomId(UUID.randomUUID().toString());
        chatRoom.setCreatedBy(user);
        chatRoom.setPassword(bCryptPasswordEncoder.encode(password));
        chatRoom.setChatRoomDescription(roomDescription);
        chatRoom.setCreatedTime(new Date());
        return chatRoom;
    }
}
