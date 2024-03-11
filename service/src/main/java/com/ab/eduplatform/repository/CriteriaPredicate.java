package com.ab.eduplatform.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class CriteriaPredicate {

    private final List<Predicate> predicates = new ArrayList<>();
    private final CriteriaBuilder cb;

    public CriteriaPredicate(CriteriaBuilder cb) {
        this.cb = cb;
    }


    public static CriteriaPredicate builder(CriteriaBuilder cb) {
        return  new CriteriaPredicate(cb);
    }

    public <T> CriteriaPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public <T> CriteriaPredicate add(Collection<T> collection, Function<Collection<T>, Predicate> function) {
        if (collection!= null && !collection.isEmpty()) {
            predicates.add(function.apply(collection));
        }
        return this;
    }

    public Predicate buildAnd() {
        return cb.and(predicates.toArray(Predicate[]::new));
    }

    public Predicate buildOr() {
        return cb.or(predicates.toArray(Predicate[]::new));
    }
}
