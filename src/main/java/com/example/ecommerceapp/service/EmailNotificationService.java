package com.example.ecommerceapp.service;

import com.example.ecommerceapp.entity.EmailNotification;
import org.springframework.web.multipart.MultipartFile;

public interface EmailNotificationService {

    boolean sendTextNotificationEmail(EmailNotification notificationEmail);
    boolean sendNotificationEmailWithAttachment(EmailNotification notificationEmail,
                                                MultipartFile file);
}
