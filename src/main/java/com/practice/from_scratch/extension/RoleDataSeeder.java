package com.practice.from_scratch.extension;

import com.practice.from_scratch.entity.Role;
import com.practice.from_scratch.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoleDataSeeder {
    @Autowired
    private RoleRepository roleRepository;

    @EventListener
    @Transactional
    public void LoadRoles(ContextRefreshedEvent event) {
        // Limit access to the db as much as possible.
        List<Role> rolesDb = (List<Role>) roleRepository.findAll();
        List<ERole> eRoles = Arrays.stream(ERole.values()).collect(Collectors.toList());

        Map<String, Role> hashRoles = rolesDb.stream()
                .collect(Collectors.toMap(Role::getName, role -> role));

        for (ERole erole : eRoles) {
            if (!hashRoles.containsKey(erole.name()))
                roleRepository.save(new Role(erole.name()));
        }
    }
}
