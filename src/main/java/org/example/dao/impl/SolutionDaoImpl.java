package org.example.dao.impl;

import org.example.dao.SolutionDao;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class SolutionDaoImpl extends DaoImpl<Solution, Long> implements SolutionDao {

    private static final EntityManager em = HibernateUtil.getEntityManager();

    public SolutionDaoImpl() {

        super(Solution.class, em);
    }
}
