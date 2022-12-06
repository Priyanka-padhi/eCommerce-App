package com.example.ecommerceapp.service;


import com.example.ecommerceapp.dao.UserDao;
import com.example.ecommerceapp.entity.JwtRequest;
import com.example.ecommerceapp.entity.JwtResponse;
import com.example.ecommerceapp.entity.User;
import com.example.ecommerceapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtUtil jwtUtil;


    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String password = jwtRequest.getPassword();

        authenticate(userName,password);
        final UserDetails userDetails = loadUserByUsername(userName);
        String  newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user =   userDao.findById(userName).get();
         return  new JwtResponse(user, newGeneratedToken);

    }
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDao.findById(userName).get();

        if(user != null){
            return new org.springframework.security.core.userdetails.User(user.getUserName(),
                    user.getPassword(),
                    getAuthorities(user));
        }else{

            throw new UsernameNotFoundException("UserName not Found");
        }
    }

    private Set getAuthorities(User user){
        Set authorities = new HashSet<>();

        user.getRole().forEach(role ->{
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });

        return authorities;
    }

    private void authenticate(String  userName, String password) throws Exception {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,password));
        }catch (DisabledException e){
            throw new Exception("user is disabled");
        }catch (BadCredentialsException e){
            throw new Exception("Bad Credential from User");
        }

    }

}
