package com.application.springboot.service;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;

@Service
public class AmazonService {

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
    private UserService userService;

    /*This function uses the basic authen
    *
    * */
    @PostConstruct
    public void initializeAmazon(){
        BasicAWSCredentials  credentials= new BasicAWSCredentials(this.accessKey,this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
                            .withRegion(Regions.US_EAST_1)
                            .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    public String uploadFile(MultipartFile multipartFile, Principal principal){
        String fileUrl ="";
            try {
                 File file = convertMultiPartToFile(multipartFile);
                 String fileName = generateFileName(multipartFile);
                 fileUrl = endPointurl + "/"+ bucketName + "/" + fileName;
                uploadFileToS3Bucket(fileName,file,principal);
                changeUserProfilePic(principal,null);
                Model model = null;
                file.delete();
             } catch (Exception ex) {
                ex.printStackTrace();
            }
        return "settings/settings";
    }

    private void uploadFileToS3Bucket(String fileName, File file,Principal principal) {
        s3client.putObject(new PutObjectRequest(bucketName,principal.getName()+"/"+fileName,file)
                .withCannedAcl(CannedAccessControlList.PublicRead)
            );

    }

    private String generateFileName(MultipartFile multipartFile) {
        return  new Date().getTime()+"-"+multipartFile.getOriginalFilename().replace("","_");
    }
    //
    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convFile);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return convFile;
    }



    public String  changeUserProfilePic(Principal principal,Model model){

        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(principal.getName()+"/").withDelimiter("/");
        ObjectListing objectListing;

        do{
            objectListing = s3client.listObjects(listObjectsRequest);

            for(S3ObjectSummary objectSummary: objectListing.getObjectSummaries()){
                userService.changeUserProfilePicture("https://user-photo-videos.s3.amazonaws.com/" + objectSummary.getKey(),principal.getName());

                return  "settings/settings";
            }
        }while (objectListing.isTruncated());
        return  "settings/settings";
    };



    //




}
