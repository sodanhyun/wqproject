package com.codehows.wqproject.auth.jwt;

import com.codehows.wqproject.constant.JwtProperties;
import com.codehows.wqproject.entity.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.codehows.wqproject.constant.JwtTokenConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final JwtProperties jwtTokenProperties;
    private final UserDetailsService userDetailsService;

    public String createJwtToken(User user, String type) {
        Date now = new Date();
        Date expiry;
        if(type.equals("access")) {
            expiry = new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis());
        }else {
            expiry = new Date(now.getTime() + REFRESH_TOKEN_DURATION.toMillis());
        }
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtTokenProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtTokenProperties.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser()
                    .setSigningKey(jwtTokenProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        }catch(Exception e) {
            log.info("유효하지 않는 JWT 토큰입니다.");
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtTokenProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getClaims(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

}
