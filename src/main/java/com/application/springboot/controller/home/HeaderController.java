package com.application.springboot.controller.home;

import com.application.springboot.model.User;
import com.application.springboot.repository.UserRepository;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HeaderController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    //Search the user username : status working fine
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByUsername(@RequestParam("username") String username) {
        List<User> user = userRepository.searchByUsername(username);
        if(user!=null){
            return ResponseEntity.ok().body(user);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/user")
    public ResponseEntity<List<User>> searchUser(@RequestParam("fromAge") int fromAge, @RequestParam("toAge") int toAge, @RequestParam("gender") String gender, @RequestParam("country") String country) {
        List<User> users = userService.searchUserProfile(gender, fromAge, toAge, country);
        if(users!=null){
            return ResponseEntity.ok().body(users);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/user/count")
    public ResponseEntity<Integer> countTotalUser(@RequestParam("fromAge") int fromAge, @RequestParam("toAge") int toAge, @RequestParam("gender") String gender, @RequestParam("country") String country) {
        int totalSearchedUser = userService.countUserProfile(gender, fromAge, toAge, country);
        if (totalSearchedUser != 0) {
            return ResponseEntity.ok().body(totalSearchedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
