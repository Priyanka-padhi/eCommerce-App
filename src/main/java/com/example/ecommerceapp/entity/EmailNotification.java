package com.example.ecommerceapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotification {
    private String to;
    private String subject;
    private String bodyMsg;
    private MultipartFile file;
}
