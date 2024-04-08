package com.codehows.wqproject.oAuth2;

import com.codehows.wqproject.constant.CustomUserDetails;
import com.codehows.wqproject.entity.Member;
import com.codehows.wqproject.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser (OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService  = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // OAuth2 서비스 id
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
        String userNameAttributeName =
                userRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        OAuthAttributes attributes =
                OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        if (!StringUtils.hasText(attributes.getEmail())) {
            log.info("소셜 로그인 이메일 허용 안함");
            throw new RuntimeException("Email not found from OAuth2 provider");
        }
        Member member = saveOrUpdate(attributes);
        return CustomUserDetails.create(member, attributes.getAttributes());
    }


    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findById(attributes.getEmail())
                .map(entity->entity.updateMember(attributes.getName()))
                .orElse(attributes.toEntity());
        return memberRepository.save(member);
    }
}