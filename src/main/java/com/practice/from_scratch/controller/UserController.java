package com.practice.from_scratch.controller;

import com.practice.from_scratch.config.JWTUtils;
import com.practice.from_scratch.dto.request.RequestLoginDto;
import com.practice.from_scratch.dto.request.RequestSignUpDto;
import com.practice.from_scratch.dto.response.ResponseLoginDto;
import com.practice.from_scratch.entity.User;
import com.practice.from_scratch.entity.UserDetailsImpl;
import com.practice.from_scratch.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtils jwtUtils;

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

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtToken(request);

        boolean isLogout = userService.logout(jwt);

        return ResponseEntity.ok()
                .body("User logout? " + isLogout);
    }

    @GetMapping("/api/users")
    public List<User> getUsers() {
        return userService.findAll();
    }
}
