package com.goormthon.halmang.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, Duration maxAge) {
        response.addHeader(HttpHeaders.SET_COOKIE,
                ResponseCookie.from(name, value)
                        .path("/")
                        .sameSite("None")
                        .secure(true)
                        .httpOnly(true)
                        .maxAge(maxAge)
                        .build()
                        .toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies==null) return;
        for(Cookie cookie : cookies) {
            if(name.equals(cookie.getName())) {
                response.addHeader(HttpHeaders.SET_COOKIE,
                        ResponseCookie.from(name, "")
                                .path("/")
                                .sameSite("None")
                                .secure(true)
                                .httpOnly(true)
                                .maxAge(0)
                                .build()
                                .toString());
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        byte[] result = Base64.getUrlDecoder().decode(cookie.getValue());
        try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(result))) {
            return cls.cast(ois.readObject());
        }catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
