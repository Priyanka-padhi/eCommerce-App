package com.example.ecommerceapp.util;

import com.example.ecommerceapp.entity.EmailNotification;
import com.example.ecommerceapp.service.EmailNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service("emailService")
@ConfigurationProperties(prefix = "spring.mail")
public class EmailService implements EmailNotificationService {

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

    @Override
    public boolean sendTextNotificationEmail(EmailNotification notificationEmail) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(notificationEmail.getTo());
            mailMessage.setSubject(notificationEmail.getSubject());
            mailMessage.setText(notificationEmail.getBodyMsg());
            mailSender.send(mailMessage);
            return true;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendNotificationEmailWithAttachment(EmailNotification notificationEmail, MultipartFile file) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(notificationEmail.getTo());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBodyMsg());

            messageHelper.addAttachment(file.getOriginalFilename(), file);
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException ex) {
            log.info(ex.getMessage());
            return false;
        }
    }

}
