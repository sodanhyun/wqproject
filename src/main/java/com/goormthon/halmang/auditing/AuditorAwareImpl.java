package com.goormthon.halmang.auditing;

import com.goormthon.halmang.auth.user.UserDetail;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == "anonymousUser"){
            return null;
        }

        return Optional.of(((UserDetail) authentication.getPrincipal()).getUsername());
    }
}
