package com.example.ecommerceapp.controller;


import com.example.ecommerceapp.entity.EmailNotification;
import com.example.ecommerceapp.entity.User;
import com.example.ecommerceapp.service.EmailNotificationService;
import com.example.ecommerceapp.service.FileStorageService;
import com.example.ecommerceapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    EmailNotificationService notificationService;

    @Autowired

    private ObjectMapper objectMapper;


    @Autowired
    private FileStorageService fileStorageService;

//        @PostMapping("/registerUser")
//        public ResponseEntity<String> registerNewUser(@RequestBody User user) throws MessagingException {
//
//            return userService.registerNewUser(user);
//        }
        @PostMapping("/sendMail")
        public ResponseEntity<String> sendMail(@ModelAttribute EmailNotification emailNotification) throws MessagingException {

            return userService.sendEmailWithAttachment(emailNotification);
        }

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerNewUserWithFile(@RequestParam("file") MultipartFile file, @RequestParam("userModel") String jsonObject) throws MessagingException, IOException {

          User  user = objectMapper.readValue(jsonObject,User.class);
        return userService.registerNewUser(user, file);
    }

    //use while updating the file
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, User user) throws IOException {
            userService.uploadFile(file,user);
            return new ResponseEntity<>("File uploaded",HttpStatus.OK);
    }

    //export to csv file
    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse response) throws IOException{
            response.setContentType("text/csv");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        userService.exportToCsv(response.getWriter());
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
