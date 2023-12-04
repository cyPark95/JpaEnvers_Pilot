package com.example.securitypilot.infrastructure.auth;

import com.example.securitypilot.domain.auth.Authority;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    @Query(
            "SELECT a "
                    + "FROM Authority a "
                    + "JOIN FETCH a.menus m "
                    + "WHERE a.id = :id"
    )
    Optional<Authority> findByIdWithMenus(@Param("id") Long id);
}
