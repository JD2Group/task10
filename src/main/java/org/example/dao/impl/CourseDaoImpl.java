package org.example.dao.impl;

import org.example.dao.CourseDao;
import org.example.pojo.Course;
import org.example.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class CourseDaoImpl extends DaoImpl<Course, Long> implements CourseDao {

    private static final EntityManager em = HibernateUtil.getEntityManager();

    public CourseDaoImpl() {
        super(Course.class, em);
    }

    @Override
    public void delete(Long id) {

        Course course = super.read(id);
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
            throw new EntityNotFoundException() ;
        }
    }
}
