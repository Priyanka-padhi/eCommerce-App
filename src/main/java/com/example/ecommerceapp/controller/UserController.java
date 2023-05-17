package com.example.ecommerceapp.controller;


import com.example.ecommerceapp.entity.EmailNotification;
import com.example.ecommerceapp.entity.User;
import com.example.ecommerceapp.service.EmailNotificationService;
import com.example.ecommerceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    EmailNotificationService notificationService;

        @PostMapping("/registerUser")
        public ResponseEntity<String> registerNewUser(@RequestBody User user) throws MessagingException {

            return userService.registerNewUser(user);
        }
    @PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(@ModelAttribute EmailNotification emailNotification) throws MessagingException {

        return userService.sendEmailWithAttachment(emailNotification);
    }

    @GetMapping("/forAdmin")
    public String forAdmin(){
       return "This URL is only accessible to admin";
    }

    @GetMapping("/forUser")
    public String forUser(){
        return "This URL is only accessible to user";
    }
}
