package com.application.springboot.repository;

import com.application.springboot.model.AboutMe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AboutMeRepository extends JpaRepository<AboutMe, Integer> {

    AboutMe findById(int id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE AboutMe a set a.liveIn =:#{#aboutMe.liveIn},a.country =:#{#aboutMe.country},a.drink=:#{#aboutMe.drink} , a.bodyType = :#{#aboutMe.bodyType} , a.education =:#{#aboutMe.education} , a.eyes=:#{#aboutMe.eyes} ,a.gender=:#{#aboutMe.gender} ,a.hair=:#{#aboutMe.hair} ,a.haveKids=:#{#aboutMe.haveKids} , a.height=:#{#aboutMe.height} , a.known=:#{#aboutMe.known} , a.languages=:#{#aboutMe.languages},a.lookingFor=:#{#aboutMe.lookingFor},a.relationship=:#{#aboutMe.relationship},a.smoke=:#{#aboutMe.smoke},a.workAs=:#{#aboutMe.workAs} where a.id=:id")
    void updateAboutMe(AboutMe aboutMe, Integer id);


}
