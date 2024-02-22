package org.example.dao.impl;

import org.example.dao.CourseDao;
import org.example.pojo.Course;

import javax.persistence.EntityNotFoundException;

public class CourseDaoImpl extends DaoImpl<Course, Long> implements CourseDao {

    public static final long DELETED_COURSE_ID = 1L;

    public CourseDaoImpl() {
        super(Course.class);
    }

    @Override
    public void delete(Long id) {

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
                System.out.println(String.format("%s with id=%s not found!", Course.class.getSimpleName(), id.toString()));
                throw new EntityNotFoundException();
            }
        }
    }
}
