package com.codehows.wqproject.constant;

import com.codehows.wqproject.entity.Member;
import com.sun.security.auth.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.*;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements OAuth2User, UserDetails, Serializable {
    private Member member;
    private Collection<? extends GrantedAuthority> authorities;
    @Setter
    private Map<String, Object> attributes;

    public CustomUserDetails(Member member) { this.member = member; }

    public static CustomUserDetails create(Member member) {
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("" + Role.USER));
        return new CustomUserDetails(
                member,
                authorities,
                null
        );
    }

    public static CustomUserDetails create(Member member, Map<String, Object> attributes) {
        CustomUserDetails customUserDetails = CustomUserDetails.create(member);
        customUserDetails.setAttributes(attributes);
        return customUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.member.getRole().getKey()));
        return authorities;
    }

    @Override
    public String getPassword() {return member.getPassword(); }

    @Override
    public String getName() { return member.getId(); }
    @Override
    public String getUsername() { return member.getId(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //이메일이 인증되어 있고 계정이 잠겨있지 않으면 true
        return true;
    }
}
