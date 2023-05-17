package com.example.ecommerceapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors();
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login","/registerUser","/createNewRole")
                .permitAll()
                .antMatchers(HttpHeaders.ALLOW).permitAll()
                .antMatchers("/forAdmin","/sendMail")
                .hasRole("Admin")
                .antMatchers("/forUser")
                .hasRole("User")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ;
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

    }
}

/*
Authentication:-
Now Spring Security uses something called an AuthenticationManager to validate if a given user has the right credentials (based on username and password).
In fact, the AuthenticationManager Interface has exactly one method authenticate which is called to verify if the username and password provided by a user are truthy.

But the AuthenticationManager needs to know where the user’s username and password have been stored.

That is why we override the configure method where Spring will pass an AuthenticationManagerBuilder.

The AuthenticationManagerBuilder accepts a custom implementation of the UserDetailsService interface (which we will implement when we are building our services).
Also at this stage, if we are using some form of encryption to store our password in the database, the AuthenticationManager needs to know about that as well.
It’s actually a very bad idea to store a password as plaintext. Here, we will be using BCrypt to encode our passwords.

The AuthenticationManager we just configured is added to the Spring Application Context and is added as a bean by overriding the authenticationManagerBean method.

Authorization:-

To set up Authorization, we again need to provide the configuration by overriding the configure method,
where we are passed a reference to the default HttpSecurity configuration.

Here we are configuring such that we will require authentication for all requests,
with the exception of, /register & /login (We require those two endpoints to be available to all users to sign-up or login).

For the graceful handling of Unauthorized requests, we pass along a class that implements AuthenticationEntryPoint. We will return a 401 Unauthorized when we encounter an exception.

Because we are using JWT to store roles, we need to translate that into something that Spring Security can understand.
The JWT Token needs to be parsed to fetch roles that the SpringSecurityContext needs to become aware of before it goes on to check if the API’s permissions will allow it. Hence, we pass along the JwtAuthenticationFilter.

 */