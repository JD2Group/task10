package org.example.service.impl;

import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.pojo.Student;
import org.example.service.AdminService;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class AdminServiceImpl extends ParrentService implements AdminService {

    @Override
    public Professor createProfessorAccount(String name, String surname, String email) throws ConstraintViolationException, PropertyValueException {

        Professor professor = Professor.builder()
                                  .name(name)
                                  .surname(surname)
                                  .email(email)
                                  .build();

        return getProfessorDao().create(professor);
    }

    @Override
    public Student createStudentAccount(String name, String surname, String email) throws ConstraintViolationException, PropertyValueException {

        Student student = Student.builder()
                              .name(name)
                              .surname(surname)
                              .email(email)
                              .build();
        return getStudentDao().create(student);
    }

    @Override
    public Professor getProfessorByEmail(String email) throws NoResultException {

        return getProfessorDao().getByEmail(email);
    }

    @Override
    public Student getStudentByEmail(String email) throws NoResultException {

        return getStudentDao().getByEmail(email);
    }

    @Override
    public void deleteAccount(Student student) throws EntityNotFoundException {

        getStudentDao().delete(student.getId());
    }

    @Override
    public void deleteAccount(Professor professor) throws EntityNotFoundException {

        getProfessorDao().delete(professor.getId());
    }

    @Override
    public List<Student> getAllStudents() {

        return getStudentDao().readAll();
    }

    @Override
    public List<Student> getAllStudents(Course course) {

        return new ArrayList<>(course.getStudents());
    }

    @Override
    public List<Professor> getAllProfessors() {

        return getProfessorDao().readAll();
    }

    @Override
    public List<Course> getAllCourses() {

        return getCourseDao().readAll();
    }

    @Override
    public List<Course> getAllCourses(Professor professor) throws NoResultException {

        return getCourseDao().readAllByProfId(professor.getId());
    }

    @Override
    public Course createCourse(String tittle, Professor professor) {

        Course course = Course.builder()
                            .title(tittle)
                            .professor(professor)
                            .build();
        return getCourseDao().create(course);
    }

    @Override
    public void updateCourse(Course course, Professor professor) {

        if (course != null) {
            course.setProfessor(professor);
            getCourseDao().update(course);
        }
    }

    @Override
    public void updateCourse(Course course, String tittle) {

        if (course != null) {
            course.setTitle(tittle);
            getCourseDao().update(course);
        }
    }

    @Override
    public void deleteCourse(Course course) throws EntityNotFoundException {

        if (course != null) {
            getCourseDao().delete(course.getId());
        }
    }

    @Override
    public List<Course> getCourseByTitleAndProfEmail(String title, String email) throws NoResultException {

        return getCourseDao().getCourseByTitleAndEmail(title, email);
    }

    @Override
    public void clearBaseFromSolutionsWithoutStudentIdAndTaskId() {

        getSolutionDao().deleteSolutionsWithoutStudentIdAndTaskId();
    }
}
