package org.example.dao.impl;

import org.example.dao.CourseDao;
import org.example.dao.ProfessorDao;
import org.example.pojo.Course;
import org.example.pojo.Professor;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class CourseDaoImpl extends DaoImpl<Course, Long> implements CourseDao {

    public CourseDaoImpl() {
        super(Course.class);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (id != DELETED_COURSE_ID) {
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

        String sqlQuery = String
                              .format("SELECT c FROM Course c WHERE c.professor=%d AND c.id NOT LIKE '%d'",
                                  id, CourseDao.DELETED_COURSE_ID);
        return getCoursesBySqlQuery(sqlQuery);
    }

    @Override
    public List<Course> getCourseByTitleAndEmail(String title, String profEmail) throws NoResultException {

        ProfessorDao professorDao = new ProfessorDaoImpl();
        Professor professor = professorDao.getByEmail(profEmail);
        professorDao.closeManager();

        String sqlQuery = String
                              .format("SELECT c FROM Course c WHERE c.title='%s' AND c.professor=%d AND c.id NOT LIKE '%d'",
                                  title, professor.getId(), CourseDao.DELETED_COURSE_ID);

        getEm().getTransaction().begin();
        TypedQuery<Course> query = getEm().createQuery(sqlQuery, Course.class);
        List<Course> courses = query.getResultList();
        getEm().getTransaction().commit();
        return courses;
    }

   /* @Override
    public List<Course> readAllByStudentId(Long studentId) throws NoResultException {

        String sqlQuery = String
                              .format("SELECT c FROM Course c, Student s WHERE c.student=s.course AND s.id=%d AND c.id NOT LIKE '%d'",
                                  studentId, CourseDao.DELETED_COURSE_ID);
        return getCoursesBySqlQuery(sqlQuery);
    }*/

    @Override
    protected String getAllSqlString() {

        return String.format("SELECT c FROM Course c WHERE c.id NOT LIKE '%d'", CourseDao.DELETED_COURSE_ID);
    }

    private List<Course> getCoursesBySqlQuery(String sqlQuery) {

        getEm().getTransaction().begin();
        TypedQuery<Course> query = getEm().createQuery(sqlQuery, Course.class);
        List<Course> list = query.getResultList();
        getEm().getTransaction().commit();
        return list;
    }
}
