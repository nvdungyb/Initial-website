package com.practice.from_scratch.service;

import com.practice.from_scratch.entity.User;
import com.practice.from_scratch.entity.UserDetailsImpl;
import com.practice.from_scratch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty())
            throw new UsernameNotFoundException("Can not found user with email: " + email);

        return new UserDetailsImpl(optional.get());
    }
}
