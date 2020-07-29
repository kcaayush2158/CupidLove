package com.application.springboot.repository;

import com.application.springboot.model.ProfileVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ProfileVisitRepository extends JpaRepository<ProfileVisit,Integer>{

    //gets the total no of visited user
    ProfileVisit countProfileVisitByProfilevisit(String email);

}
