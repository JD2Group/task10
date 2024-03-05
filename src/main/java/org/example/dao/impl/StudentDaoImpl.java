package org.example.dao.impl;

import org.apache.log4j.Logger;
import org.example.dao.StudentDao;
import org.example.pojo.Course;
import org.example.pojo.Student;
import org.example.pojo.Task;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.Constants.*;

public class StudentDaoImpl extends DaoImpl<Student, Long> implements StudentDao {

    public static final String GET_STUDENT_BY_EMAIL = "SELECT s FROM Student s WHERE s.email='%s' AND s.id NOT LIKE '%d'";
    public static final String GET_ALL_STUDENTS = "SELECT s FROM Student s WHERE s.id NOT LIKE '%d'";
    private final Logger log = Logger.getLogger(StudentDaoImpl.class);


    public StudentDaoImpl() {

        super(Student.class);
    }

    @Override
    public Student create(Student student) throws ConstraintViolationException, PropertyValueException {
        super.create(student);
        return student;
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (!id.equals(StudentDao.DELETED_STUDENT_ID)) {
            Student student = super.read(id);

            if (student != null) {
                getEm().getTransaction().begin();
                String JPQL = String.format("UPDATE solutions s SET s.student_id=%d WHERE s.student_id=%d", DELETED_STUDENT_ID, id);
                Query query = getEm().createNativeQuery(JPQL);
                query.executeUpdate();
                getEm().remove(student);
                commitTransaction(DELETE_MESSAGE, DELETE_FAILED_MESSAGE, student);
            } else {
                log.error(String.format(ENTITY_NOT_FOUND_MESSAGE, id));
                throw new EntityNotFoundException();
            }
        }
    }

    @Override
    public Student getByEmail(String email) throws NoResultException {

        String sqlQuery = String.format(GET_STUDENT_BY_EMAIL,
            email, StudentDao.DELETED_STUDENT_ID);
        TypedQuery<Student> query = getEm().createQuery(sqlQuery, Student.class);
        return query.getSingleResult();
    }

    @Override
    public List<Course> readAllCoursesByStudentId(Long studentId) throws NoResultException {

        Student student = read(studentId);
        return new ArrayList<>(student.getCourses());
    }

    @Override
    public List<Task> readTasksByStudentId(Long studentId) throws NoResultException {

        List<Task> taskList = new ArrayList<>();
        List<Course> courses = readAllCoursesByStudentId(studentId);
        courses.forEach(course -> taskList.addAll(course.getTasks()));
        return taskList;
    }

    @Override
    public List<Student> getAllStudentsByCourse(Course course) {

        return new ArrayList<>(course.getStudents());
    }

    @Override
    protected String getAllSqlString() {

        return String.format(GET_ALL_STUDENTS, StudentDao.DELETED_STUDENT_ID);
    }
}
