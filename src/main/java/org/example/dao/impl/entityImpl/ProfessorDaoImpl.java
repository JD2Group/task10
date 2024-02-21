package org.example.dao.impl.entityImpl;



import org.example.dao.ProfessorDao;
import org.example.dao.impl.DaoImpl;
import org.example.pojo.Professor;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class ProfessorDaoImpl extends DaoImpl<Professor> implements ProfessorDao {

    private EntityManager em;

    public ProfessorDaoImpl() {

        super(Professor.class);
        this.em = getEm();
    }

    @Override
    public void delete(Object id) {

        Professor professor = super.read(id);
        try {
            if (professor != null) {
                em.refresh(professor);
                em.getTransaction().begin();
                professor.getCourses().stream()
                    .peek(course -> course.setProfessor(null))
                    .forEach(em::merge);
                em.flush();
                em.remove(professor);
                em.getTransaction().commit();
            }else {
                System.out.println(String.format("%s with id=%s not found!",Professor.class.getSimpleName(), id.toString()));
            }
        } catch (EntityNotFoundException e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
