package org.example.servise;

import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.pojo.Student;

import java.util.List;

public interface AdministratorServ {


    Professor createProfessorAccount(String name, String surname, String email);

    Student createStudentAccount(String name, String surname, String email);

    Professor getProfessorByEmail(String email);

    Student getStudentByEmail(String email);

    void deleteAccount(Student student);

    void deleteAccount(Professor professor);

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
