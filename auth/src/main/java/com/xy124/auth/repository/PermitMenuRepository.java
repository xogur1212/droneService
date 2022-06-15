package com.xy124.auth.repository;

import com.xy124.auth.model.PermitMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitMenuRepository extends JpaRepository<PermitMenu, Long> {

    PermitMenu findByCodeValue(String codeValue);

}
