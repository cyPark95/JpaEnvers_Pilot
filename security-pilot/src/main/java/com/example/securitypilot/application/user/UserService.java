package com.example.securitypilot.application.user;

import com.example.securitypilot.domain.auth.DomainType;
import com.example.securitypilot.infrastructure.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final DomainType domainType;

    public UserService(
            UserRepository userRepository,
            @Value("${domain}") String domain
    ) {
        this.userRepository = userRepository;
        this.domainType = DomainType.valueOf(domain);
    }
}
