package com.application.springboot.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.application.springboot.model.Photos;
import com.application.springboot.model.User;
import com.application.springboot.service.photo.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private PhotoService photoService;
    @Autowired
    private UserService userService;

    /*This function uses the basic authentication in AWS
     *
     * */
    @PostConstruct
    public void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    // This method is used to upload the user profile photo
    public void uploadFile(MultipartFile multipartFile, String email) {
        try {
            File convertMultiPartToFile = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            uploadFileToS3Bucket(fileName, convertMultiPartToFile, email);
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(email + "/photos/").withDelimiter("/");
            ObjectListing objectListing;

            do {
                objectListing = s3client.listObjects(listObjectsRequest);

                for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                    userService.changeUserProfilePicture("https://user-photo-videos.s3.amazonaws.com/" + objectSummary.getKey(), email);
                }
            } while (objectListing.isTruncated());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deletePhoto(String object) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        try {
            s3.deleteObject(bucketName, object);

        } catch (AmazonServiceException e) {
            e.getMessage();
        }

    }


    // This method is used to upload the photo in the amazon and the db
    public void uploadUserImages(MultipartFile multipartFile, String email) throws IOException {
        File convertMultiPartToFile = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(multipartFile);
        uploadFileToS3Bucket(fileName, convertMultiPartToFile, email);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
            .withBucketName(bucketName)
            .withPrefix(email + "/photos/").withDelimiter("/");

        User user = userService.findExistingEmail(email);

        Photos photos = new Photos();
        photos.setPhotoType("PUBLIC");
        photos.setPhotoUrl("https://user-photo-videos.s3.amazonaws.com/" + email + "/photos/" + fileName);
        photos.setPrincipalName(user);
        photoService.savePhotos(photos);




    }

    private void uploadFileToS3Bucket(String fileName, File file, String email) {
        s3client.putObject(new PutObjectRequest(bucketName, email + "/photos/" + fileName, file)
            .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    private String generateFileName(MultipartFile multipartFile) {
        return new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace("", "_");
    }

    //
    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File convertFiles = new File(multipartFile.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(convertFiles);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return convertFiles;
    }


}
