package com.example.securitypilot.domain.auth;

import static jakarta.persistence.EnumType.STRING;

import com.example.securitypilot.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"user", "authority"})
public class DomainAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorityId")
    private Authority authority;

    @Builder
    private DomainAuthority(Domain domain, Authority authority) {
        this.domain = domain;
        this.authority = authority;
    }

    public void registerUser(User user) {
        if (!Objects.isNull(this.user)) {
            this.user.getDomainAuthorities().remove(this);
        }

        this.user = user;
        user.addDomainAuthority(this);
    }
}
