package com.practice.from_scratch.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.Email;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 20)
    private String firstName;
    @Column(nullable = false, length = 20)
    private String lastName;

    @Email(message = "Email must be valid!")
    @Column(nullable = false, length = 40)
    private String email;

    @Column(nullable = false, length = 10)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    private boolean enabled;
}
