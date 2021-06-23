package ru.otus.hibernate.core.repository;

import com.google.common.collect.Lists;
import org.hibernate.Criteria;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class DataTemplateHibernate<T> implements DataTemplate<T> {

    private final Class<T> clazz;

    public DataTemplateHibernate(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findById(Session session, long id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<T> findAll(Session session) {
        return session.createQuery(String.format("from %s", clazz.getSimpleName()), clazz).getResultList();
    }

    // conditions: "login", "user1"
    @Override
    public List<T> findAllByConditions(Session session, String... conditions) {
        var chunks = Lists.partition(List.of(conditions), 2);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(clazz);
        Root<T> root = cr.from(clazz);

        var predicates = chunks.stream()
                .map(it -> cb.equal(root.get(it.get(0)), it.get(1)))
                .toArray(Predicate[]::new);

        cr.select(root).where(predicates);

        return session.createQuery(cr).getResultList();
    }


    @Override
    public void insert(Session session, T object) {
        session.persist(object);
    }

    @Override
    public void update(Session session, T object) {
        session.merge(object);
    }
}
