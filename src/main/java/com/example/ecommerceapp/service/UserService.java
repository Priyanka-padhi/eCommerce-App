package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dao.UserDao;
import com.example.ecommerceapp.entity.EmailNotification;
import com.example.ecommerceapp.entity.User;
import com.example.ecommerceapp.util.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao userDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailNotificationService notificationService;

    @Value("${spring.mail.username}")
    private String adminEmail;


    public ResponseEntity<String> registerNewUser(User user) throws MessagingException {
//        if(userDao.existsById(user.getUserName())){
//            return new ResponseEntity<>("Email already exist", HttpStatus.BAD_REQUEST);
//        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);

        String subject = "E-Commerce Application | User Registration Notification";
        String email = user.getUserName();
        String msg = "Hi " + user.getFirstName() + ",\n Your account setup completed\n "+
                "wait for Approval\n";
        emailService.sendMail(email,subject,msg);
        //mail to admin
        String email2 = adminEmail;
        String msg2 = "Hi" + ",\n User account setup completed\n "+
                "waiting for your Approval\n";
        emailService.sendMail(email2,subject,msg2);
        return new ResponseEntity<>("User registered Successfully!!",HttpStatus.CREATED);
    }
    public ResponseEntity<String> sendEmailWithAttachment(EmailNotification notificationEmail)throws MessagingException{

        boolean emailSent = false;
        if (notificationEmail.getFile()!=null) {
            emailSent = notificationService.sendNotificationEmailWithAttachment(notificationEmail, notificationEmail.getFile());
        } else {
            emailSent = notificationService.sendTextNotificationEmail(notificationEmail);
        }
        if (emailSent) {
            return ResponseEntity.ok("Email sent.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while sending email.");
        }
    }



    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }
}
