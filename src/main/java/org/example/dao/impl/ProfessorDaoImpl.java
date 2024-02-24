package org.example.dao.impl;

import org.example.dao.ProfessorDao;
import org.example.pojo.Professor;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class ProfessorDaoImpl extends DaoImpl<Professor, Long> implements ProfessorDao {


    public ProfessorDaoImpl() {

        super(Professor.class);
    }

    @Override
    public Professor create(Professor professor) throws ConstraintViolationException/*not unique email*/, PropertyValueException /*empty fields*/ {

        super.create(professor);
        return professor;
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

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

    @Override
    public Professor getByEmail(String oldEmail) throws NoResultException {

        String sqlQuery = String
                              .format("SELECT p FROM Professor p WHERE p.email='%s'", oldEmail);
        getEm().getTransaction().begin();
        TypedQuery<Professor> query = getEm().createQuery(sqlQuery, Professor.class);
        Professor professor = query.getSingleResult();
        getEm().getTransaction().commit();
        return professor;
    }
}
