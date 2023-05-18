package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dao.UserDao;
import com.example.ecommerceapp.entity.EmailNotification;
import com.example.ecommerceapp.entity.User;
import com.example.ecommerceapp.util.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
@Slf4j
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

    private final String PATH = "/home/dell/Documents/";




    public ResponseEntity<String> registerNewUser(User user, MultipartFile file) throws IOException {
//        if(userDao.existsById(user.getUserName())){
//            return new ResponseEntity<>("Email already exist", HttpStatus.BAD_REQUEST);
//        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
        uploadFile(file,user);

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

    public void uploadFile(MultipartFile file,User user)throws IOException {
        String name = user.getUserName();
        String path = PATH + name;
        File dirName = new File(path);
        boolean dir = dirName.mkdir();
        System.out.println(dir);
        String fullPath = path + "/" + file.getOriginalFilename();
        user.setFilePath(fullPath);
        file.transferTo(new File(fullPath));
        userDao.save(user);
    }

    public void exportToCsv(Writer writer) throws IOException {

        List<User> users =  userDao.findAll();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Email", "FirstName", "LastName");
            for (User user : users) {
                csvPrinter.printRecord(user.getUserName(),user.getFirstName(),user.getLastName());
            }
        } catch (IOException e) {

            log.error("Error While writing CSV ", e);
        }

    }

    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }
}
