package com.application.springboot.service;

import com.application.springboot.model.AboutMe;
import com.application.springboot.model.User;
import com.application.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public void saveUser(User employee) {
        userRepository.save(employee);
    }

    public List<User> listUser() {
        return userRepository.findAll();
    }

    public List<User> getLoginUsernameAndPassword() {
        return userRepository.getUsernameAndPassword();
    }

    public Optional<User> getAllByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //when the user visits the other user profile
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Repo:UserRepository,Controller:ProfileController
    public User getAllWithUsername(String username) {
        return userRepository.getAllByUsername(username);
    } //WORKING

    public Optional<User> findUserInOwnProfile(String email) {
        return userRepository.findByEmail(email);
    }

    public User fetchUserWithEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    //change the user profile picture
    public int changeUserProfilePicture(String link, String email) {
        return userRepository.insertUserProfilePicture(link, email);
    }

    public List<User> findOthersSimilarUsers(String gender) {
        return userRepository.findTop8ByAboutMe_Gender(gender);
    }

    public List<User> findAllUsersProfile(int limit, int start) {

        return userRepository.findAllTheUsersProfiles(limit, start);
    }


    public Optional<User> changeUserProfilePassword(String email) {
        return userRepository.findByEmail(email);
    }

    public User findExistingEmail(String email) {
        return userRepository.findExistingEmail(email);
    }

    //search the user in navbar
    public List<User> searchUserProfile(String gender, int fromAge, int toAge, String country) {
        return userRepository.searchUser(gender, fromAge, toAge, country);
    }

    //count the searched user  in navbar
    public int countUserProfile(String gender, int fromAge, int toAge, String country) {
        return userRepository.countUsersByAboutMeGenderAndAboutMeAgeIsGreaterThanEqualAndAboutMeAgeIsLessThanEqualAndAboutMe_Country(gender, fromAge, toAge, country);
    }

    //count the total no of users while searching in navbar
    public int countTotalUser(String username) {
        return userRepository.countUserByUsernameStartingWith(username);
    }

    public int getAllUserProfile() {
        return userRepository.getAllUserProfiles();
    }

    public User findUserById(int id) {
        return userRepository.findUserById(id);
    }


    public User getUserStatus(boolean status, String email) {
        return userRepository.findUserByActiveIsAndEmail(status, email);
    }

    public User updateUserStatus(boolean active, int id) {
        return userRepository.updateUserOnlineStatus(active, id);
    }

    public User fetchUserEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }


    public List<User> findUsersFromOwnCountry(String country) {

        return userRepository.findUsersFromOwnCountry(country);
    }

    public List<User> switchGenders(String gender) {
        return userRepository.switchGender(gender);
    }


    public User updateUser(User user) {

        return  userRepository.save(user);
    }
}
