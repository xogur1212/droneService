package com.xy124.auth.config.auth;

import com.xy124.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonUserDetails implements UserDetails {

    private User user;

    private Set<String> permitList;


    public CommonUserDetails(User user) {
        this.user = user;
        permitList = new LinkedHashSet<>();
    }

    public User getUser() {

        return user;
    }

    public Long getUserSeq() {
        return user.getUserSeq();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();


        if (user != null) {
            user.getUserInGroup().forEach(userInGroup -> {
                userInGroup
                        .getUserGroup()
                        .getUserGroupPermit()
                        .stream()
                        .filter(userGroupPermit -> permitList.add("ROLE_" + userGroupPermit.getPermitMenu().getCodeValue() + "_" + userGroupPermit.getPermit().getCodeValue()))
                        .collect(Collectors.toList());
            });
            permitList.forEach(permit -> {
                authorities.add(new SimpleGrantedAuthority(permit));
            });
        }
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
