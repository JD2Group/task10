package org.example.dao.impl;

import org.apache.log4j.Logger;
import org.example.dao.CourseDao;
import org.example.pojo.Course;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.example.utils.Constants.*;

public class CourseDaoImpl extends DaoImpl<Course, Long> implements CourseDao {

    public static final String GET_COURSE_BY_PROFESSOR_ID = "SELECT c FROM Course c WHERE c.professor=%d AND c.id NOT LIKE '%d'";
    public static final String GET_COURSES_BY_TITLE_EMAIL = "SELECT c FROM Course c, Professor p WHERE c.title='%s' AND c.professor=p.id AND p.email='%s' AND c.id NOT LIKE '%d'";
    public static final String GET_ALL_COURSES = "SELECT c FROM Course c WHERE c.id NOT LIKE '%d'";
    private final Logger log = Logger.getLogger(CourseDaoImpl.class);

    public CourseDaoImpl() {
        super(Course.class);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (DELETED_COURSE_ID != id) {
            Course course = super.read(id);
            Course deleted = super.read(DELETED_COURSE_ID);
            if (course != null) {
                getEm().getTransaction().begin();
                getEm().refresh(course);
                course.getTasks().stream()
                    .peek(task -> task.setCourse(deleted))
                    .forEach(getEm()::merge);
                getEm().flush();
                getEm().remove(course);
                commitTransaction(DELETE_MESSAGE, DELETE_FAILED_MESSAGE, course);
            } else {
                log.error(String.format(ENTITY_NOT_FOUND_MESSAGE,id));
                throw new EntityNotFoundException();
            }
        }
    }

    @Override
    public List<Course> readAllByProfId(Long id) throws NoResultException {

        String sqlQuery = String
                              .format(GET_COURSE_BY_PROFESSOR_ID,
                                  id, CourseDao.DELETED_COURSE_ID);
        return getCoursesBySqlQuery(sqlQuery);
    }

    @Override
    public List<Course> getCourseByTitleAndEmail(String title, String profEmail) throws NoResultException {

        String sqlQuery = String
                              .format(GET_COURSES_BY_TITLE_EMAIL,
                                  title, profEmail, CourseDao.DELETED_COURSE_ID);
        TypedQuery<Course> query = getEm().createQuery(sqlQuery, Course.class);
        return query.getResultList();
    }

    @Override
    protected String getAllSqlString() {

        return String.format(GET_ALL_COURSES, CourseDao.DELETED_COURSE_ID);
    }

    private List<Course> getCoursesBySqlQuery(String sqlQuery) {

        TypedQuery<Course> query = getEm().createQuery(sqlQuery, Course.class);
        return query.getResultList();
    }
}
