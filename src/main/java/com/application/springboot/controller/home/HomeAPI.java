package com.application.springboot.controller.home;


import com.application.springboot.model.AboutMe;
import com.application.springboot.model.Role;
import com.application.springboot.model.User;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HomeAPI {
    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("sessionRegistry")
    private SessionRegistry sessionRegistry;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/count/users/online")
    public @ResponseBody
    ResponseEntity<Integer> getOnlineUsers() {
        return new ResponseEntity<>(sessionRegistry.getAllPrincipals().size(), HttpStatus.OK);
    }

    @GetMapping("/user/status/get")
    public ResponseEntity<User> getUserStatus(boolean status, Principal principal) {
        User user = userService.findExistingEmail(principal.getName());
        return new ResponseEntity<>(userService.getUserStatus(true, user.getEmail()), HttpStatus.OK);
    }

    @GetMapping("/user/status/{id}/update")
    public ResponseEntity<User> updateUserStatus(@RequestParam("active") boolean active, Principal principal) {
        return new ResponseEntity<>(userService.getUserStatus(active, principal.getName()), HttpStatus.CREATED);
    }


    @RequestMapping("/user")
    public ResponseEntity<Principal> user(Principal user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/u/online")
    public List<Object> findMessagesForUser() {
        return sessionRegistry.getAllPrincipals();
    }


    @GetMapping("/u/{email}/online/get")
    public ResponseEntity<User> getAllOnlineUsers(@PathVariable(value = "email", required = false) String email) {
        User onlineUsers = userService.findExistingEmail(email);
        return new ResponseEntity<>(onlineUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/user/login")
    public ResponseEntity<User> loginUsers(@RequestParam("email") String email, @RequestParam("password") String password) {

        User userPrincipal = userService.findExistingEmail(email);
        if (userPrincipal.getEmail().equals(email)) {
            if (bCryptPasswordEncoder.matches(password, userPrincipal.getPassword())) {
                return new ResponseEntity<>(userPrincipal, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/user/own-country")
    public ResponseEntity<List<User>> getUserFromOwnCountry(@RequestParam("country")String country){
        return ResponseEntity.ok().body( userService.findUsersFromOwnCountry(country));
    }
    @GetMapping("/user/switch-gender")
    public ResponseEntity<List<User>> switchGender(@RequestParam("gender")String gender){
        return ResponseEntity.ok().body( userService.switchGenders(gender));
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser( @RequestParam("username") String username, @RequestParam("lastName") String lastName, @RequestParam("bio") String bio, @RequestParam("interests") String interests, @RequestParam("firstName") String firstName, @RequestParam("age") String age, @RequestParam("password") String password, @RequestParam("country") String country, @RequestParam("known") String known, @RequestParam("lookingFor") String lookingFor, @RequestParam("height") String height, @RequestParam String liveIn, @RequestParam String haveKids, @RequestParam("email") String email, @RequestParam String gender, @RequestParam String bodyType, @RequestParam String drink, @RequestParam String education, @RequestParam String eyes, @RequestParam String hair, @RequestParam String languages, @RequestParam String relationship, @RequestParam String smoke, @RequestParam String workAs, Model model) throws Exception {
        User user = userService.findExistingEmail(email);
        if(user ==null){
            user = this.saveUser(user.getId(),username, lastName, firstName, password, email);
            user.setProfilePhoto(user.getProfilePhoto());
            int convertedAge = Integer.parseInt(age);
            AboutMe aboutMe =  saveAboutMe(convertedAge,bio, interests, country, known, lookingFor, height, liveIn, haveKids, gender, bodyType, drink, education, eyes, hair, languages, relationship, smoke, workAs);
            user.setAboutMe(aboutMe);
            userService.saveUser(user);
            model.addAttribute("user", user);
            return ResponseEntity.ok().body(user);
        }else{
            if ( user != null || email.equals(user.getEmail())) {
                throw new Exception("USER_ALREADY EXISTED");
            }
        }
    return ResponseEntity.ok().build();
    }


    @PostMapping("/user/update")
    public ResponseEntity<User> update( @RequestParam("username") String username, @RequestParam("lastName") String lastName, @RequestParam("bio") String bio, @RequestParam("interests") String interests, @RequestParam("firstName") String firstName, @RequestParam("age") String age, @RequestParam("country") String country, @RequestParam("known") String known, @RequestParam("lookingFor") String lookingFor, @RequestParam("height") String height, @RequestParam String liveIn, @RequestParam String haveKids, @RequestParam("email") String email, @RequestParam String gender, @RequestParam String bodyType, @RequestParam String drink, @RequestParam String education, @RequestParam String eyes, @RequestParam String hair, @RequestParam String languages, @RequestParam String relationship, @RequestParam String smoke, @RequestParam String workAs, Model model) throws Exception {
        User user = userService.findExistingEmail(email);
                user.setProfilePhoto(user.getProfilePhoto());
            user = saveUser(user.getId(),username, lastName, firstName, user.getPassword(), email);
            int convertedAge = Integer.parseInt(age);
            AboutMe aboutMe =  saveAboutMe(convertedAge,bio, interests, country, known, lookingFor, height, liveIn, haveKids, gender, bodyType, drink, education, eyes, hair, languages, relationship, smoke, workAs);
            user.setAboutMe(aboutMe);
            userService.saveUser(user);
            return ResponseEntity.ok().body(user);
    }


    private AboutMe saveAboutMe(int age, String bio, String interests, String country, String known, String lookingFor, String height, String liveIn, String haveKids, String gender, String bodyType, String drink, String education, String eyes, String hair, String languages, String relationship, String smoke, String workAs) {
        AboutMe aboutMe = new AboutMe();
        aboutMe.setAge(age);
        aboutMe.setCountry(country);
        aboutMe.setBodyType(bodyType);
        aboutMe.setDrink(drink);
        aboutMe.setEducation(education);
        aboutMe.setLiveIn(liveIn);
        aboutMe.setEyes(eyes);
        aboutMe.setGender(gender);
        aboutMe.setKnown(known);
        aboutMe.setHaveKids(haveKids);
        aboutMe.setHair(hair);
        aboutMe.setHeight(height);
        aboutMe.setInterests(interests);
        aboutMe.setLanguages(languages);
        aboutMe.setLiveIn(liveIn);
        aboutMe.setLookingFor(lookingFor);
        aboutMe.setRelationship(relationship);
        aboutMe.setSmoke(smoke);
        aboutMe.setWorkAs(workAs);
        aboutMe.setBio(bio);
        aboutMe.setKnown(known);
        return aboutMe;
    }

    private User saveUser(int  id,String username,String lastName,  String firstName,  String password, String email) {
        User user = new User();
        user.setId(id);
        user.setRoles(Collections.singletonList(new Role("USER")));
        user.setUsername(username);

        user.setCreatedDate(new Date());
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setActive(true);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        return user;
    }

}
