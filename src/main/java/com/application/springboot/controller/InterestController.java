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
            if(existingData.getInterest().equals(interest)  && existingData.getUser().getEmail().equals(email)){
                throw new Exception("DATA_EXIST...");
            }
        }
        Interests   interests  = new  Interests();
        interests.setInterest(interest);
        interests.setUser(user);
        return ResponseEntity.ok().body(interestService.saveInterest(interests));
    }

    @PostMapping("/interest/delete")
    public ResponseEntity<Void> deleteInterests(@RequestParam("email")String email, @RequestParam("id")int id) throws Throwable{
        Interests interests = interestService.findInterest(id);
        System.out.println(interests);
        if(interests.getUser().getEmail().equals(email)){
            System.out.println("DELETING");
            interestService.deleteInterest(id);

        }else{
            throw new Exception("YOU_ARE_UNAUTHORIZED");
        }
        return ResponseEntity.ok().build();
    }


}
