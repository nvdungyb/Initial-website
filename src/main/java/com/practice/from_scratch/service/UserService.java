package com.practice.from_scratch.service;

import com.practice.from_scratch.dto.UserSignUpDto;
import com.practice.from_scratch.entity.Role;
import com.practice.from_scratch.entity.User;
import com.practice.from_scratch.extension.RoleFactory;
import com.practice.from_scratch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User createUser(UserSignUpDto userSignUpDto) {
        return User.builder()
                .firstName(userSignUpDto.getFirstName())
                .lastName(userSignUpDto.getLastName())
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .phoneNumber(userSignUpDto.getPhoneNumber())
                .address(userSignUpDto.getAddress())
                .enabled(userSignUpDto.isEnabled())
                .roles(determineRoles(userSignUpDto.getRoles()))
                .build();
    }

    public boolean findUserExist(User user) {
        Optional<User> optional = userRepository.findByEmail(user.getEmail());
        if (optional.isEmpty())
            return false;
        return true;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
