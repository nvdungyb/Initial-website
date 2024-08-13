package com.practice.from_scratch.controller;

import com.practice.from_scratch.config.JWTUtils;
import com.practice.from_scratch.dto.request.RequestLoginDto;
import com.practice.from_scratch.dto.request.RequestSignUpDto;
import com.practice.from_scratch.dto.response.TokenDto;
import com.practice.from_scratch.dto.response.v1.ResponseLoginDto;
import com.practice.from_scratch.dto.response.v2.ResponseLoginDtoV2;
import com.practice.from_scratch.entity.User;
import com.practice.from_scratch.entity.UserDetailsImpl;
import com.practice.from_scratch.service.AuthService;
import com.practice.from_scratch.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthService authService;

    @PostMapping("/api/signup")
    public ResponseEntity<?> userSignUp(@RequestBody RequestSignUpDto signupDto) {
        User userDto = userService.createUser(signupDto);

        if (!userService.findUserExist(userDto)) {
            User user = userService.save(userDto);
            return ResponseEntity.ok()
                    .body(user);
        }

        return ResponseEntity.badRequest()
                .body("User already exist!");
    }

    @GetMapping("/api/login")
    public ResponseEntity<?> jwtProvider(@RequestBody RequestLoginDto userLogin) {
        Authentication authentication = userService.authenticate(userLogin);
        String jwt = jwtUtils.generateJwtToken(authentication);

        ResponseLoginDto responseLoginDto = ResponseLoginDto.builder()
                .userDetails((UserDetailsImpl) authentication.getPrincipal())
                .jwt(jwt)
                .build();

        return ResponseEntity.ok()
                .body(responseLoginDto);
    }

    @GetMapping("/api/v2/login")
    public ResponseEntity<?> jwtProviderV2(@RequestBody RequestLoginDto userLogin) {
        Authentication authentication = userService.authenticate(userLogin);
        TokenDto tokens = jwtUtils.createTokens((UserDetails) authentication.getPrincipal());

        ResponseLoginDtoV2 responseLoginDto = ResponseLoginDtoV2.builder()
                .userDetails((UserDetailsImpl) authentication.getPrincipal())
                .tokens(tokens)
                .build();

        return ResponseEntity.ok()
                .body(responseLoginDto);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtToken(request);

        boolean isLogout = userService.logout(jwt);

        return ResponseEntity.ok()
                .body("User logout? " + isLogout);
    }

    @PutMapping("/api/v2/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtils.getJwtToken(request);

        if (refreshToken == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid client request!");

        TokenDto accessAndRefreshToken = authService.getTokens(refreshToken);
        return ResponseEntity.ok().body(accessAndRefreshToken);
    }

    @GetMapping("/api/users")
    public List<User> getUsers() {
        return userService.findAll();
    }
}
