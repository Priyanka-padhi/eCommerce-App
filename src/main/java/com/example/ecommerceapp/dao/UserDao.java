package com.example.ecommerceapp.dao;

import com.example.ecommerceapp.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User,String> {
}
