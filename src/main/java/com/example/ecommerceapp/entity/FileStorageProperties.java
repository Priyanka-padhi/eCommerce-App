package com.example.ecommerceapp.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
//@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;

}
