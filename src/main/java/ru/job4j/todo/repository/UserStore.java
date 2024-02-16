package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Repository
@AllArgsConstructor
public class UserStore implements UserRepository {

    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        var session = sf.openSession();
        Optional<User> userOptional = Optional.empty();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User as i where i.login = :fLogin and i.password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password);
            userOptional = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return userOptional;
    }

    @Override
    public Collection<User> findAll() {
        var session = sf.openSession();
        List<User> taskList = new ArrayList<>();
        try {
            session.beginTransaction();
            taskList = session.createQuery("from User order by id", User.class).getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskList;
    }
}
