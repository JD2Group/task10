package org.example.dao.impl;

import org.example.dao.StudentDao;
import org.example.pojo.Course;
import org.example.pojo.Student;
import org.example.pojo.Task;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl extends DaoImpl<Student, Long> implements StudentDao {

    public static final String GET_STUDENT_BY_EMAIL = "SELECT s FROM Student s WHERE s.email='%s' AND s.id NOT LIKE '%d'";
    public static final String GET_ALL_STUDENTS = "SELECT s FROM Student s WHERE s.id NOT LIKE '%d'";

    public StudentDaoImpl() {

        super(Student.class);
    }

    @Override
    public Student create(Student student) throws ConstraintViolationException, PropertyValueException {

        return super.create(student);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (id != null) {
            if (!StudentDao.DELETED_STUDENT_ID.equals(id)) {
                Student student = super.read(id);
                Student deleted = super.read(StudentDao.DELETED_STUDENT_ID);
                if (student != null) {
                    getEm().getTransaction().begin();
                    getEm().refresh(student);
                    student.getSolutions().stream()
                        .peek(solution -> solution.setStudent(deleted))
                        .forEach(getEm()::merge);
                    getEm().remove(student);
                    getEm().getTransaction().commit();
                } else {
                    throw new EntityNotFoundException();
                }
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
    public List<Course> readAllCoursesByStudentId(Long studentId) throws EntityNotFoundException {

        List<Course> list = new ArrayList<>();
        if (studentId != null) {
            Student student = read(studentId);
            list.addAll(student.getCourses());
        }
        return list;
    }

    @Override
    public List<Task> readTasksByStudentId(Long studentId) throws NoResultException {

        List<Task> taskList = new ArrayList<>();
        if (studentId != null) {
            List<Course> courses = readAllCoursesByStudentId(studentId);
            courses.forEach(course -> taskList.addAll(course.getTasks()));
            return taskList;
        } else {
            throw new NoResultException();
        }
    }

    @Override
    public List<Student> getAllStudentsByCourse(Course course) {

        if (course != null) {
            return new ArrayList<>(course.getStudents());
        } else {
            throw new NoResultException();
        }
    }


    @Override
    protected String getAllSqlString() {

        return String.format(GET_ALL_STUDENTS, StudentDao.DELETED_STUDENT_ID);
    }
}
