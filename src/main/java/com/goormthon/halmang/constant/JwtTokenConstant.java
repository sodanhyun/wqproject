package com.goormthon.halmang.constant;

import java.time.Duration;

public class JwtTokenConstant {

    //oAuth
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String TYPE = "type";
    public static final String USER_ROLE = "user_role";

    //JWT
    public static final String ACCESS_TOKEN_COOKIE_NAME = "X-AUTH_TOKEN";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "X-REFRESH_TOKEN";
    public static final String HEADER_AUTHORIZATION = "authorization";
    public static final Duration OAUTH_COOKIE_EXPIRE = Duration.ofMinutes(5);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofHours(12);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofMinutes(15);
}
