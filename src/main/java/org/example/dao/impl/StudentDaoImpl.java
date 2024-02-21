package org.example.dao.impl;

import org.example.dao.StudentDao;
import org.example.pojo.Student;
import org.example.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

public class StudentDaoImpl extends DaoImpl<Student,Long> implements StudentDao {

    private static final EntityManager em = HibernateUtil.getEntityManager();

    public StudentDaoImpl() {

        super(Student.class, em);

    }

    @Override
    public Student create(Student student) {

        if (super.readAll()
                .stream()
                .allMatch(stud -> (!stud.getName()
                                        .equalsIgnoreCase(student.getName()))
                                      || (!stud.getSurname()
                                               .equalsIgnoreCase(student.getSurname())))) {
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit();
        } else {
            return null;
        }
        return student;
    }

    @Override
    public void delete(Long id) {

        Student student = super.read(id);
        if (student != null) {
            em.getTransaction().begin();
            em.refresh(student);
            student.getSolutions().forEach(em::remove);
            em.remove(student);
            em.getTransaction().commit();
        } else {
            System.out.println(String.format("%s with id=%s not found!", Student.class.getSimpleName(), id.toString()));
            throw new EntityNotFoundException() ;
        }
    }


    public Student read(String name, String surname) {

        Student student;
        String sqlQuery = String.format("SELECT s FROM students s WHERE s.name='%s' AND s.surname='%s'", name, surname);
        TypedQuery<Student> query = em.createQuery(sqlQuery, Student.class);
        student = query.getSingleResult();
        return student;
    }
}
