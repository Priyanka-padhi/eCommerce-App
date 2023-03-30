package com.example.ecommerceapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {


    @Value("${jwt_secret_key}")
    private String secret_key;

    @Value("${jwt_token_validity}")
    private long TOKEN_VALIDITY;

    public String getUserNameFromToken(String token){
       return getClaimFromToken(token,Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims,T> claimResolver){

      final Claims claims =   getAllClaimsFromToken(token);
      return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String  userName = getUserNameFromToken(token);
        return  ( userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
       final Date expDate = getExpirationDateFromToken(token);
       return expDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String  token){
        return getClaimFromToken(token,Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){

        return Jwts.builder()
                .claim("Role",userDetails.getAuthorities())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS256,secret_key)
                .compact()
                ;
    }

}

/*
Spring Security Context holds the information of an authenticated user represented as an Authentication object.
In order to construct this Authentication object, we need to provide a UsernamePasswordAuthenticationToken which will later be used by our AuthenticationManager (Which we configured previously)
to Authenticate our user. To construct, we are passing along the user details as well as a collection of authorities(roles) that we parse from the JWT Token.
 */
