package com.application.springboot.controller;

import com.application.springboot.service.AmazonService;
import com.application.springboot.service.UserService;
import com.application.springboot.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "*")
public class FileUploadController {

    private final AmazonService amazonService;
    @Autowired
    private PhotoService photoService;

    @Autowired
    FileUploadController(AmazonService amazonService) {
        this.amazonService = amazonService;
    }

    @Autowired
    private UserService userService;

    @GetMapping("/profile/pic/upload")
    public String filePage(Model model) {
        return "/imageupload/FileUpload";
    }

    @GetMapping("/profile/pic/success")
    public String filePage2(Model model) {
        return "/imageupload/uploadStatusView";
    }




}
