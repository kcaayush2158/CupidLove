package com.application.springboot.controller;

import com.application.springboot.model.AboutMe;
import com.application.springboot.model.Role;
import com.application.springboot.model.User;
import com.application.springboot.model.userstore.ActiveUserStore;
import com.application.springboot.repository.UserRepository;
import com.application.springboot.service.AboutMeService;
import com.application.springboot.service.UserService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
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

    /*
     *  Used to register the user
     * status: working correctly
     * */
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, AboutMe aboutMe, Model model, @RequestParam("email") String email) {


            ConnectionFactory connectionFactory = new ConnectionFactory();
            try {
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(user.getUsername(), false, false, false, null);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());

            }
            user.setActive(true);
            user.setFirstName(user.getFirstName());
            user.setLastName(user.getLastName());
            user.setId(user.getId());
            user.setEmail(user.getEmail());
            user.setRoles(Collections.singletonList(new Role("USER")));
            user.setUsername(user.getUsername());
            user.setCreatedDate(new Date());
            user.setAboutMe(user.getAboutMe());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        return "login";

    }

    @PostMapping("/user/aboutme/save")
    public String saveUserAboutMe(@Valid @ModelAttribute AboutMe aboutMe, Model model, Principal principal){
       User user = userService.getAllByEmail(principal.getName());
        aboutMe.setAge(aboutMe.getAge());
        aboutMe.setLookingFor(aboutMe.getLookingFor());
        aboutMe.setBio(aboutMe.getBio());
        aboutMe.setBodyType(aboutMe.getBodyType());
        aboutMe.setCountry(aboutMe.getCountry());
        aboutMe.setDrink(aboutMe.getDrink());
        aboutMe.setEducation(aboutMe.getEducation());
        aboutMe.setGender(aboutMe.getGender());
        aboutMe.setHaveKids(aboutMe.getHaveKids());
        aboutMe.setWorkAs(aboutMe.getWorkAs());
        aboutMe.setSmoke(aboutMe.getSmoke());
        aboutMe.setRelationship(aboutMe.getRelationship());
        aboutMe.setEyes(aboutMe.getEyes());
        user.setAboutMe(aboutMe);
        model.addAttribute("aboutMe",aboutMe);
        userService.saveUser(user);
        aboutMeService.saveAboutMe(aboutMe);
        return "redirect:/";
    }

    @GetMapping("/aboutme")
    public String aboutme (Model model,AboutMe aboutMe){
         model.addAttribute("aboutme",aboutMe);
        return "aboutme" ;
    }

    //Search the user username : status working fine
    @GetMapping("/search")
    public String searchUserByUsername(@RequestParam("username") String username, Model model,Principal principal){
        List<User> user =  userRepository.searchByUsername(username);
        int totalUser= userService.countTotalUser(username);
        if(user !=null){
            model.addAttribute("message","Search Results "+ totalUser );
            model.addAttribute("users", user);
            return "search";
        }else{
            model.addAttribute("message","User not found");
        }
        return "search";
    }




}
