package com.application.springboot.repository;

import com.application.springboot.model.Interests;
import com.application.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interests,Integer> {

    List<Interests> findInterestsByUser(User user);

    Interests findInterestsById(int id);
}
