package com.example.ecommerceapp.config;

import com.example.ecommerceapp.service.JwtService;
import com.example.ecommerceapp.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtService jwtService;

    @Value("${jwt_header_string}")
    public String HEADER_STRING;

    @Value("${jwt_token_prefix}")
    public String TOKEN_PREFIX;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {


        final String header = httpServletRequest.getHeader(HEADER_STRING);

        String jwtToken = null;
        String  userName = null;

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            jwtToken = header.replace(TOKEN_PREFIX,"");

            try {

               userName = jwtUtil.getUserNameFromToken(jwtToken);

            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get Jwt Token");
            } catch (ExpiredJwtException e) {
                System.out.println("Jwt Token is expired");
            }
        } else {
            System.out.println("JWT Token does not start with Bearer");
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = jwtService.loadUserByUsername(userName);

                if (jwtUtil.validateToken(jwtToken, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    jwtToken, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    logger.info("Authenticated user "+ userName+ " setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}

/*

JwtRequestFilter which will filter out requests that have JWT as header and translate that to something Spring Security can understand using the methods
from the Token Provider we just created. This extends the OncePerRequestFilter meaning it's going to look for the JWT token in every single request and update the SecurityContext.
 */