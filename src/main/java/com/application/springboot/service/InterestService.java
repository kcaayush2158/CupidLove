package com.application.springboot.service;

import com.application.springboot.model.Interests;
import com.application.springboot.model.User;
import com.application.springboot.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

    public List<Interests> findAll(){
        return  this.interestRepository.findAll();
    }

    public List<Interests> findInterests(User user){
        return  this.interestRepository.findInterestsByUser( user);
    }
    public Interests findInterest(int id){
        return  this.interestRepository.findInterestsById( id);
    }
    public void deleteInterest(int id){
        this.interestRepository.deleteInterestsById(id);
    }

    public Interests saveInterest(Interests interest){
        return  this.interestRepository.save(interest);
    }


}
