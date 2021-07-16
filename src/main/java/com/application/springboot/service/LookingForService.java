package com.application.springboot.service;

import com.application.springboot.model.LookingFor;
import com.application.springboot.repository.LookingForRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LookingForService {

    @Autowired
    public LookingForRepository lookingForRepository;

    public LookingFor save(LookingFor lookingFor){
        return lookingForRepository.save(lookingFor);
    }

    public void delete(int id){
        lookingForRepository.deleteById(id);
    }







}
