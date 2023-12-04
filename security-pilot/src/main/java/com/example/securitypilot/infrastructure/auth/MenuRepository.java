package com.example.securitypilot.infrastructure.auth;

import com.example.securitypilot.domain.auth.Menu;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<Menu, Long> {

}
