package com.example.securitypilot;

import com.example.securitypilot.domain.auth.Authority;
import com.example.securitypilot.domain.auth.Domain;
import com.example.securitypilot.domain.auth.DomainType;
import com.example.securitypilot.domain.auth.Menu;
import com.example.securitypilot.domain.user.User;
import com.example.securitypilot.infrastructure.auth.AuthorityRepository;
import com.example.securitypilot.infrastructure.auth.MenuRepository;
import com.example.securitypilot.infrastructure.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitConstruct implements InitializingBean {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() {
        init();
    }

    private void init() {
        Menu study = Menu.builder()
                .url("/builder/study")
                .name("과제 구축")
                .build();
        Menu crf = Menu.builder()
                .url("/builder/crf")
                .name("CRF 구축")
                .build();
        menuRepository.saveAll(List.of(study, crf));

        Menu studyInfo = Menu.builder()
                .url("/studyInfo")
                .name("과제 정보")
                .build();
        Menu crfInfo = Menu.builder()
                .url("/crfInfo")
                .name("CRF 정보")
                .build();
        Menu statistics = Menu.builder()
                .url("/statistics")
                .name("통계")
                .build();
        menuRepository.saveAll(List.of(studyInfo, crfInfo, statistics));

        Authority bmu = Authority.builder()
                .name("BMU")
                .build();
        bmu.addMenu(study);
        bmu.addMenu(crf);

        Authority dm = Authority.builder()
                .name("DM")
                .build();
        dm.addMenu(statistics);

        Authority pi = Authority.builder()
                .name("PI")
                .build();
        pi.addMenu(studyInfo);
        pi.addMenu(crfInfo);

        Authority cro = Authority.builder()
                .name("CRO")
                .build();
        cro.addMenu(studyInfo);
        cro.addMenu(crfInfo);

        authorityRepository.saveAll(List.of(bmu, dm, pi, cro));

        User builder = User.builder()
                .email("builder@gmail.com")
                .password(passwordEncoder.encode("builderPassword"))
                .name("builder")
                .build();

        User user = User.builder()
                .email("user@gmail.com")
                .password(passwordEncoder.encode("userPassword"))
                .name("user")
                .build();

        Domain builderBuilder = Domain.builder()
                .domainType(DomainType.BUILDER)
                .build();
        builderBuilder.addAuthority(bmu);
        builderBuilder.addAuthority(dm);
        builderBuilder.registerUser(builder);

        Domain builderUat = Domain.builder()
                .domainType(DomainType.UAT)
                .build();
        builderUat.addAuthority(pi);
        builderUat.registerUser(builder);

        Domain userUat = Domain.builder()
                .domainType(DomainType.UAT)
                .build();
        userUat.addAuthority(cro);
        userUat.registerUser(user);

        userRepository.saveAll(List.of(builder, user));
    }
}
