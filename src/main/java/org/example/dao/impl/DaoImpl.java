package org.example.dao.impl;

import org.apache.log4j.Logger;
import org.example.dao.DAO;
import org.example.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.example.utils.Constants.*;

public abstract class DaoImpl<T, R> implements DAO<T, R> {
    public static final String GET_ALL_ENTITIES = "SELECT s FROM %s s";
    private final Logger log = Logger.getLogger(DaoImpl.class);
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
        log.info(String.format(SAVE_MESSAGE, object.toString()));
        return object;
    }

    @Override
    public T read(R id) throws EntityNotFoundException {

        return em.find(clazz, id);
    }

    @Override
    public T update(T object) {

        T t;
        em.getTransaction().begin();
        t = em.merge(object);
        em.getTransaction().commit();
        log.info(String.format(UPDATE_MESSAGE, object.toString()));
        return t;
    }

    @Override
    public void delete(R id) throws EntityNotFoundException {

        em.getTransaction().begin();
        Object rootEntity = em.getReference(clazz, id.toString());
        em.remove(rootEntity);
        em.getTransaction().commit();
        log.info(String.format(DELETE_MESSAGE, id.toString()));
    }

    @Override
    public List<T> readAll() {
        TypedQuery<T> query = em.createQuery(getAllSqlString(), clazz);
        return query.getResultList();
    }

    @Override
    public void closeManager() {

        if (em != null && em.isOpen()) {
            em.close();
            log.info(String.format(MANAGER_CLOSED_MESSAGE, em.toString()));
        }
    }

    protected EntityManager getEm() {

        if (em == null || !em.isOpen()) {
            em = HibernateUtil.getEntityManager();
            log.info(String.format(MANAGER_OPENED_MESSAGE, em.toString()));
        }
        return em;
    }

    protected String getAllSqlString() {

        return String.format(GET_ALL_ENTITIES, clazz.getSimpleName());
    }
}
