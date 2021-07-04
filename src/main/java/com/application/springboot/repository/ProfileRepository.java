package com.application.springboot.repository;

import com.application.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository  extends JpaRepository<User,Integer> {

    @Query("select u from  User  u where u.id>=:initialId and u.id<=:endId  ")
    List<User> getUniqueUsers(int initialId ,int endId);

}
