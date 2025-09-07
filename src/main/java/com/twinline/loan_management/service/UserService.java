package com.twinline.loan_management.service;

import com.twinline.loan_management.entities.User;
import com.twinline.loan_management.exceptions.ResourceNotFoundException;
import com.twinline.loan_management.exceptions.UnauthorizedException;
import com.twinline.loan_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean usernameExists(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ResourceNotFoundException("Username cannot be null or empty");
        }
        return userRepository.findByUsername(username).isPresent();
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }
        return user;
    }

}
