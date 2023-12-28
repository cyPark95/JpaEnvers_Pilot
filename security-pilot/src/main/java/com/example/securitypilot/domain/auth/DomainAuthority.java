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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"user", "authorities"})
public class DomainAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "domain_authority",
            joinColumns = @JoinColumn(name = "domainId")
    )
    private final Set<Authority> authorities = new HashSet<>();

    @Builder
    private DomainAuthority(Domain domain) {
        this.domain = domain;
    }

    public void registerUser(User user) {
        if (!Objects.isNull(this.user)) {
            this.user.getDomainAuthorities().remove(this);
        }

        this.user = user;
        user.addDomainAuthority(this);
    }

    public void addAuthority(Authority authority) {
        authorities.add(authority);
    }
}
