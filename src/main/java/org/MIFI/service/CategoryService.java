package org.MIFI.service;

import org.MIFI.entity.Category;
import org.MIFI.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired

    public CategoryService(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    public void addCategory(Category category) {
        this.categoryRepository.save(category);
    }

    public boolean categoryExist(long userId, String name) {
        return !categoryRepository.findAllByUserId(userId).stream().filter(category -> category.getName().equals(name)).toList().isEmpty();
    }
}
