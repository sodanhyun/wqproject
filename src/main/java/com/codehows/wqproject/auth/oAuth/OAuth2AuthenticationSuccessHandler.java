package com.codehows.wqproject.auth.oAuth;

import com.codehows.wqproject.auth.jwt.RefreshTokenService;
import com.codehows.wqproject.constant.SocialType;
import com.codehows.wqproject.auth.jwt.JwtTokenProvider;
import com.codehows.wqproject.domain.auth.responseDto.TokenResponse;
import com.codehows.wqproject.domain.auth.service.AuthService;
import com.codehows.wqproject.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.Cookie;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static com.codehows.wqproject.auth.jwt.JwtTokenConstant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${domainName}")
    private String domainName;

    private final AuthService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository cookieRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        SocialType type = getOAuthType(Objects.requireNonNull(WebUtils.getCookie(request, TYPE)).getValue());
        String userId = getUserId(type, attributes);
        User user = userService.findById(userId);
        String refreshToken = refreshTokenService.createNewToken(user);
        addRefreshTokenToCookie(request, response, refreshToken);
        String accessToken = jwtTokenProvider.createAccessToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(request, new TokenResponse(accessToken, user.getRole().getKey()), authentication);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private SocialType getOAuthType(String type) {
        if(type.equals("kakao"))
            return SocialType.KAKAO;
        if(type.equals("google"))
            return SocialType.GOOGLE;
        return null;
    }

    private String getUserId(SocialType type, Map<String, Object> attributes) {
        if(type== SocialType.GOOGLE) {
            return attributes.get("email") + "_" + SocialType.GOOGLE.getKey();

        }else if(type== SocialType.KAKAO) {
            if(attributes.get("kakao_account") instanceof Map<?, ?> kakaoAccount)
                return kakaoAccount.get("email") + "_" + SocialType.KAKAO.getKey();
        }
        return null;
    }

    private String getTargetUrl(HttpServletRequest request, TokenResponse response, Authentication authentication) {
        Cookie cookie = WebUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME);

        return UriComponentsBuilder.fromUriString(cookie==null ? "/" : cookie.getValue())
                .queryParam("access_token", response.getAccessToken())
                .queryParam("user_id", authentication.getName())
                .queryParam("role", "TEMP")
                .build()
                .toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieRepository.removeAuthorizationRequestCookies(request, response);
    }

}
