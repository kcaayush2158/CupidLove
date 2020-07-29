package com.application.springboot.controller;

import com.application.springboot.model.User;
import com.application.springboot.repository.UserRepository;
import com.application.springboot.service.AboutMeService;
import com.application.springboot.service.AmazonService;
import com.application.springboot.service.SettingService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
public class SettingController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SettingService  settingService;
    @Autowired
    private AboutMeService aboutMeService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // Working change the user password in the settings ....
    @PostMapping("/settings/password/save")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, User user, Errors errors, Model model, Principal principal) {
        if (errors.hasErrors()) {
            model.addAttribute("message", "Please enter the valid password ");
        }
        String newPassword = user.getPassword();

        String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);

        User user1 = userRepository.findExistingEmail(principal.getName());

        System.out.println(newPassword);
        System.out.println(user1.getPassword());

        if (bCryptPasswordEncoder.matches(newPassword, user1.getPassword())) {
            userRepository.changeUserPassword(encryptedPassword, user1.getEmail());
            model.addAttribute("message", "password changed successfully");
            return "settings/settings";
        } else {
            model.addAttribute("message", "Error!!! while saving the password");
        }

        return "settings/settings";
    }


 // delete the user profile permanently

    @GetMapping("/profile/delete")
    public String deleteUserProfile(Principal principal , Model model){
        User user = userService.getAllByEmail(principal.getName());
        settingService.deleteUserProfile(user.getEmail());
        return "login";
    }

//    //change the user information in the settings
//    @PostMapping("/settings/bio/save")
//    public String changeUserInformation(@Valid @ModelAttribute User user, Principal principal, BindingResult bindingResult, AboutMe aboutMe, Model message){
//       if(bindingResult.hasErrors()){
//           message.addAttribute("message","Please fill all the forms correctly");
//           System.out.print("Binding error while saving in the password field");
//           return "settings/settings";
//       }
//
//        User user1 = settingService.getAllUserByEmail(principal.getName());
//       System.out.println(principal);
//        AboutMe profileInformation = aboutMeService.saveProfileInformationInSettings(aboutMe);
//
//             profileInformation.setLookingFor(aboutMe.getLookingFor());
//             userService.saveUser(user);
//        aboutMeService.saveAboutMe(aboutMe);
//        System.out.println(aboutMe);
//        message.addAttribute("message","Password changed successfully");
//        return "settings/settings";
//    }

}
