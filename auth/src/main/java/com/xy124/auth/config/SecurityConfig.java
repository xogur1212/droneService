package com.xy124.auth.config;


import com.xy124.auth.config.security.CustomAuthenticationEntryPoint;
import com.xy124.auth.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsConfig corsConfig;
    private final AccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;

    @Value("#{'${permit.all.page.basic}'.split(',')}")
    private String[] permitAllBasic;

    @Value("#{'${permit.all.page.add}'.split(',')}")
    private String[] permitAllAdd;

    private String[] permitAll = null;

    @Value("#{'${role.admin.page}'.split(',')}")
    private String[] roleAdminPage;

    @Value("#{'${role.manager.page}'.split(',')}")
    private String[] roleManagerPage;





    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        List<String> list = new ArrayList<>();
        Collections.addAll(list, permitAllBasic);
        Collections.addAll(list, permitAllAdd);

        permitAll = list.toArray(new String[list.size()]);
        httpSecurity
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

    //TODO ROLE 관련 추가

        httpSecurity
                .authorizeRequests()
//                .antMatchers(permitAll).permitAll()
//                .antMatchers(roleAdminPage).access("hasRole('ROLE_ADMIN')")
//                .antMatchers(roleManagerPage).access("hasRole('ROLE_MANAGER')")
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder delegatingPasswordEncoder() {
        return DefaultPasswordEncoderFactories.getInstance().createDelegatingPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
