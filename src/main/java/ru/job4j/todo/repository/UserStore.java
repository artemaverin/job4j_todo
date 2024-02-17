package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.*;

@Repository
@AllArgsConstructor
public class UserStore implements UserRepository {

    private CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        crudRepository.run(session -> session.persist(user));
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional("from User as i where i.login = :fLogin and i.password = :fPassword",
                User.class,
                Map.of("fLogin", login, "fPassword", password));
    }

    @Override
    public Collection<User> findAll() {
        return crudRepository.query("from User order by id", User.class);
    }
}
