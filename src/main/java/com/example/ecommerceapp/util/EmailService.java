package com.example.ecommerceapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;

@Service("emailService")
@ConfigurationProperties(prefix = "spring.mail")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

//    public void sendMailWithAttachment(String to, String subject, String body) throws MessagingException {
//        JavaMailSenderImpl sender = new JavaMailSenderImpl();
//        MimeMessage message = sender.createMimeMessage();
//        try{
//            MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
//
//            ClassPathResource img = new ClassPathResource("src/main/resources/Images/home/dell/Pictures");
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(body);
//            helper.addAttachment("krsna.jpg",img);
//            sender.send((MimeMessagePreparator) helper);
//
//
//        }catch (MessagingException e) {
//             e.getMessage();
//        }
//    }

//    public void sendMailWithAttachment(String to, String subject, String body, String fileToAttach) {
//        MimeMessagePreparator preparator = mimeMessage -> {
//            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
////            mimeMessage.setFrom(new InternetAddress("admin@gmail.com"));
//            mimeMessage.setSubject(subject);
//            mimeMessage.setText(body);
//
//            FileSystemResource file = new FileSystemResource(new File(fileToAttach));
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//            helper.addAttachment("krsna.jpg", file);
//        };
//
//        try {
//            mailSender.send(preparator);
//        } catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
//        }
//    }

//    public void sendEmailWithAttachment(String to, String subject, String body) throws MessagingException{
//        JavaMailSenderImpl sender = new JavaMailSenderImpl();
//        MimeMessage message = sender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(body);
//
//        FileSystemResource file = new FileSystemResource(new File("krsna.jpg"));
//        helper.addAttachment("krsna.jpg", file);
//
//        mailSender.send(message);
//    }
//public boolean sendNotificationEmailWithAttachment(String to, String subject, String body,
//                                                   MultipartFile file) {
//    MimeMessage mimeMessage = mailSender.createMimeMessage();
//    try {
//        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(body);
//
//        helper.addAttachment("", file);
//        mailSender.send(mimeMessage);
//        return true;
//    } catch (MessagingException ex) {
//        log.info(ex.getMessage());
//        return false;
//    }
//}
//}
}
