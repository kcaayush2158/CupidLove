package com.application.springboot.controller;

import com.application.springboot.model.User;
import com.application.springboot.repository.UserRepository;
import com.application.springboot.service.AboutMeService;
import com.application.springboot.service.NotificationService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@CrossOrigin(origins = "*")
public class MainController {
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AboutMeService aboutMeService;


    @GetMapping("/signup")
    public String signUp(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "Signup";
    }


    @GetMapping(value = "/")
    public String listAllPage(Model model, Principal principal) {

        if (principal != null) {
            model.addAttribute("userProfiles", userService.getAllUserProfile());
            return "home";
        }
        return "index";
    }


}
