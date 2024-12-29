package org.MIFI.service;

import org.MIFI.entity.User;
import org.MIFI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String name) {
        return userRepository.findByName(name).get();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public boolean validateUser(String name, String password) {
        Optional<User> extractUser = userRepository.findByName(name);
        if (extractUser.isPresent()) {
            if (extractUser.get().getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean userExistByName(String name) {
        return userRepository.existsByName(name);
    }

}
