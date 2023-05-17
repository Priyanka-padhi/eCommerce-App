package com.example.ecommerceapp.controller;


import com.example.ecommerceapp.entity.User;
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

        @PostMapping("/registerUser")
        public ResponseEntity<String> registerNewUser(@RequestBody User user) throws MessagingException {

            return userService.registerNewUser(user);
        }
    @GetMapping("/sendMail")
    public ResponseEntity<String> sendMail() throws MessagingException {

        return userService.sendEmailWithAttachment();
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
