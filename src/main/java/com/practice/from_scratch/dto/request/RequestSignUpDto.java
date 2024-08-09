package com.practice.from_scratch.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestSignUpDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private List<String> roles;
    private boolean enabled;
}
