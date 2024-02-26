package org.example.dao.impl;

import org.example.dao.CourseDao;
import org.example.pojo.Course;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl extends DaoImpl<Course, Long> implements CourseDao {

    public static final String GET_COURSE_BY_PROFESSOR_ID = "SELECT c FROM Course c WHERE c.professor=%d AND c.id NOT LIKE '%d'";
    public static final String GET_COURSES_BY_TITLE_EMAIL = "SELECT c FROM Course c, Professor p WHERE c.title='%s' AND c.professor=p.id AND p.email='%s' AND c.id NOT LIKE '%d'";
    public static final String GET_ALL_COURSES = "SELECT c FROM Course c WHERE c.id NOT LIKE '%d'";

    public CourseDaoImpl() {
        super(Course.class);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (id != null && DELETED_COURSE_ID != id) {
            Course course = super.read(id);
            Course deleted = super.read(DELETED_COURSE_ID);
            if (course != null) {
                getEm().getTransaction().begin();
                getEm().refresh(course);
                course.getStudents().stream()
                    .peek(student -> student.getCourses().remove(course))
                    .forEach(getEm()::merge);
                course.getTasks().stream()
                    .peek(task -> task.setCourse(deleted))
                    .forEach(getEm()::merge);
                getEm().flush();
                getEm().remove(course);
                getEm().getTransaction().commit();
            } else {
                throw new EntityNotFoundException();
            }

        }
    }

    @Override
    public List<Course> readAllByProfId(Long id) throws NoResultException {

        List<Course> list = new ArrayList<>();
        if (id != null) {
            String sqlQuery = String
                                  .format(GET_COURSE_BY_PROFESSOR_ID,
                                      id, CourseDao.DELETED_COURSE_ID);
            list = getCoursesBySqlQuery(sqlQuery);
        }
        return list;
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
