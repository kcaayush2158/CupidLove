package com.application.springboot.service;

import com.application.springboot.model.User;
import com.application.springboot.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public List<User> getUniquerUsersForNavigation(int initialId,int  endId){
       return profileRepository.getUniqueUsers(initialId,endId);
    }

}
