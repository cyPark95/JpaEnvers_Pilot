package com.example.securitypilot.common.security.token;

import com.example.securitypilot.domain.auth.Authority;
import com.example.securitypilot.domain.auth.Domain;
import com.example.securitypilot.domain.user.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@ToString
public class PrincipalDetails implements UserDetails {

    private final User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Domain domain = user.getDomains().stream()
                .findFirst().orElseThrow(RuntimeException::new);
        List<String> authorityNames = domain.getAuthorities().stream()
                .map(Authority::getName)
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

    public boolean isAccessUrl(String requestUrl) {
        return user.getDomains().stream()
                .flatMap(domainAuthority -> domainAuthority.getAuthorities().stream())
                .flatMap(authority -> authority.getMenus().stream())
                .anyMatch(menu -> menu.isMatch(requestUrl));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof PrincipalDetails that) {
            return Objects.equals(user.getId(), that.user.getId());
        }
        if (o instanceof User that) {
            return Objects.equals(user.getId(), that.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }
}
