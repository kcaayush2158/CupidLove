package com.application.springboot.service.profile;

import com.application.springboot.model.ProfileVisit;
import com.application.springboot.repository.ProfileVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileVisitService {

    @Autowired
    private ProfileVisitRepository profileVisitRepository;

    // if user profile does npt exist then it save the no of visit
    public ProfileVisit profileVisitExist(ProfileVisit profileVisit){
        return  profileVisitRepository.save(profileVisit);
    }

    public ProfileVisit countProfileVisit(String email ){
        return profileVisitRepository.countProfileVisitByProfilevisit(email);
    }

}
