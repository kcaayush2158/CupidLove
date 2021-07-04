package com.application.springboot.controller.profile;

import com.application.springboot.model.User;
import com.application.springboot.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProfileRestController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile/navigate")
    public List<User> getUniqueUsers(@RequestParam("id")int id ){
       int initialId = id-1;
       int endId = id + 1;
        return this.profileService.getUniquerUsersForNavigation(initialId , endId);
    }
}
