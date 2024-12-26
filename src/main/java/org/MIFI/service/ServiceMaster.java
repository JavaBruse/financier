package org.MIFI.service;

import org.MIFI.entity.Category;
import org.MIFI.entity.User;
import org.MIFI.repository.CategoryRepository;
import org.MIFI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceMaster {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private User userMaster;

    @Autowired
    public ServiceMaster(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository =  categoryRepository;
    }

    public User getUser() {
        return userMaster;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public boolean validateUser(String name, String password) {
        Optional<User> extractUser = userRepository.findByName(name);
        if (extractUser.isPresent()) {
            if (extractUser.get().getPassword().equals(password)) {
                userMaster = extractUser.get();
                return true;
            }
        }
        return false;
    }

    public List<Category> getCategories() {
        return userMaster.getCategories().stream().toList();
    }
}
