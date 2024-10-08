package com.codehows.wqproject.auth.jwt;

import com.codehows.wqproject.constant.JwtProperties;
import com.codehows.wqproject.domain.auth.service.RefreshTokenService;
import com.codehows.wqproject.entity.RefreshToken;
import com.codehows.wqproject.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static com.codehows.wqproject.constant.JwtTokenConstant.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final JwtProperties jwtTokenProperties;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private Key secretAccessKey;
    private Key secretRefreshKey;

    @PostConstruct
    protected void init() {
        byte[] secretAccessKeyBytes = Decoders.BASE64.decode(jwtTokenProperties.getSecretAccessKey());
        byte[] secretRefreshKeyBytes = Decoders.BASE64.decode(jwtTokenProperties.getSecretRefreshKey());

        secretAccessKey = Keys.hmacShaKeyFor(secretAccessKeyBytes);
        secretRefreshKey = Keys.hmacShaKeyFor(secretRefreshKeyBytes);
    }

    public String createJwtToken(String userId, String type) {
        Date now = new Date();
        Date expiry;
        JwtBuilder jwtBuilder;
        if(type.equals("access")) {
            expiry = new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis());
            jwtBuilder = Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setIssuer(jwtTokenProperties.getIssuer())
                    .setIssuedAt(now)
                    .setExpiration(expiry)
                    .setSubject(userId)
                    .signWith(secretAccessKey, SignatureAlgorithm.HS512);
        }else{ // if (type.equals("refresh"))
            expiry = new Date(now.getTime() + REFRESH_TOKEN_DURATION.toMillis());
            jwtBuilder = Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setIssuer(jwtTokenProperties.getIssuer())
                    .setIssuedAt(now)
                    .setExpiration(expiry)
                    .setSubject(userId)
                    .signWith(secretRefreshKey, SignatureAlgorithm.HS512);
        }
        return jwtBuilder.compact();
    }

    public String validateAccessToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(secretAccessKey)
                    .build()
                    .parseClaimsJws(token);
            return "success";
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return "expired";
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return "tampered";
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return null;
        }
    }

    public String validateRefreshToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(secretRefreshKey)
                    .build()
                    .parseClaimsJws(token);
            Authentication authentication = getAuthentication(getUserIdInRefreshToken(token));
            RefreshToken refreshToken = refreshTokenService.findByUserId(authentication.getName());
            if(refreshToken == null || !refreshToken.getValue().equals(token)) {
                throw new JwtException("토큰의 유저 정보와 일치하지 않습니다.");
            }
            return "success";
        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return "expired";
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return "tampered";
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return null;
        }
    }

    public String getUserIdInAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretAccessKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getUserIdInRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretRefreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Authentication getAuthentication(String userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String refresh(String userId, String newRefreshToken) {
        return refreshTokenService.updateRefreshToken(userId, newRefreshToken).toString();
    }

}
