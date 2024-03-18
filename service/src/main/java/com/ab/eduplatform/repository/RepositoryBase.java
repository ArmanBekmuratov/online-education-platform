package com.ab.eduplatform.repository;

import com.ab.eduplatform.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase <K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    protected final Class<E> clazz;
    protected final EntityManager entityManager;

    @Override
    public E save(E entity) {
        entityManager.persist(entity);

        return entity;
    }

    @Override
    public void delete(E entity) {
        if (entity != null) {
            entityManager.remove(entity);
            entityManager.flush();
        }
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(clazz);

        criteria.from(clazz);
        return entityManager.createQuery(criteria)
                .getResultList();
    }
}
