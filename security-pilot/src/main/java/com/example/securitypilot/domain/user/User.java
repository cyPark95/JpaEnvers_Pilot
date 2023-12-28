package com.example.securitypilot.domain.user;

import com.example.securitypilot.domain.auth.DomainAuthority;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
@ToString(exclude = "domainAuthorities")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;
    private boolean isBuilder;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true
    )
    private final Set<DomainAuthority> domainAuthorities = new HashSet<>();

    private User(
            String email,
            String password,
            String name,
            boolean isBuilder
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.isBuilder = isBuilder;
    }

    public static User createBuilder(
            String email,
            String password,
            String name
    ) {
        return new User(email, password, name, true);
    }

    public static User createUser(
            String email,
            String password,
            String name
    ) {
        return new User(email, password, name, false);
    }

    public void addDomainAuthority(DomainAuthority domainAuthority) {
        domainAuthorities.add(domainAuthority);
    }


}
