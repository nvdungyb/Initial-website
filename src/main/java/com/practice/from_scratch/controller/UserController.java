package com.practice.from_scratch.controller;

import com.practice.from_scratch.dto.UserSignUpDto;
import com.practice.from_scratch.entity.User;
import com.practice.from_scratch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> userSignUp(@RequestBody UserSignUpDto signupDto) {
        User userDto = userService.createUser(signupDto);

        if (!userService.findUserExist(userDto)) {
            User user = userService.save(userDto);
            return ResponseEntity.ok()
                    .body(user);
        }

        return ResponseEntity.badRequest()
                .body("User already exist!");
    }
}
