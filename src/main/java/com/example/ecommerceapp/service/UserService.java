package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dao.UserDao;
import com.example.ecommerceapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserDao userDao;

    public User registerNewUser(User user){
        return userDao.save(user);
    }


}
