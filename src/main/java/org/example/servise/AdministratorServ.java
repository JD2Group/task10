package org.example.servise;

import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.pojo.Student;

import java.util.List;

public interface AdministratorServ {


    <T> Long createAccount(String name, String surname, Class<T> clazz);//return id of new account

    <T> void deleteAccount(Long id, Class<T> clazz);

    <T> void deleteAccount(String name, String surname, Class<T> clazz);


    List<Student> getAllStudents();

    List<Student> getAllStudents(Course course);

    List<Professor> getAllProfessors();

    List<Course> getAllCourses();

    List<Course> getAllCourses(Professor professor);


    Course createCourse(String tittle, Professor professor);

    void updateCourse(Course course, Professor professor);

    void updateCourse(Course course, String tittle);

    void deleteCourse(Course course);
}
