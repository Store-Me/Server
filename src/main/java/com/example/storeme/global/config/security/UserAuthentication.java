package com.example.storeme.global.config.security;

import com.example.storeme.fo_domain.user.domain.User;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;
    public UserAuthentication(User user) {
        super(getAuthorities(user));
        this.userId=user.getId();
    }

    private static List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoleType().name()));
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
