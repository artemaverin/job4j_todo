package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CategoryStore implements CategoryRepository {

    private CrudRepository crudRepository;

    @Override
    public Collection<Category> findAll() {
        return crudRepository.query("from Category", Category.class);
    }

    @Override
    public List<Category> findByIds(List<Integer> integerList) {
        var textQuery = """
                FROM Category f 
                WHERE f.id IN :fCategories
                """;
        return crudRepository.query(textQuery, Category.class, Map.of("fCategories", integerList));
    }
}
