package com.application.chat.controller.shoutout;

import com.application.chat.model.ShoutOut;
import com.application.chat.service.ShoutOutService;
import com.application.chat.util.UserSearcher;
import com.application.springboot.model.User;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.InetAddress;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ShoutOutController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShoutOutService shoutOutService;

    @GetMapping("/chatrooms/shoutout/all")
    public @ResponseBody
    ResponseEntity<List<ShoutOut>> getAllShoutOuts(){
        return ResponseEntity.ok().body(shoutOutService.getAllShoutOuts());
    }

    @PostMapping("/chatrooms/shoutout/delete")
    public ResponseEntity<Void> deleteShoutOuts(@RequestParam("id") int id){
        shoutOutService.deleteShoutOuts(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chatrooms/shoutout/save")
    public ResponseEntity<ShoutOut> saveShoutOuts( @RequestParam("message") String message, @RequestParam(value = "email",required = false)String email, Principal principal) throws Exception {
        InetAddress myIP=InetAddress.getLocalHost();
        ShoutOut shoutOut = new ShoutOut();
        User user=searchUser(email,principal);
        shoutOut.setIp(myIP.getHostAddress());
        shoutOut.setMessage(message);
        shoutOut.setUser(user);
        return ResponseEntity.ok().body(shoutOutService.saveShoutOuts(shoutOut));
    }

    public User searchUser(String email,Principal principal ) {
        User user;
        if (email != null) {
            user = userService.findExistingEmail(email);
        } else {

            user = userService.findExistingEmail(principal.getName());
        }

        return user;
    }
}
