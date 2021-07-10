package com.application.springboot.controller;

import com.application.springboot.model.Interests;
import com.application.springboot.model.User;
import com.application.springboot.service.InterestService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InterestController {

    @Autowired
    private InterestService interestService;
    @Autowired
    private UserService userService;

    @GetMapping("/interest/all")
    public ResponseEntity<List<Interests>> findInterestByUser(@RequestParam("email")String email){
        User user = userService.findExistingEmail(email);
        return ResponseEntity.ok().body(this.interestService.findInterests(user));
    }

    @PostMapping("/interest/save")
    public ResponseEntity<Interests> saveInterest(@RequestParam("email")String email,@RequestParam("interest")String interest) throws  Exception{
        User user = userService.findExistingEmail(email);
        for(Interests existingData : interestService.findInterests(user)){
            if(existingData.getUser().getEmail().equals(email)){
                throw new Exception("DATA_EXIST...");
            }
        }
        Interests   interests  = new  Interests();
        interests.setInterests(interest);
        interests.setUser(user);
        return ResponseEntity.ok().body(interestService.saveInterest(interests));
    }

    @DeleteMapping("/interest/delete")
    public ResponseEntity<Void> deleteInterests(@RequestParam("email")String email, @RequestParam("id")int id) throws Throwable{
        Interests interests = interestService.findInterest(id);
        if(interests.getUser().getEmail().equals(email)){
            this.interestService.deleteInterest(id);
            return ResponseEntity.ok().build();
        }else{
            throw new Exception("YOU_ARE_UNAUTHORIZED");
        }
    }

}
