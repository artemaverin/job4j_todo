package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.status.Status;

import java.util.*;

@Repository
@AllArgsConstructor
public class TaskStore implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public Task save(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public boolean deleteById(int id) {
        var session = sf.openSession();
        int result = 0;
        try {
            session.beginTransaction();
            result = session.createQuery("DELETE Task where id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result > 0;
    }

    @Override
    public boolean update(Task task) {
        var session = sf.openSession();
        int result = 0;
        try {
            session.beginTransaction();
            var text = """
                    UPDATE Task SET
                    title = :fTitle,
                    description = :fDescription
                    WHERE id = :fId
                    """;
            result = session.createQuery(text)
                    .setParameter("fTitle", task.getTitle())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result > 0;
    }

    @Override
    public boolean complete(Task task) {
        var session = sf.openSession();
        int result = 0;
        try {
            session.beginTransaction();
            var text = """
                    UPDATE Task SET
                    done = :fDone 
                    WHERE id = :fId
                    """;
            result = session.createQuery(text)
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result > 0;
    }

    @Override
    public Optional<Task> findById(int id) {
        var session = sf.openSession();
        Optional<Task> taskOptional = Optional.empty();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task as i where i.id = :fId", Task.class);
            query.setParameter("fId", id);
            taskOptional = query.uniqueResultOptional();

            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskOptional;
    }

    @Override
    public Collection<Task> findAll() {
        var session = sf.openSession();
        List<Task> taskList = new ArrayList<>();
        try {
            session.beginTransaction();
            taskList = session.createQuery("from Task order by id", Task.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskList;
    }

    @Override
    public Collection<Task> findByStatus(Status status) {
        var session = sf.openSession();
        List<Task> taskList = new ArrayList<>();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery(
                    "from Task as i where i.done = :fStatus", Task.class);
            if (status == Status.COMPLETED) {
                query.setParameter("fStatus", status.getStatus());
            }
            if (status == Status.NEW) {
                query.setParameter("fStatus", status.getStatus());
            }
            taskList = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskList;
    }

}
