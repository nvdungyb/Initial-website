package com.practice.from_scratch.service;

import com.practice.from_scratch.dto.request.RequestSignUpDto;
import com.practice.from_scratch.entity.Role;
import com.practice.from_scratch.entity.User;
import com.practice.from_scratch.extension.RoleFactory;
import com.practice.from_scratch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleFactory roleFactory;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Set<Role> determineRoles(List<String> rolesName) {
        Set<Role> roles = new HashSet<>();
        if (rolesName == null) {
            roles.add(roleFactory.getInstance("CUSTOMER"));
        } else {
            for (String name : rolesName) {
                roles.add(roleFactory.getInstance(name));
            }
        }

        return roles;
    }

    public User createUser(RequestSignUpDto requestSignUpDto) {
        return User.builder()
                .firstName(requestSignUpDto.getFirstName())
                .lastName(requestSignUpDto.getLastName())
                .email(requestSignUpDto.getEmail())
                .password(requestSignUpDto.getPassword())
                .phoneNumber(requestSignUpDto.getPhoneNumber())
                .address(requestSignUpDto.getAddress())
                .enabled(requestSignUpDto.isEnabled())
                .roles(determineRoles(requestSignUpDto.getRoles()))
                .build();
    }

    public boolean findUserExist(User user) {
        Optional<User> optional = userRepository.findByEmail(user.getEmail());
        if (optional.isEmpty())
            return false;
        return true;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }
}
