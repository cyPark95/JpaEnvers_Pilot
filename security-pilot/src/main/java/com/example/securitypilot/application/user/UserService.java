package com.example.securitypilot.application.user;

import com.example.securitypilot.domain.auth.Domain;
import com.example.securitypilot.domain.auth.token.PrincipalDetails;
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
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Domain domain;

    public UserService(
            UserRepository userRepository,
            @Value("${domain}") String domain) {
        this.userRepository = userRepository;
        this.domain = Domain.valueOf(domain);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("LoadUser Username = {}", email);
        User user = userRepository.findByEmailWithAuthorization(email, domain)
                .orElseThrow(RuntimeException::new);
        return new PrincipalDetails(user);
    }
}
