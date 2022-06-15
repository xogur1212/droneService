package com.xy124.auth.repository;

import com.xy124.auth.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
    UserStatus findByCodeValue(String codeValue);
}
