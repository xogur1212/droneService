package com.xy124.auth.filter;

import com.xy124.auth.config.auth.CommonUserDetailsService;
import com.xy124.auth.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import javax.servlet.http.Cookie;

@RequiredArgsConstructor
@Configuration
public class JwtRequestFilter extends OncePerRequestFilter {


    private final CommonUserDetailsService commonUserDetailsService;
    private final JwtUtil jwtUtil;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        final String uri = request.getRequestURI();

        if (uri.contains("/t1-danmcdn-net/") || uri.contains("/webjar/")) {

        } else {
            String authorizationHeader = request.getHeader("Authorization");
            String refreshToken = request.getHeader("RefreshHeader");
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("accessToken")) {
                        authorizationHeader = "Bearer " + cookie.getValue();
                    }
                }
            }


            String username = null;
            String jwt = null;


            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

                jwt = authorizationHeader.substring(7);

                try {
                    username = jwtUtil.extractUsername(jwt);
                } catch (ExpiredJwtException e) {
                    request.setAttribute("exception", "ExpiredJwtException");
                } catch (JwtException e) {
                    //e.printStackTrace();
                    request.setAttribute("exception", "JwtException");
                }

            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.commonUserDetailsService.loadUserByUsername(username);

                if (jwtUtil.validToken(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        }


        filterChain.doFilter(request, response);
    }
}
