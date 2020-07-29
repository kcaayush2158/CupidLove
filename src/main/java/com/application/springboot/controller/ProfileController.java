package com.application.springboot.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.application.springboot.model.AboutMe;
import com.application.springboot.model.ProfileVisit;
import com.application.springboot.model.User;
import com.application.springboot.model.notification.Notification;
import com.application.springboot.repository.AboutMeRepository;
import com.application.springboot.repository.ProfileVisitRepository;
import com.application.springboot.repository.UserRepository;
import com.application.springboot.service.*;
import com.application.springboot.service.profile.ProfileVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Basic;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class ProfileController {

    @Autowired
    private AboutMeService aboutMeService;
    @Autowired
    private AmazonService amazonService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AboutMeRepository aboutMeRepository;
    @Autowired
    private ProfileVisitRepository profileVisitRepository;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ProfileVisitService profileVisitService;
    private AmazonS3 s3client;

    @Value("${bucketName}")
    private String bucketName;
    @Value("${endpointUrl}")
    private String endPointurl;
    @Value("${ACCESS-KEY}")
    private String accessKey;
    @Value("${SECRET-KEY}")
    private String secretKey;
    @Autowired
    private NotificationService notificationService;

    /*
    Working correctly ,
    Repo:-
    * Page userprofile page
    * */
    @GetMapping(value = "/user/profile/{username}")
    public ModelAndView showUserProfiles(@PathVariable("username") String username, Model model, ProfileVisit profileVisit, Principal principal) {
        ModelAndView modelAndView = new ModelAndView("userprofile");
        User user = userService.getAllWithUsername(username);
        User currentloginInformation = userService.getAllByEmail(principal.getName());

        // save the notification if the principal visit other profiles
        Notification notification = new Notification();
        notification.setUserSender(principal.getName());
        notification.setUserReceiver(user.getEmail());
        notification.setMessage(currentloginInformation.getFirstName() + " " + currentloginInformation.getLastName() + " visited your profile");
        String dateStart = currentloginInformation.getCreatedDate().toString();
        String dateStop = new Date().toString();

//        // Custom date format
//        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
//
//        convertIntervalOfDate(dateStart, dateStop, format);
//
//        notification.setDatetime_added(new Date());
//        notification.setStatus(false);
        if (user.getEmail() != currentloginInformation.getEmail()) {
            notificationService.saveUserVisit(notification);
        }
        model.addAttribute("currentUser", currentloginInformation);
        User result = userService.findByUsername(username);

        model.addAttribute("users", result);
        model.addAttribute("aboutme", user);
        return modelAndView;
    }



    /* 3.
       Controller:
       Page: UserProfile
      Find the about me information from the current username
      Status:working
  */
    @GetMapping("/profile")
    public String myProfile(Model model, Principal principal, ProfileVisit profileVisit) {
        List<Notification> notification = notificationService.getNotification(principal.getName());
        User user2 = userService.getAllByEmail(principal.getName());
        if (principal.getName().equals(user2.getEmail())) {

            for (Notification notify : notification) {
                model.addAttribute("newNotification", notificationService.countNewMessages(false, notify.getUserReceiver()));
                model.addAttribute("notifications", notification);
                User user = userService.findByUsername(user2.getUsername());
                model.addAttribute("users", user);
            }
        }
    return "profile";
    }

    @PostMapping("/aboutme/save")
    public String saveUserAboutMe(
            @RequestParam("liveIn") String livein,
            @RequestParam("age") String age,
            @RequestParam("bio") String bio,
            @RequestParam("drink") String drink,
            @RequestParam("bodyType") String bodyType,
            @RequestParam("education") String education,
            @RequestParam("eyes") String eyes,
            @RequestParam("gender") String gender,
            @RequestParam("country") String country,
            @RequestParam("hair") String hair,
            @RequestParam("haveKids") String havekids,
            @RequestParam("height") String height,
            @RequestParam("known") String known,
            @RequestParam("languages") String languages,
            @RequestParam("lookingFor") String lookingFor,
            @RequestParam("relationship") String relationship,
            @RequestParam("smoke") String smoke,
            @RequestParam("workAs") String workAs,

            Principal principal, AboutMe aboutMe,
            Model model

    ) {
        User user = userService.getAllByEmail(principal.getName());
        aboutMe.setBio(aboutMe.getBio());
        aboutMe.setAge(aboutMe.getAge());
        aboutMe.setBodyType(aboutMe.getBodyType());
        aboutMe.setDrink(aboutMe.getDrink());
        aboutMe.setEducation(aboutMe.getEducation());
        aboutMe.setGender(aboutMe.getGender());
        aboutMe.setHaveKids(aboutMe.getHaveKids());
        aboutMe.setKnown(aboutMe.getKnown());
        aboutMe.setLookingFor(aboutMe.getLookingFor());
        aboutMe.setBodyType(aboutMe.getBodyType());
        aboutMe.setEyes(aboutMe.getEducation());
        aboutMe.setHeight(aboutMe.getHeight());
        aboutMe.setLiveIn(aboutMe.getLiveIn());
        aboutMe.setHair(aboutMe.getHair());
        aboutMe.setSmoke(aboutMe.getSmoke());
        aboutMe.setDrink(aboutMe.getDrink());
        aboutMe.setId(aboutMe.getId());
        model.addAttribute("users", user);
        aboutMeRepository.updateAboutMe(aboutMe, user.getId());
        return "redirect:/profile";
    }

    //search the user in the nav
    // @param
    @GetMapping("/search/user")
    public String searchUser(@RequestParam("fromAge") int fromAge, @RequestParam("toAge") int toAge, @RequestParam("gender") String gender,@RequestParam("country") String country,Model model,Principal principal) {
        List<User> results = userService.searchUserProfile(gender,fromAge,toAge,country );
        int totalSearchedUser = userService.countUserProfile(gender,fromAge,toAge,country);
        int totalUser = userService.countTotalUser(principal.getName());
     for (User re: results){
         model.addAttribute("totalSearchedUser",totalSearchedUser);
         model.addAttribute("message", "Search Results");
         model.addAttribute("users", results);
         model.addAttribute("totalUsers",totalUser);
         model.addAttribute("message", "Search Results , "+ totalSearchedUser);
         return "search";
     }
        model.addAttribute("result", "User doesn't found");
        model.addAttribute("message", "Search Results , "+ totalSearchedUser);
        return "search";
    }



    @GetMapping("/list/bucket")
    public @ResponseBody
    String listAllBucketFiles(Model model, Principal principal ){
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(principal.getName()+"/").withDelimiter("/");

        ObjectListing objects = s3client.listObjects(listObjectsRequest);

            List<S3ObjectSummary> summaries = objects.getObjectSummaries();

            for (S3ObjectSummary item : summaries) {
                System.out.println(" -> " + item.getKey() + "  " + "(size = " + item.getSize()/1024 + " KB)");

            }
//                System.out.println("https://user-photo-videos.s3.amazonaws.com/" + item.getKey()+ principal.getName());
//                model.addAttribute("photos","https://user-photo-videos.s3.amazonaws.com/" + item.getKey()+ principal.getName());
        return "";
    }


    private void convertIntervalOfDate(String dateStart, String dateStop, SimpleDateFormat format) {
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get msec from each, and subtract.
        long diff = d2.getTime() - d1.getTime();

        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long remainingHoursInMillis = diff - TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(remainingHoursInMillis);
        long remainingMinutesInMillis = remainingHoursInMillis - TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMinutesInMillis);
        long remainingSecondsInMillis = remainingMinutesInMillis - TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingSecondsInMillis);
    }



}
