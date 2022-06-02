package com.xy124.auth.repository;

import com.xy124.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserId(String id);
}
