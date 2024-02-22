package org.example.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

    private static EntityManagerFactory entityManagerFactory;

    private HibernateUtil() {
    }

    public static EntityManager getEntityManager() {

        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("university");
        }
        return entityManagerFactory.createEntityManager();
    }

    public static void closeFactory() {

        entityManagerFactory.close();
    }
}
