package org.example.dao.impl;

import org.example.dao.ProfessorDao;
import org.example.pojo.Professor;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class ProfessorDaoImpl extends DaoImpl<Professor, Long> implements ProfessorDao {


    public static final String GET_PROFESSOR_BY_EMAIL = "SELECT p FROM Professor p WHERE p.email='%s'";

    public ProfessorDaoImpl() {

        super(Professor.class);
    }

    @Override
    public Professor create(Professor professor) throws ConstraintViolationException, PropertyValueException {

        return super.create(professor);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (id != null) {
            Professor professor = super.read(id);
            if (professor != null) {
                getEm().refresh(professor);
                getEm().getTransaction().begin();
                professor.getCourses().stream()
                    .peek(course -> course.setProfessor(null))
                    .forEach(getEm()::merge);
                getEm().flush();
                getEm().remove(professor);
                getEm().getTransaction().commit();
            } else {
                throw new EntityNotFoundException();
            }
        }
    }

    @Override
    public Professor getByEmail(String email) throws NoResultException {

        String sqlQuery = String
                              .format(GET_PROFESSOR_BY_EMAIL, email);
        TypedQuery<Professor> query = getEm().createQuery(sqlQuery, Professor.class);
        return query.getSingleResult();
    }
}
