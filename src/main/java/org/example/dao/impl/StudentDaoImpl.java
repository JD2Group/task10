package org.example.dao.impl;

import org.example.dao.StudentDao;
import org.example.pojo.Course;
import org.example.pojo.Student;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl extends DaoImpl<Student, Long> implements StudentDao {

    public StudentDaoImpl() {

        super(Student.class);
    }

    @Override
    public Student create(Student student) throws ConstraintViolationException/*not unique email*/, PropertyValueException /*empty fields*/ {

        super.create(student);
        return student;
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (!id.equals(StudentDao.DELETED_STUDENT_ID)) {
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

    @Override
    public Student getByEmail(String email) throws NoResultException {

        Student student;
        String sqlQuery = String.format("SELECT s FROM Student s WHERE s.email='%s' AND s.id NOT LIKE '%d'",
            email, StudentDao.DELETED_STUDENT_ID);
        getEm().getTransaction().begin();
        TypedQuery<Student> query = getEm().createQuery(sqlQuery, Student.class);
        student = query.getSingleResult();
        getEm().getTransaction().commit();
        return student;
    }

    @Override
    public List<Course> readAllByStudentId(Long studentId) throws NoResultException {

        Student student = read(studentId);
        return new ArrayList<>(student.getCourses());
    }

    @Override
    protected String getAllSqlString() {

        return String.format("SELECT s FROM Student s WHERE s.id NOT LIKE '%d'", StudentDao.DELETED_STUDENT_ID);
    }
}
