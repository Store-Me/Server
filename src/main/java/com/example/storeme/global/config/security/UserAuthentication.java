package com.example.storeme.global.config.security;

import com.example.storeme.fo_domain.user.constant.RoleType;
import com.example.storeme.fo_domain.user.domain.User;
import com.example.storeme.global.common.dto.JwtUserDto;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {
    private final Long userId;
    public UserAuthentication(JwtUserDto jwtUserDto) {
        super(getAuthorities(jwtUserDto.getRoleType()));
        this.userId= Long.valueOf(jwtUserDto.getUserId());
    }

    private static List<GrantedAuthority> getAuthorities(RoleType roleType) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleType.name()));
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
