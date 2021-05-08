package com.ksteindl.logolo.services;

import com.ksteindl.logolo.domain.User;
import com.ksteindl.logolo.domain.UserInput;
import com.ksteindl.logolo.exceptions.ValidationException;
import com.ksteindl.logolo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(UserInput userInput) {
        User user = validateUserInput(userInput);
        return userRepository.save(user);
    }

    private User validateUserInput(UserInput userInput) {
        User user = new User();
        validateAndSetUsername(user, userInput.getUsername());
        user.setFullName(userInput.getFullName());
        user.setPassword(bCryptPasswordEncoder.encode(userInput.getPassword()));
        return user;
    }

    private void validateAndSetUsername(User user, String username) {
        userRepository.findByUsername(username).ifPresent( (foundUser) -> {throw new ValidationException("Username already exists");});
        user.setUsername(username);
    }
}
