package com.application.springboot.controller;

import com.application.springboot.model.notification.Notification;
import com.application.springboot.service.NotificationService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @GetMapping("/notification/all")
    public @ResponseBody List<Notification> notifications( Model model, Principal principal){
        return notificationService.getNotification(principal.getName());

    }
    @DeleteMapping("/notification/delete/{id}")
    public Notification deleteNotification(@PathVariable("id") int id){
        return notificationService.deleteNotification(id);
    }
}
