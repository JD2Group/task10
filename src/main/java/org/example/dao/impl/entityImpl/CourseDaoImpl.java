package org.example.dao.impl.entityImpl;


import org.example.dao.CourseDao;
import org.example.dao.impl.DaoImpl;
import org.example.pojo.Course;
import org.example.pojo.Professor;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class CourseDaoImpl extends DaoImpl<Course> implements CourseDao {

    private EntityManager em;

    public CourseDaoImpl() {

        super(Course.class);
        this.em = getEm();
    }

    @Override
    public void delete(Object id) {

        Course course = super.read(id);
        try {
            if (course != null) {
                em.getTransaction().begin();
                em.refresh(course);
                course.getStudents().stream()
                    .peek(student -> student.getCourses().remove(course))
                    .forEach(em::merge);
                course.getTasks().stream()
                    .peek(task -> task.setCourse(null))
                    .forEach(em::merge);
                em.flush();
                em.remove(course);
                em.getTransaction().commit();
            } else {
                System.out.println(String.format("%s with id=%s not found!", Course.class.getSimpleName(), id.toString()));
            }
        } catch (EntityNotFoundException e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
