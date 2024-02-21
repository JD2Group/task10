package org.example.servise;

import org.example.pojo.Course;
import org.example.pojo.Professor;

import java.util.List;

public interface AdministratorServ {


    <T> Long createAccount(String name, String surname, Class<T> clazz);//return id of new account
    <T> void deleteAccount(Long id, Class<T> clazz);
    <T> void deleteAccount(String name, String surname, Class<T> clazz);

    Long createCourse(String tittle, Professor professor);//return id of new course
    List<Course> getAllCourses();
    Course getCourse(Professor professor);
    Course getCourse(String tittle);
    void updateCourse(Course course,Professor professor);
    void updateCourse(Course course,String tittle);
    void deleteCourse(Long id);


}
