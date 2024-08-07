package com.practice.from_scratch.extension;

import com.practice.from_scratch.entity.Role;
import com.practice.from_scratch.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleFactory {
    @Autowired
    private RoleRepository roleRepository;

    public Role getInstance(String role) {
        Optional<Role> optional = roleRepository.findByName(role);

        if (optional.isEmpty()) {
            return roleRepository.findByName(ERole.CUSTOMER.name()).get();
        }
        return optional.get();
    }
}
