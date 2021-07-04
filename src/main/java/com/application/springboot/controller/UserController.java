package com.application.springboot.controller;

import com.application.springboot.model.AboutMe;
import com.application.springboot.model.Role;
import com.application.springboot.model.User;
import com.application.springboot.model.userstore.ActiveUserStore;
import com.application.springboot.repository.UserRepository;
import com.application.springboot.service.AboutMeService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
@Controller
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    ActiveUserStore activeUserStore;
    @Autowired
    public UserService userService;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AboutMeService aboutMeService;
    @Autowired
    private JavaMailSender javaMailSender;
    String senderMessage;
    String senderEmail="enviktatechnologies@gmail.com";
    String senderpassword="Envikta123";

    /*
     *  Used to register the user
     * status: working correctly
     * */
    @PostMapping("/signup")
    public String registerUser( @RequestParam("username") String username, @RequestParam("lastName") String lastName, @RequestParam("bio") String bio, @RequestParam("interests") String interests, @RequestParam String firstName, @RequestParam("age") int age, @RequestParam("password") String password, @RequestParam("country") String country, @RequestParam("known") String known, @RequestParam("lookingFor") String lookingFor, @RequestParam("height") String height, @RequestParam String liveIn, @RequestParam String haveKids, @RequestParam("email") String email, @RequestParam String gender, @RequestParam String bodyType, @RequestParam String drink, @RequestParam String education, @RequestParam String eyes, @RequestParam String hair, @RequestParam String languages, @RequestParam String relationship, @RequestParam String smoke, @RequestParam String workAs, Model model) {
//
        try {
            User existingEmail = userService.findExistingEmail(email);
            if (existingEmail != null) {
                if (email.equals(existingEmail.getEmail())) {
                    model.addAttribute("error", "User is already existed");
                    return "signup";
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        User user = saveUser(username, lastName, firstName, password, email);
        AboutMe aboutMe =  saveAboutMe(age,bio, interests, country, known, lookingFor, height, liveIn, haveKids, gender, bodyType, drink, education, eyes, hair, languages, relationship, smoke, workAs);
        user.setAboutMe(aboutMe);
        userService.saveUser(user);
        model.addAttribute("user", user);
        return "login";

    }



    @PostMapping("/send")
    public  String sendMain(Model model, @RequestParam("message")String message, @RequestParam("email")String email){
        System.out.println("Sending Email...");
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail,senderpassword);
                }
            });
        //compose message
        try {
            MimeMessage message2 = new MimeMessage(session);
            message2.addRecipient(Message.RecipientType.TO,new InternetAddress(senderEmail));

            message2.setText("https://localhost:4200/recover-email?email="+email+"");
            message2.setFrom(email);
            message2.setSentDate(new Date());

            Transport.send(message2);
            model.addAttribute("response","Email sent successfully");
            return "contact";
        } catch (MessagingException e) {throw new RuntimeException(e);}


    }



    @GetMapping("/visits")
    public String visitsPage(Model model) {
        return "visits";
    }

    @PostMapping("/user/aboutme/save")
    public String saveUserAboutMe(@Valid @ModelAttribute AboutMe aboutMe, Model model, @RequestParam("email")String email, Principal principal) {
        User user = userService.findExistingEmail(principal.getName());
        saveAboutMe(aboutMe.getAge(), aboutMe.getBio(), aboutMe.getInterests(), aboutMe.getCountry(), aboutMe.getKnown(), aboutMe.getLookingFor(), aboutMe.getHeight(), aboutMe.getLiveIn(), aboutMe.getHaveKids(), aboutMe.getGender(), aboutMe.getBodyType(), aboutMe.getDrink(), aboutMe.getEducation(),aboutMe.getEyes(), aboutMe.getHair(), aboutMe.getLanguages(), aboutMe.getRelationship(), aboutMe.getSmoke(),aboutMe.getWorkAs());
        user.setAboutMe(aboutMe);
        model.addAttribute("aboutMe", aboutMe);
        aboutMeService.saveAboutMe(aboutMe);;
        userService.saveUser(user);

        return "redirect:/";
    }

    @GetMapping("/aboutme")
    public String aboutme(Model model) {
        AboutMe aboutMe = new AboutMe();
        model.addAttribute("aboutme", aboutMe);
        return "aboutme";
    }

    //Search the user username : status working fine
    @GetMapping("/search")
    public String searchUserByUsername(@RequestParam("username") String username, Model model, Principal principal) {
        List<User> user = userRepository.searchByUsername(username);
        int totalUser = userService.countTotalUser(username);
        if (user != null) {
            model.addAttribute("message", "Search Results " + totalUser);
            model.addAttribute("users", user);
            return "search";
        } else {
            model.addAttribute("message", "User not found");
        }
        return "search";
    }




    private AboutMe saveAboutMe( int age,String bio, String interests,  String country,  String known, String lookingFor, String height,  String liveIn,  String haveKids,  String gender,  String bodyType,  String drink,  String education,  String eyes,  String hair,  String languages,  String relationship,  String smoke,String workAs) {
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

    private User saveUser(String username,String lastName,  String firstName,  String password, String email) {
        User user = new User();
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
