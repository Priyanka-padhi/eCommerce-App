package com.example.ecommerceapp.controller;


import com.example.ecommerceapp.entity.User;
import com.example.ecommerceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerUser")
    public User registerNewUser(@RequestBody User user){

        return userService.registerNewUser(user);
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
