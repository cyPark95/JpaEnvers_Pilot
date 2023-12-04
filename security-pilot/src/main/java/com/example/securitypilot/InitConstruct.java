package com.example.securitypilot;

import static com.example.securitypilot.domain.auth.Domain.BUILDER;
import static com.example.securitypilot.domain.auth.Domain.UAT;

import com.example.securitypilot.domain.auth.Authority;
import com.example.securitypilot.domain.auth.DomainAuthority;
import com.example.securitypilot.domain.auth.Menu;
import com.example.securitypilot.domain.user.User;
import com.example.securitypilot.infrastructure.user.UserRepository;
import com.example.securitypilot.infrastructure.auth.AuthorityRepository;
import com.example.securitypilot.infrastructure.auth.MenuRepository;
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
    public void afterPropertiesSet() throws Exception {
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

        Authority dm = Authority.builder()
                .name("DM")
                .build();
        dm.addMenu(statistics);

        Authority pi = Authority.builder()
                .name("PI")
                .build();
        pi.addMenu(studyInfo);
        pi.addMenu(crfInfo);
        pi.addMenu(statistics);

        Authority cro = Authority.builder()
                .name("CRO")
                .build();
        cro.addMenu(studyInfo);
        cro.addMenu(crfInfo);

        authorityRepository.saveAll(List.of(dm, pi, cro));

        User builder = User.createBuilder(
                "builder@gmail.com",
                passwordEncoder.encode("builderPassword"),
                "builder"
        );

        User user = User.createUser(
                "user@gmail.com",
                passwordEncoder.encode("userPassword"),
                "user"
        );

        DomainAuthority builderDm = DomainAuthority.builder()
                .domain(BUILDER)
                .authority(dm)
                .build();

        DomainAuthority builderCro = DomainAuthority.builder()
                .domain(BUILDER)
                .authority(cro)
                .build();

        DomainAuthority uatPi = DomainAuthority.builder()
                .domain(UAT)
                .authority(pi)
                .build();

        builderDm.registerUser(builder);
        builderCro.registerUser(user);
        uatPi.registerUser(builder);

        userRepository.saveAll(List.of(builder, user));
    }
}
