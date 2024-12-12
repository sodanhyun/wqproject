package com.goormthon.halmang.auth.user;

import com.goormthon.halmang.constant.enumVal.UserRole;
import com.goormthon.halmang.entity.User;
import com.goormthon.halmang.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
@Transactional
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) {

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
        return new UserDetail(user, getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {

        List<GrantedAuthority> authorities = new ArrayList<>();

        UserRole userRole = user.getUserRole();

        authorities.add(new SimpleGrantedAuthority(userRole.getType()));

        return authorities;
    }

}