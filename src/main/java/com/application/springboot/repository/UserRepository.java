package com.application.springboot.repository;

import com.application.springboot.model.AboutMe;
import com.application.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Async
    Optional<User> findByEmail(String email);

    User getAllByEmail(String email);
    /*
    *
    * */
    @Modifying
    @Query("SELECT email,password from User")
    List<User> getUsernameAndPassword();

    //When the user visits the  other user profile
    User findByUsername(String username);//UserService

    // FOR THE THE PROFILE PAGES

    /* 3.
    Status:working perfectly ,ProfileController
    gets all the information from the db for the visit profile page ->
    */
    User getAllByUsername(String username);

    // Working : Updating the user profile password in the settings
    @Modifying
    @Transactional
    @Query(value = "update User u set u.password = :password where u.email = :email")
    void changeUserPassword(@RequestParam("password") String password, String email);

    @Modifying
    @Transactional
    @Query(value = "update User u set u.profile_visit = ?1 where u.email = ?2")
    void updateProfileVisit(Integer id, String email);

    /* User can able to search the user in the navbar section
    *   @param("username") // gets the username from the header section
    */
    @Query(value = "from User u where u.username like :username%")
    List<User> searchByUsername(@Param("username") String username);

    // returns the total of user in the search
    int countUserByUsernameStartingWith(@Param("username") String username);

    @Query(value = "from  User  u where u.email= :email")
    User findExistingEmail(@Param("email") String email);

    //deletes the user profile permanently
    // @Param email
    @Transactional
    @Modifying
    @Query("DELETE from User u WHERE u.email= :email ")
    void deleteByEmail(@Param("email") String email);

    //search the user profile  in the navbar
    // @Param
    @Transactional
    @Modifying
    @Query(value = " from User u where u.aboutMe.age >=  :fromAge and u.aboutMe.age <= :toAge and u.aboutMe.gender = :gender and u.aboutMe.country=:country" )
    List<User> searchUser(@Param("gender") String gender, @Param("fromAge") int fromAge, @Param("toAge") int toAge,@Param("country")String country);


    int countUsersByAboutMeGenderAndAboutMeAgeIsGreaterThanEqualAndAboutMeAgeIsLessThanEqualAndAboutMe_Country(@Param("gender") String gender, @Param("fromAge") int fromAge, @Param("toAge") int toAge,@Param("country")String country);

    // able to insert the user profile in the settings
    @Transactional
    @Modifying
    @Query(value="Update User u SET u.profilePhoto = :link where u.email =:email")
    int insertUserProfilePicture(@Param("link") String link,@Param("email") String email);
    //counts the total no of user profiles
    @Query("select count(u) from User u")
    int getAllUserProfiles();

    User findUserById(int id);


}
