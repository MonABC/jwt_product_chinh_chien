package com.example.jwt_chinh_chien_product_manager.security.jwt;

import com.example.jwt_chinh_chien_product_manager.model.User;
import com.example.jwt_chinh_chien_product_manager.security.userprincal.UserPrinciple;
import com.example.jwt_chinh_chien_product_manager.service.impl.UserServiceImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private static final String SECRET_KEY = "123456789"; //ma so bi mat
    private static final Long EXPIRE_TIME = 8640000000l;

    @Autowired
    private UserServiceImpl userService;

    public String createToken(Authentication authentication) { // tao ra token
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime() + EXPIRE_TIME * 1000)) // set thoi gian ton ta cho token
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string empty -> Message: {}, e");
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT Token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expire JWT Token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT Token -< Message: {}", e);
        }
        return false;
    }

    public String getUsernameFromJwtToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return username;
    }

    public User getUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }
}
