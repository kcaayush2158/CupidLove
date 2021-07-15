package com.application.springboot.controller;

import com.application.springboot.model.User;
import com.application.springboot.repository.UserRepository;
import com.application.springboot.service.AboutMeService;
import com.application.springboot.service.SettingService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@CrossOrigin(origins = "*")
public class SettingController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SettingService settingService;
    @Autowired
    private AboutMeService aboutMeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // Working change the user password in the settings ....
    @PostMapping("/api/settings/password/save")
    public void changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("password") String password, Model model, Principal principal) {

        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        User user = userRepository.findExistingEmail(principal.getName());
        if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            userRepository.changeUserPassword(encryptedPassword, user.getEmail());
        }
    }

    // delete the user profile permanently
    @GetMapping("/profile/delete")
    public String deleteUserProfile(Principal principal, Model model) {
        User user = userService.findExistingEmail(principal.getName());
        settingService.deleteUserProfile(user.getEmail());
        return "login";
    }

    @GetMapping("/settings")
    public String settings(Model model, Principal principal) {
        model.addAttribute("users", userService.findExistingEmail(principal.getName()));
        return "/settings/settings";
    }


}
