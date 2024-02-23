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

    @Override
    public List<Category> findByIds(List<Integer> integerList) {
        return categoryRepository.findByIds(integerList);
    }
}
