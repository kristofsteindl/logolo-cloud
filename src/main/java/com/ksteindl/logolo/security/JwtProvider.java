package com.ksteindl.logolo.security;

import com.ksteindl.logolo.domain.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    public static final Long EXPIRATION_TIME_IN_MILLIS = 1800_000l;
    public static final String SECRET = "SecretKeyToGenJWTs";

    // Generate the token
    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_IN_MILLIS);
        String userId = Long.toString(user.getId());

        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();


    }

    // Validate the token
//    public boolean validateToken(String token){
//        try {
//            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
//            return true;
//        } catch (SignatureException signatureException) {
//            logger.info("Invalid JWT Signature");
//        } catch (MalformedJwtException malformedJwtException) {
//            logger.info("Invalid JWT token");
//        } catch (ExpiredJwtException expiredJwtException) {
//            logger.info("Expired JWT token");
//        } catch (UnsupportedJwtException unsupportedJwtException) {
//            logger.info("Unsopported JWT token");
//        } catch (IllegalArgumentException illegalArgumentException) {
//            logger.info("JWT claims string is emptry");
//        }
//        return false;
//    }

    // TODO maybe this and the upper method can be merged
    // Get userId from token
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");
        return  Long.parseLong(id);
    }

}
