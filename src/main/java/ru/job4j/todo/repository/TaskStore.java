package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.status.Status;

import java.util.*;

@Repository
@AllArgsConstructor
public class TaskStore implements TaskRepository {

    private CrudRepository crudRepository;

    @Override
    public Optional<Task> save(Task task) {
        try {
            crudRepository.run(session -> session.persist(task));
            return Optional.of(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return crudRepository.booleanRun("DELETE Task where id = :fId", Map.of("fId", id));
    }

    @Override
    public boolean update(Task task) {
        return crudRepository.booleanRun("""
                    UPDATE Task SET
                    title = :fTitle,
                    description = :fDescription
                    WHERE id = :fId
                    """,
                Map.of("fTitle", task.getTitle(), "fDescription", task.getDescription(), "fId", task.getId()));
    }

    @Override
    public boolean complete(Task task) {
        return crudRepository.booleanRun("""
                    UPDATE Task SET
                    done = :fDone 
                    WHERE id = :fId
                    """,
                Map.of("fDone", task.isDone(), "fId", task.getId()));
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional("from Task as i where i.id = :fId", Task.class,
                Map.of("fId", id));
    }

    @Override
    public Collection<Task> findAll() {
        return crudRepository.query("from Task order by id", Task.class);
    }

    @Override
    public Collection<Task> findByStatus(Status status) {
        return crudRepository.query("from Task as i where i.done = :fStatus",
                Task.class, Map.of("fStatus", status.getStatus()));
    }

}
