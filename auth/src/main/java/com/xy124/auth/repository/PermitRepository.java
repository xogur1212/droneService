package com.xy124.auth.repository;

import com.xy124.auth.model.Permit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermitRepository extends JpaRepository<Permit,Long> {

//    Permit findByCodeValue(String codeValue);
//
//    Permit findByCodeSeq(int codeSeq);
}
