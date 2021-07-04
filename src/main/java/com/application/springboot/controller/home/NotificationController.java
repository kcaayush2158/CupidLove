package com.application.springboot.controller.home;

import com.application.springboot.model.User;
import com.application.springboot.model.notification.Notification;
import com.application.springboot.service.NotificationService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "/count/notification")
    public @ResponseBody
    ResponseEntity<Integer> countNotification(@RequestParam(value = "email", required = false) String email, Principal principal) {
        User user = this.searchUser(email, principal);
        int notification = notificationService.countNewMessages(false, user.getEmail());
        if (notification > 0) {
            return new ResponseEntity<>(notification, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(0, HttpStatus.OK);
        }

    }

    @GetMapping("/users/notifications/get")
    public @ResponseBody
    ResponseEntity<List<Notification>> getAllNotifications(Principal principal) {
        return ResponseEntity.ok(notificationService.getNotification(principal.getName()));
    }


    @GetMapping("/notification/all")
    public ResponseEntity<List<Notification>> notifications(Model model, @RequestParam("email") String email, Principal principal) {
        User user = this.searchUser(email, principal);

        List<Notification> notification = notificationService.getNotification(user.getEmail());
        return new ResponseEntity<>(notification, HttpStatus.OK);

    }

    @PostMapping("/notification/delete/{id}")
    public ResponseEntity<Integer> deleteNotification(@PathVariable("id") int id) {
        return new ResponseEntity<>(notificationService.deleteNotification(id), HttpStatus.OK);
    }

    @PostMapping("/notification/read/{id}")
    public ResponseEntity<Void> readNotification(@RequestBody @RequestParam String email, @PathVariable("id") int id) {
        notificationService.readNotification(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public User searchUser(String email,Principal principal ) {
        User user;
        if (email != null) {
            user = this.userService.findExistingEmail(email);
        } else {

            user = this.userService.findExistingEmail(principal.getName());
        }
        return user;
    }
}
