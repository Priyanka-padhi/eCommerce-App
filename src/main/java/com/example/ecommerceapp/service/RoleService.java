package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dao.RoleDao;
import com.example.ecommerceapp.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role createNewRole(Role role){
       return roleDao.save(role);
    }


}
