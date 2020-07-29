package com.application.chat.controller;

import com.application.chat.model.ChatRoom;
import com.application.chat.service.ChatRoomService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserService userService;

    @PostMapping("/chatroom/create")
    public String createChatRoom( @ModelAttribute("chatRoom") ChatRoom chatRoom, BindingResult bindingResult, Model model, @RequestParam("roomName") String roomName, @RequestParam("roomDescription") String roomDescription, @RequestParam("roomType") String roomType ){

        ChatRoom existedChatRoom = chatRoomService.findByChatRoomName(roomName);

        if(existedChatRoom !=null){
            if(existedChatRoom.getChatRoomName().equals(roomName)){
                System.out.print("UPDATING THE ROOM....");
                chatRoomService.updateChatRoom(roomName, roomDescription, roomType,new Date());
                System.out.print(existedChatRoom.getId());
            }

        }


        System.out.print("existedChatRoom rooms is fired");   chatRoom.setChatRoomName(roomName);
        chatRoom.setType(roomType);
        chatRoom.setChatRoomDescription(roomDescription);
        chatRoom.setCreatedTime(new Date());
        chatRoomService.createChatRoom(chatRoom);

        return "redirect:/chatroom";

    }
    @GetMapping("/chatroom/search/{name}")
    public @ResponseBody String searchChatRoom(@PathVariable("name") String name) {
        List<ChatRoom> chatRooms = chatRoomService.searchChatRooms(name);
        String result= "";
        for (ChatRoom room : chatRooms){
            result += "<div class=\"card bg-light mb-3 mr-5  ml-5 mt-2 \" id=\"custom-card-design\">" +
                    "<div class=\"card-body\"><h5 class=\"card-title\">"+room.getChatRoomName() +"</h5>" +
                    "<p class=\"card-text\">"+room.getChatRoomDescription()+"</p><br>" +
                    "<button class='btn btn-success'>Join Room</button>" +
                    " </div>" +
                    "</div>";
        }
        return  result;
    }
    @GetMapping("/chatroom/{id}")
    public @ResponseBody  ChatRoom rooms(@PathVariable("id")int id, Model model){
        return chatRoomService.findByChatRoomId(id);
    }

    @DeleteMapping("/chatroom/{id}/delete")
    public ChatRoom chatRoom(@PathVariable("id")int id){
        return chatRoomService.deleteChatRoom(id);
    }


}
