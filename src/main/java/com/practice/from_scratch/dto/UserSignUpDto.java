package com.practice.from_scratch.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserSignUpDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private List<String> roles;
    private boolean enabled;
}
