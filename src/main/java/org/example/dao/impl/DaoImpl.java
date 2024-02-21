package org.example.dao.impl;

import org.example.dao.DAO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class DaoImpl<T, R> implements DAO<T, R> {

    private final Class<T> clazz;
    private final EntityManager em;

    public DaoImpl(Class<T> clazz, EntityManager em) {
        this.clazz = clazz;
        this.em = em;
    }

    @Override
    public T create(T object) {

        em.getTransaction().begin();
        em.persist(object);
        em.getTransaction().commit();
        return object;
    }

    @Override
    public T read(R id) {
        T object;
        em.getTransaction().begin();
        object = em.find(clazz, id);
        em.getTransaction().commit();
        return object;
    }

    @Override
    public void update(T object) {

        em.getTransaction().begin();
        em.merge(object);
        em.getTransaction().commit();
    }

    @Override
    public void delete(R id) {

        em.getTransaction().begin();
        Object rootEntity = em.getReference(clazz, id);
        em.remove(rootEntity);
        em.getTransaction().commit();
    }


    @Override
    public List<T> readAll() {

        String sqlQuery = String.format("SELECT s FROM %s s", clazz.getSimpleName());
        em.getTransaction().begin();
        TypedQuery<T> query = em.createQuery(sqlQuery, clazz);
        List<T> list = query.getResultList();
        em.getTransaction().commit();
        return list;
    }
}
