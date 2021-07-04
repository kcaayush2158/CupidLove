package com.application.springboot.controller;

import com.application.chat.util.UserSearcher;
import com.application.springboot.model.User;
import com.application.springboot.model.Visits;
import com.application.springboot.service.UserService;
import com.application.springboot.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VisitsController {
    @Autowired
    private VisitService visitService;
    @Autowired
    private UserService userService;

    private UserSearcher userSearcher;

    @GetMapping("/visits")
    public ResponseEntity<List<Visits>> getVisits(@RequestParam(value = "email", required = false) String email, Principal principal) {
        User user;
        if (email != null) {
            user = this.userService.findExistingEmail(email);
        } else {

            user = this.userService.findExistingEmail(principal.getName());
        }
        return ResponseEntity.ok().body(visitService.getUsers(user));
    }


    @GetMapping("/visits/users/count")
    public ResponseEntity<Integer> countTotalVisitedUsers(@RequestParam(value = "email", required = false) String email, Principal principal) {
//        User user = userSearcher.searchUser(email, principal);
        User user;
        if (email != null) {
            user = this.userService.findExistingEmail(email);
        } else {

            user = this.userService.findExistingEmail(principal.getName());
        }
        return ResponseEntity.ok().body(visitService.countTotalVisitedUsers(user));
    }


}
