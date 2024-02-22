package org.example.service;

import org.example.DTO.request.CourseDTORequest;
import org.example.DTO.request.CourseUpdateDTORequest;
import org.example.DTO.request.ProfessorUpdateDTORequest;
import org.example.dao.CourseDao;
import org.example.dao.ProfessorDao;
import org.example.dao.StudentDao;
import org.example.dao.impl.CourseDaoImpl;
import org.example.dao.impl.ProfessorDaoImpl;
import org.example.dao.impl.StudentDaoImpl;
import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.pojo.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdministratorServImpl implements AdministratorServ {

    ProfessorDao professorDao = new ProfessorDaoImpl();
    CourseDao courseDao = new CourseDaoImpl();
    StudentDao studentDao = new StudentDaoImpl();

    @Override
    public List<Student> getAllStudents() {
        return studentDao.readAll();
    }

    @Override
    public List<Student> getAllStudents(CourseDTORequest courseDTORequest) {
        Course course = getCourse(courseDTORequest);
        return new ArrayList<>(course.getStudents());
    }

    @Override
    public List<Professor> getAllProfessors() {
        return professorDao.readAll();
    }

    @Override
    public void deleteProfessor(String email) {
        professorDao.deleteByEmail(email);
    }

    @Override
    public void updateProfessor(ProfessorUpdateDTORequest professorUpdateDTORequest) {
        Professor professor = professorDao.getByEmail(professorUpdateDTORequest.getOldEmail());
        Optional.ofNullable(professorUpdateDTORequest.getNewEmail()).ifPresent(professor::setEmail);
        Optional.ofNullable(professorUpdateDTORequest.getNewName()).ifPresent(professor::setName);
        Optional.ofNullable(professorUpdateDTORequest.getNewSurname()).ifPresent(professor::setSurname);
        professorDao.update(professor);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.readAll();
    }

    @Override
    public List<Course> getAllCourses(String prof_email) {
        Professor professor = professorDao.getByEmail(prof_email);
        return courseDao.readAllByProfId(professor.getId()); // Или можно черезе объект, как тебе там удобней будет.
    }

    @Override
    public Course createCourse(CourseDTORequest courseDTORequest) {

        Professor professor = professorDao.getByEmail(courseDTORequest.getProf_email());
        Course course = Course.builder()
                .professor(professor)
                .title(courseDTORequest.getTitle())
                .build();
        professor.getCourses().add(course);
        professorDao.update(professor);
        courseDao.create(course);
        return course;
    }

    @Override
    public Course getCourse(CourseDTORequest courseDTORequest) {
        List<Course> courseList = courseDao.getCourseByTitleAndEmail(courseDTORequest.getTitle(), courseDTORequest.getProf_email());
        return courseList.get(0); // Или ты на стороне дао приведешь мне к объекту, а не листу объектов.
    }

    @Override
    public void updateCourseTitle(CourseUpdateDTORequest courseUpdateDTORequest) {
        Course course = getCourse(new CourseDTORequest(courseUpdateDTORequest.getProfEmail(),
                courseUpdateDTORequest.getTitle()));
        Optional.ofNullable(courseUpdateDTORequest.getNewTitle()).ifPresent(course::setTitle);
        courseDao.update(course);
    }

    @Override
    public void addStudentToCourse(CourseDTORequest courseDTORequest, Student student) {
        //TODO Что-то тут не то, кажется
        // что поле на студентов в курсе лишнее, курсу так-то без разницы, сколько на нем студентов,
        // на занятии, да, мы делали так, но уточнялось, что подобная запись только в ознакомительных целях,
        // а так она редко используется.
        Course course = getCourse(courseDTORequest);
        course.getStudents().add(student);
        student.getCourses().add(course);
        courseDao.update(course);
        studentDao.update(student);
    }

    @Override
    public void deleteCourse(CourseDTORequest courseDTORequest) {
        Course course = getCourse(courseDTORequest);
        courseDao.delete(course.getId());
    }

    @Override
    public void deleteCourse(Long id) {
        courseDao.delete(id);
    }
}
