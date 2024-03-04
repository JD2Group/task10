package org.example.dao.impl;

import org.apache.log4j.Logger;
import org.example.dao.ProfessorDao;
import org.example.pojo.Professor;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import static org.example.utils.Constants.*;

public class ProfessorDaoImpl extends DaoImpl<Professor, Long> implements ProfessorDao {


    public static final String GET_PROFESSOR_BY_EMAIL = "SELECT p FROM Professor p WHERE p.email='%s'";
    private final Logger log = Logger.getLogger(ProfessorDaoImpl.class);

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
            //getEm().refresh(professor);
            getEm().getTransaction().begin();

            String JPQL = String.format("UPDATE courses c SET c.professor_id=%d WHERE c.professor_id=%d", null, id);
            Query query = getEm().createNativeQuery(JPQL);
            query.executeUpdate();

            /*
            professor.getCourses().stream()
                .peek(course -> course.setProfessor(null))
                .forEach(getEm()::merge);
            getEm().flush();
            */

            getEm().remove(professor);
            commitTransaction(DELETE_MESSAGE, DELETE_FAILED_MESSAGE, professor);
        } else {
            log.error(String.format(ENTITY_NOT_FOUND_MESSAGE, id));
            throw new EntityNotFoundException();
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
