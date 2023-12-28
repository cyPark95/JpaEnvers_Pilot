package com.example.securitypilot.domain.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString(exclude = "menus")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "authority_menu",
            joinColumns = @JoinColumn(name = "authorityId")
    )
    private final Set<Menu> menus = new HashSet<>();

    @Builder
    private Authority(String name) {
        this.name = name;
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
    }
}
