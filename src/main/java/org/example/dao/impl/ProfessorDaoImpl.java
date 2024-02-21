package org.example.dao.impl;

import org.example.dao.ProfessorDao;
import org.example.pojo.Professor;
import org.example.pojo.Student;
import org.example.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class ProfessorDaoImpl extends DaoImpl<Professor,Long> implements ProfessorDao {

    private static final EntityManager em = HibernateUtil.getEntityManager();

    public ProfessorDaoImpl() {

        super(Professor.class, em);
    }

    @Override
    public Professor create(Professor professor) {

        if (super.readAll()
                .stream()
                .allMatch(prof -> (!prof.getName()
                                        .equalsIgnoreCase(professor.getName()))
                                      || (!prof.getSurname()
                                               .equalsIgnoreCase(professor.getSurname())))) {
            em.getTransaction().begin();
            em.persist(professor);
            em.getTransaction().commit();
        } else {
            return null;
        }
        return professor;
    }

    @Override
    public void delete(Long id) {

        Professor professor = super.read(id);
        if (professor != null) {
            em.refresh(professor);
            em.getTransaction().begin();
            professor.getCourses().stream()
                .peek(course -> course.setProfessor(null))
                .forEach(em::merge);
            em.flush();
            em.remove(professor);
            em.getTransaction().commit();
        } else {
            System.out.println(String.format("%s with id=%s not found!", Professor.class.getSimpleName(), id.toString()));
            throw new EntityNotFoundException() ;
        }
    }
}
