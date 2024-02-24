package org.example.dao.impl;

import org.example.dao.DAO;
import org.example.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class DaoImpl<T, R> implements DAO<T, R> {

    private final Class<T> clazz;
    private EntityManager em;

    public DaoImpl(Class<T> clazz) {

        this.clazz = clazz;
        this.em = getEm();
    }

    @Override
    public T create(T object) {

        em.getTransaction().begin();
        em.persist(object);
        em.getTransaction().commit();
        return object;
    }

    @Override
    public T read(R id) throws EntityNotFoundException {

        T object;
        em.getTransaction().begin();
        object = em.find(clazz, id);
        em.getTransaction().commit();
        return object;
    }

    @Override
    public T update(T object) {

        T t;
        em.getTransaction().begin();
        t = em.merge(object);
        em.getTransaction().commit();
        return t;
    }

    @Override
    public void delete(R id) throws EntityNotFoundException {

        em.getTransaction().begin();
        Object rootEntity = em.getReference(clazz, id);
        em.remove(rootEntity);
        em.getTransaction().commit();
    }

    @Override
    public List<T> readAll() {

        em.getTransaction().begin();
        TypedQuery<T> query = em.createQuery(getAllSqlString(), clazz);
        List<T> list = query.getResultList();
        em.getTransaction().commit();
        return list;
    }

    @Override
    public void closeManager() {

        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    protected EntityManager getEm() {

        if (em == null || !em.isOpen()) {
            em = HibernateUtil.getEntityManager();
        }
        return em;
    }

    protected String getAllSqlString() {

        return String.format("SELECT s FROM %s s", clazz.getSimpleName());
    }
}
