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
    public Professor create(Professor professor) throws ConstraintViolationException, PropertyValueException {

        /*if (super.readAll().stream()
                .allMatch(prof -> (
                    !prof.getEmail().equalsIgnoreCase(professor.getEmail())))) {*/
       super.create(professor);
             /* } else {
            return null;
        }*/
        return professor;
    }

    @Override
    public void delete(Long id) {

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
            System.out.println(String.format("%s with id=%s not found!", Professor.class.getSimpleName(), id.toString()));
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Professor read(String email) throws NoResultException {

        Professor professor;
        String sqlQuery = String.format("SELECT s FROM Professor s WHERE s.email='%s'", email);
        TypedQuery<Professor> query = getEm().createQuery(sqlQuery, Professor.class);
        professor = query.getSingleResult();
        return professor;
    }

    @Override
    public Professor getByEmail(String oldEmail) {
        return null;
    }

    @Override
    public void deleteByEmail(String email) {

    }
}
