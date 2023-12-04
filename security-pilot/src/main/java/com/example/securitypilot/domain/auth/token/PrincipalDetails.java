package com.example.securitypilot.domain.auth.token;

import com.example.securitypilot.domain.user.User;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public record PrincipalDetails (User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> authorityNames = user.getDomainAuthorities().stream()
                .map(domainAuthority -> domainAuthority.getAuthority().getName())
                .toList();
        return AuthorityUtils.createAuthorityList(authorityNames);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

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
        return true;
    }

//    public List<Menu>
}
