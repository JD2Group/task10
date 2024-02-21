package org.example.dao.impl;

import org.example.dao.DAO;
import org.example.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoImpl<T> implements DAO<T> {

    private final Class<T> clazz;
    private final EntityManager em;

    public DaoImpl(Class<T> clazz) {
        this.clazz = clazz;
        em = HibernateUtil.getEntityManager();
    }

    @Override
    public void create(T t) {

        try {
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Entity not saved!");
            e.printStackTrace();
        }

    }

    @Override
    public T read(Object id) {
        T t = null;
        try {
            em.getTransaction().begin();
            t = em.find(clazz, id);

            em.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return t;
    }

    @Override
    public void update(T t) {

        try {
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            System.out.println("Entity has not updated!");
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Object id) {

        try {
            em.getTransaction().begin();
            Object rootEntity = em.getReference(clazz, id);
            em.remove(rootEntity);
            em.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            em.getTransaction().rollback();
            System.out.println("Entity not found!");
        }
    }


    @Override
    public List<T> readAll() {

        List<T> list = new ArrayList<>();
        try {
            em.getTransaction().begin();
            TypedQuery<T> query = em.createQuery("", clazz);
            list = query.getResultList();
            em.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            System.out.println("Entity not found!");
            e.printStackTrace();
        }
        return list;
    }

    protected EntityManager getEm() {
        return em;
    }
}
