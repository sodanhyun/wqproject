package com.codehows.wqproject.auth.oAuth;

import com.codehows.wqproject.domain.account.service.AccountService;
import com.codehows.wqproject.auth.jwt.JwtTokenProvider;
import com.codehows.wqproject.constant.enumVal.SocialType;
import com.codehows.wqproject.domain.auth.responseDto.TokenRes;
import com.codehows.wqproject.domain.auth.service.RefreshTokenService;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static com.codehows.wqproject.constant.JwtTokenConstant.*;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${domainName}")
    private String domainName;

    private final JwtTokenProvider tokenProvider;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final AccountService accountService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        SocialType type = getOAuthType(Objects.requireNonNull(WebUtils.getCookie(request, TYPE)).getValue());
        String userId = getUserId(type, attributes);
        User user = accountService.findUserById(userId);
        String accessToken = tokenProvider.createJwtToken(user.getId(), "access");
        String refreshToken = tokenProvider.createJwtToken(user.getId(), "refresh");
        refreshTokenService.updateOrSaveByUser(user, refreshToken);
        CookieUtil.addCookie(response,
                ACCESS_TOKEN_COOKIE_NAME,
                accessToken,
                ACCESS_TOKEN_DURATION);
        CookieUtil.addCookie(response,
                REFRESH_TOKEN_COOKIE_NAME,
                refreshToken,
                REFRESH_TOKEN_DURATION);
        String targetUrl = getTargetUrl(request,
                TokenRes.builder()
                        .userId(user.getId())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .userRole(user.getUserRole()).build()
        );
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request,
                                               HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
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
            return attributes.get("email") + "_" + SocialType.GOOGLE.getType();

        }else if(type== SocialType.KAKAO) {
            if(attributes.get("kakao_account") instanceof Map<?, ?> kakaoAccount)
                return kakaoAccount.get("email") + "_" + SocialType.KAKAO.getType();
        }
        return null;
    }

    private String getTargetUrl(HttpServletRequest request, TokenRes response) {
        Cookie cookie = WebUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME);
        String redirectPath = domainName + (cookie==null ? "/" : cookie.getValue());

        return UriComponentsBuilder.fromUriString(redirectPath)
                .queryParam("user_role", response.getUserRole().getType())
                .build()
                .toUriString();
    }


}
