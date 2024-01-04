package com.example.securitypilot.application.auth;

import com.example.securitypilot.domain.auth.DomainType;
import com.example.securitypilot.common.security.token.PrincipalDetails;
import com.example.securitypilot.domain.user.User;
import com.example.securitypilot.infrastructure.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DomainType domainType;

    public AuthService(
            UserRepository userRepository,
            @Value("${domain}") String domain
    ) {
        this.userRepository = userRepository;
        this.domainType = DomainType.valueOf(domain);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithAuthorization(email, domainType)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));

        log.info("LoadUser User = {}", user);
        return new PrincipalDetails(user);
    }
}
