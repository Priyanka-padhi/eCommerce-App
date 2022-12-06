package com.example.ecommerceapp.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.ecommerceapp.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role,String> {

}
