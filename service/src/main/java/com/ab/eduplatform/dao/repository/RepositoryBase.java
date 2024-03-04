package com.ab.eduplatform.dao.repository;

import com.ab.eduplatform.entity.BaseEntity;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase <K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private final Class<E> clazz;
    private final Session session;

    @Override
    public E save(E entity) {
        session.persist(entity);

        return entity;
    }

    @Override
    public void delete(K id) {
        E entity = session.find(clazz, id);

        if (entity != null) {
            session.remove(entity);
            session.flush();
        }
    }

    @Override
    public void update(E entity) {
        session.merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteria = session.getCriteriaBuilder().createQuery(clazz);

        criteria.from(clazz);
        return session.createQuery(criteria)
                .getResultList();
    }
}
