package com.example.learn.config;

import com.example.learn.auth.dto.CustomUserDetails;
import com.example.learn.auth.util.JWTUtil;
import com.example.learn.user.User;
import com.example.learn.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtilservice;
    private final UserRepository userRepository;
    public JwtAuthenticationFilter(JWTUtil service, UserRepository repository) {
        jwtUtilservice = service;
        userRepository = repository;
    }

    @Override
    protected  boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth/");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getCookies() != null) {
            for(Cookie cookie: request.getCookies()) {
                if("access_token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    String userEmail = jwtUtilservice.extractUsername(token);
                    User user = userRepository.findByEmail(userEmail).orElse(null);
                    if(user != null) {
                        CustomUserDetails userDetails = new CustomUserDetails(user);
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()))
                                );
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
