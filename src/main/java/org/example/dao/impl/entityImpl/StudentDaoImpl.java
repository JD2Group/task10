package org.example.dao.impl.entityImpl;


import org.example.dao.StudentDao;
import org.example.dao.impl.DaoImpl;
import org.example.pojo.Professor;
import org.example.pojo.Student;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class StudentDaoImpl extends DaoImpl<Student> implements StudentDao {

    private EntityManager em;

    public StudentDaoImpl() {

        super(Student.class);
        this.em = getEm();
    }

    @Override
    public void delete(Object id) {

        Student student = super.read(id);
        try {
            if (student != null) {
                em.getTransaction().begin();
                em.refresh(student);
                student.getSolutions().forEach(em::remove);
                //em.flush();
                em.remove(student);
                em.getTransaction().commit();
            } else {
                System.out.println(String.format("%s with id=%s not found!", Student.class.getSimpleName(), id.toString()));
            }
        } catch (EntityNotFoundException e) {
            em.getTransaction().rollback();
            System.out.println("Entity not found!");
        }
    }
}
