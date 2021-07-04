package com.application.springboot.controller.like;

import com.application.springboot.model.Likes;
import com.application.springboot.model.User;
import com.application.springboot.model.notification.Notification;
import com.application.springboot.service.LikeService.LikesService;
import com.application.springboot.service.NotificationService;
import com.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class LikeRestApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private LikesService likesService;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/likes/save")
    public ResponseEntity<Likes> saveLikes(@RequestParam("id") int id, @RequestParam(value = "email", required = false) String email, Principal principal) {
        User likedByUser = this.searchUser(email, principal);

        User likedToUser = userService.findUserById(id);

        Likes existingUser = likesService.findExistingUser(likedToUser, likedByUser);

        if (existingUser == null) {
            //sends the user notification to the users
            Notification notification = saveNotification(false, likedToUser, likedByUser);
            notification.setUser(likedByUser);
            Likes like = saveLike(likedByUser, likedToUser);
            return ResponseEntity.ok().body(likesService.saveLikes(like));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/likes/all")
    public ResponseEntity<List<Likes>> getAllLikedUsers(@RequestParam(value = "email", required = false) String email, Principal principal) {
        User user = this.searchUser(email, principal);
        return ResponseEntity.ok().body(likesService.getLikedUsers(user));
    }

    @GetMapping("/likes/u/all")
    public ResponseEntity<List<Likes>> getAllLikedByPrincipal(@RequestParam(value = "email", required = false) String email, Principal principal) {
        User user = this.searchUser(email, principal);
        return ResponseEntity.ok().body(likesService.getYouLiked(user));
    }

    @GetMapping("/likes/users/count")
    public ResponseEntity<Integer> countTotalVisitedUsers(@RequestParam(value = "email", required = false) String email, Principal principal) {
        User user = this.searchUser(email, principal);
        return ResponseEntity.ok().body(likesService.countTotalLikes(user));
    }

    @GetMapping("/likes/you-liked/count")
    public ResponseEntity<Integer> countTotalUserLikesBy(@RequestParam(value = "email", required = false) String email, Principal principal) {
        User user = this.searchUser(email, principal);
        return new ResponseEntity<>(likesService.countTotalUserLikesBy(user), HttpStatus.OK);
    }

    @PostMapping("/likes/{id}/update")
    public ResponseEntity<Likes> updatesLikeStatus(@RequestParam(value = "email", required = false) String email, @RequestBody @PathVariable("id") int id, Principal principal) {
        User user = this.searchUser(email, principal);
        Likes likes = new Likes();
        likes.setLikedBy(user);
        likes.setLikedTo(likes.getLikedTo());
        if (likes.isStatus()) {
            likes.setStatus(false);
        } else {
            likes.setStatus(true);
        }
        return ResponseEntity.ok().body(likesService.updateLikes(likes.getId(), true));
    }


    @PostMapping("/likes/{id}/delete")
    public ResponseEntity<Integer> deleteLike(@RequestBody @PathVariable("id") int id) {
        return ResponseEntity.ok().body(likesService.deleteLikes(id));
    }


    public User searchUser(String email, Principal principal) {
        User user;
        if (email != null) {
            user = this.userService.findExistingEmail(email);
        } else {
            user = this.userService.findExistingEmail(principal.getName());
        }
        return user;
    }

    public Notification saveNotification(boolean status, User likedToUser, User likedByUser) {
        Notification notification = new Notification();
        notification.setMessage(likedByUser.getUsername() + ' ' + "has liked your profile");
        notification.setDatetime_added(new Date());
        notification.setUserSender(likedByUser.getEmail());
        notification.setStatus(status);
        notification.setUserReceiver(likedToUser.getEmail());
        notificationService.saveUserVisit(notification);
        notification.setUser(likedByUser);
        return notification;
    }

    private Likes saveLike(User likedByUser, User likedToUser) {
        Likes like = new Likes();
        like.setLikedBy(likedByUser);
        like.setLikedTo(likedToUser);
        like.setStatus(true);
        return like;
    }
}
