package com.practice.from_scratch.service;

import com.practice.from_scratch.config.JWTUtils;
import com.practice.from_scratch.dto.response.TokenDto;
import com.practice.from_scratch.entity.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTUtils jwtUtils;

    public TokenDto getTokens(String refreshToken) {
        String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        TokenDto tokens = jwtUtils.createTokens(userDetails);

        return tokens;
    }
}
