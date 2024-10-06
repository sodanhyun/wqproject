package com.codehows.wqproject.auth.user;

import com.codehows.wqproject.constant.enumVal.UserRole;
import com.codehows.wqproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDetail implements OAuth2User, UserDetails, Serializable {
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;
    @Setter
    private Map<String, Object> attributes;

    public static UserDetail create(User member) {
        List<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("" + UserRole.USER));
        return new UserDetail(
                member,
                authorities,
                null
        );
    }

    public static UserDetail create(User member, Map<String, Object> attributes) {
        UserDetail customUserDetails = UserDetail.create(member);
        customUserDetails.setAttributes(attributes);
        return customUserDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() {return user.getPassword(); }

    @Override
    public String getName() { return user.getId(); }

    @Override
    public String getUsername() { return user.getId(); }

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
    public boolean isEnabled() {return true;}
}
