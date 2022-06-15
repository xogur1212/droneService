package com.xy124.auth.repository;

import com.xy124.auth.model.Permit;
import com.xy124.auth.model.UserGroup;
import com.xy124.auth.model.UserGroupPermit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserGroupPermitRepository extends JpaRepository<UserGroupPermit, Long> {
    Long deleteByUserGroupSeq(int userGroupSeq);

    Long deleteByUserGroup2AndPermit(UserGroup usergroup, Permit permit);

    @Modifying
    @Query("delete from UserGroupPermit where userGroup2.userGroupSeq = :userGroupSeq")
    void deleteAllByUserGroupSeq(int userGroupSeq);

}
