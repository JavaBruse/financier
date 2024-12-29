package org.MIFI.service;

import org.MIFI.entity.Category;
import org.MIFI.entity.User;
import org.MIFI.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired

    public CategoryService(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    public Category addCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public Category getCategoryByNameByUser(User user, String nameCategory) {
        return categoryRepository.findAllByUserId(user.getId()).stream().filter(category -> category.getName().equals(nameCategory)).findFirst().get();
    }

    public boolean categoryExist(long userId, String name) {
        return !categoryRepository.findAllByUserId(userId).stream().filter(category -> category.getName().equals(name)).toList().isEmpty();
    }
}
