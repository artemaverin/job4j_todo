package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;
import ru.job4j.todo.status.Status;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {
    Optional<Task> save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    boolean complete(Task task);

    Optional<Task> findById(int id);

    Collection<Task> findAll();

    Collection<Task> findByStatus(Status status);
}
