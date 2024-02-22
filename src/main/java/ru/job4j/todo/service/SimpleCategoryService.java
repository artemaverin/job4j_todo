package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {

    private CategoryRepository categoryRepository;

    @Override
    public Collection<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> convert(List<Integer> idList) {
        List<Category> newCategories = new ArrayList<>();
        var categories = categoryRepository.findAll();
        for (Integer id:idList) {
            for (Category category:categories) {
                if (category.getId() == id) {
                    newCategories.add(category);
                }
            }
        }
        return newCategories;
    }
}
