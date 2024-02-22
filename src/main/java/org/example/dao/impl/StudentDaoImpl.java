package org.example.dao.impl;

import org.example.dao.StudentDao;
import org.example.pojo.Student;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class StudentDaoImpl extends DaoImpl<Student, Long> implements StudentDao {

    public StudentDaoImpl() {

        super(Student.class);
    }

    @Override
    public Student create(Student student) throws ConstraintViolationException, PropertyValueException {

       /* if (super.readAll().stream()
                .allMatch(stud -> (
                    !stud.getEmail().equalsIgnoreCase(student.getEmail())))) {*/
        super.create(student);
       /* } else {
            return null;
        }*/
        return student;
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        Student student = super.read(id);
        if (student != null) {
            getEm().getTransaction().begin();
            getEm().refresh(student);
            student.getSolutions().forEach(getEm()::remove);
            getEm().remove(student);
            getEm().getTransaction().commit();
        } else {
            System.out.println(String.format("%s with id=%s not found!", Student.class.getSimpleName(), id.toString()));
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Student read(String email) throws NoResultException {

        Student student;
        String sqlQuery = String.format("SELECT s FROM Student s WHERE s.email='%s'", email);
        TypedQuery<Student> query = getEm().createQuery(sqlQuery, Student.class);
        student = query.getSingleResult();
        return student;
    }
}
