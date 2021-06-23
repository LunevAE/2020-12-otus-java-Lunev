package ru.otus.hibernate.core.repository;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface DataTemplate<T> {
    Optional<T> findById(Session session, long id);

    List<T> findAll(Session session);

    // conditions: "login", "user1"
    List<T> findAllByConditions(Session session, String... conditions);

    void insert(Session session, T object);

    void update(Session session, T object);
}
