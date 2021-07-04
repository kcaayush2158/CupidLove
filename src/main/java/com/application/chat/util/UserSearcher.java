package com.application.chat.util;

import com.application.springboot.model.User;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;

public class UserSearcher {
    @Autowired
    public UserService userService;

    public User searchUser(String email,Principal principal ) {
        User user;
        if (email != null) {
            user = userService.findExistingEmail(email);
        } else {

            user = userService.findExistingEmail(principal.getName());
        }

        return user;
    }
}


