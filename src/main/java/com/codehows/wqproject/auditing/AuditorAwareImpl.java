package com.codehows.wqproject.auditing;

import com.codehows.wqproject.auth.user.UserDetail;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
