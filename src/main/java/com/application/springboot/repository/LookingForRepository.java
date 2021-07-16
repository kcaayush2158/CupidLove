package com.application.springboot.repository;


import com.application.springboot.model.LookingFor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LookingForRepository extends JpaRepository<LookingFor,Integer> {


}
