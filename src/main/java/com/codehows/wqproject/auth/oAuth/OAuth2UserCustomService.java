package com.codehows.wqproject.auth.oAuth;

import com.codehows.wqproject.constant.enumVal.UserRole;
import com.codehows.wqproject.constant.enumVal.SocialType;
import com.codehows.wqproject.entity.User;
import com.codehows.wqproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        if(userRequest.getClientRegistration().getClientName().equals("kakao")) {
            saveOrUpdateForKakao(user);
        }else if(userRequest.getClientRegistration().getClientName().equals("google")) {
            saveOrUpdateForGoogle(user);
        }
        return user;
    }

    private void saveOrUpdateForKakao(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        if(attributes.get("kakao_account") instanceof Map<?, ?> kakaoAccount &&
                kakaoAccount.get("profile") instanceof Map<?, ?> kakaoProfile) {
            String email = (String) kakaoAccount.get("email");
            String name = (String) kakaoProfile.get("nickname");
            User user = userRepository.findById(email + "_" + SocialType.KAKAO.getType())
                    .map(entity -> entity.updateName(name))
                    .orElse(User.builder()
                            .email(email)
                            .name(name)
                            .userRole(UserRole.USER)
                            .socialType(SocialType.KAKAO)
                            .build());
            userRepository.save(user);
        }
    }

    private void saveOrUpdateForGoogle(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User user = userRepository.findById(email + "_" + SocialType.GOOGLE.getType())
                .map(entity -> entity.updateName(name))
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .userRole(UserRole.USER)
                        .socialType(SocialType.GOOGLE)
                        .build());
        userRepository.save(user);
    }
}
