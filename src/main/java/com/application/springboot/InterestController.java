package com.application.springboot;

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
        Interests interests = null;
        for(Interests existingData : interestService.findInterests(user)){
            if(interest.equals(existingData.getInterests())){
                throw  new Exception("DATA_EXISTED");
            }else{
                interests = new Interests();
                interests.setInterests(interest);
                interests.setUser(user);
            }
        }
        return ResponseEntity.ok().body(this.interestService.saveInterest(interests));
    }

    @DeleteMapping("/interest/delete")
    public ResponseEntity<Void> deleteInterests(@RequestParam("email")String email, @RequestParam("id")int id) throws Throwable{
        User user = userService.findUserById(id);
        if(user.getEmail().equals(email)){
            this.interestService.deleteInterest(id);
        }else{
            throw new Exception("YOU_ARE_UNAUTHORIZED");
        }
         return ResponseEntity.ok().build();
    }

}
