package com.example.ecommerceapp;

import com.example.ecommerceapp.entity.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
//@EnableConfigurationProperties({FileStorageProperties.class})
public class ECommerceAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceAppApplication.class, args);
    }

}
