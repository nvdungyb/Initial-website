package com.practice.from_scratch.config;

import com.practice.from_scratch.entity.User;
import com.practice.from_scratch.entity.UserDetailsImpl;
import com.practice.from_scratch.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;

    public String getJwtToken(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        if (token != null && token.startsWith("Bearer "))
            return token.substring(7);

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtToken(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                Optional<User> optional = userRepository.findByEmail(username);
                UserDetailsImpl userDetails = new UserDetailsImpl(optional.get());
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
