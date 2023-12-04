package com.example.securitypilot.infrastructure.user;

import com.example.securitypilot.domain.auth.Domain;
import com.example.securitypilot.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(
            value = "SELECT u "
                    + "FROM User u "
                    + "JOIN FETCH u.domainAuthorities da "
                    + "JOIN FETCH da.authority a "
                    + "JOIN FETCH a.menus m "
                    + "WHERE u.email = :email "
                    + "AND da.domain = :domain"
    )
    Optional<User> findByEmailWithAuthorization(
            @Param("email") String email,
            @Param("domain") Domain domain
    );
}
