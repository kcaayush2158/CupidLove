package com.application.springboot.controller;

import com.application.springboot.model.AboutMe;
import com.application.springboot.model.User;
import com.application.springboot.model.message.Message;
import com.application.springboot.model.notification.Notification;
import com.application.springboot.repository.UserRepository;
import com.application.springboot.service.AboutMeService;
import com.application.springboot.service.NotificationService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
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
    public String signup(Model model, User user) {
        model.addAttribute("user", user);
        return "Signup";
    }


    @GetMapping(value = "/")
    public String listAllPage(Model model, Principal principal, AboutMe aboutMe, Authentication authentication) {

        if (principal != null) {

            //retrieves the user profile picture
            List<Notification> notification = notificationService.getNotification(principal.getName());
            for (Notification notify : notification) {
                System.out.println(notify.getUserSender());
                User user = userService.findExistingEmail(notify.getUserSender());
                    //if user doesnt have the information then it will rediect to the profile page;
                    if(user.getAboutMe().equals(null) || user.getAboutMe().equals("") ){
                        model.addAttribute("error","Please fill your information first to visit another page");
                        return "redirect:/profile";
                    }
                model.addAttribute("picture", user.getProfilePhoto());

              // model.addAttribute("sessionInformation ",sessionInformations);
                model.addAttribute("userProfiles",userService.getAllUserProfile());
                model.addAttribute("activeuser",  sessionRegistry.getAllPrincipals().size());
                //for notification
                model.addAttribute("newNotification", notificationService.countNewMessages(false,notify.getUserReceiver()));
                model.addAttribute("notifications", notification);
            }

            model.addAttribute("aboutMe", aboutMe);
            return "home";
        }
        return "index";
    }

    @GetMapping("/message")
    public String message(@ModelAttribute Message message, Model model) {

        model.addAttribute("message", message);
        return "message";
    }



}
