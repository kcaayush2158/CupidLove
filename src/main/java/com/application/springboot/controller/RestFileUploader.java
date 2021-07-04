package com.application.springboot.controller;

import com.application.springboot.model.Photos;
import com.application.springboot.service.AmazonService;
import com.application.springboot.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class RestFileUploader {


    private final AmazonService amazonService;
    @Autowired
    private PhotoService photoService;


    @Autowired
    RestFileUploader(AmazonService amazonService) {
        this.amazonService = amazonService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> upload(Model model, @RequestPart("file") MultipartFile files, @RequestParam(value = "email", required = false) String email) {
      amazonService.uploadFile(files, email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/p/upload")
    public  ResponseEntity<Void> uploadUserPhoto( @RequestPart("file") MultipartFile file, @RequestParam(value = "email", required = false) String email) throws IOException {
       amazonService.uploadUserImages(file, email);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/photo/delete")
    public ResponseEntity<Void> deletePhoto(@RequestBody @RequestParam("id") int id, @RequestParam(value = "email", required = false) String email) throws Exception {
        Photos photos = photoService.findPhoto(id);
        if(email.equals(photos.getPrincipalName().getEmail())){
            photoService.deletePhotos(id);
            amazonService.deletePhoto(photos.getPhotoUrl());
        }else{
            throw new Exception("You are not authorized");
        }
        return ResponseEntity.ok().build();
    }

}
