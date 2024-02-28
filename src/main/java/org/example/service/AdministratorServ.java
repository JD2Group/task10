package org.example.service;

import org.example.DTO.request.CourseDTORequest;
import org.example.DTO.request.CourseUpdateDTORequest;
import org.example.DTO.request.ProfessorUpdateDTORequest;
import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.pojo.Student;

import java.util.List;

public interface AdministratorServ {


    //TODO Вообще не понял вот этих методов, сущность админа нигде не хранится,
    // в персоне должно быть поле с сетом ролей, а роли вынесены в enum (admin, professor, student) это потребуется для
    // разгроничения прав на использование данных методов, но в условиях данной задачи, аунтентификация не производится,
    // как и авторизация.
    /*<T> Long createAccount(String name, String surname, Class<T> clazz);//return id of new account

    <T> void deleteAccount(Long id, Class<T> clazz);

    <T> void deleteAccount(String name, String surname, Class<T> clazz);*/

    //Get all student
    List<Student> getAllStudents();
    //Get all student on specific course
    List<Student> getAllStudents(CourseDTORequest courseDTORequest);
    //Get all existed professors
    List<Professor> getAllProfessors();
    //Remove professor by email. First step: Find; Second step: Delete.
    void deleteProfessor(String email);
    //Find by email. Update by arg entity professor.
    void updateProfessor(ProfessorUpdateDTORequest professorUpdateDTORequest);
    //Get all existed courses.
    List<Course> getAllCourses();
    //Find all courses by professor_name and professor_surname
    List<Course> getAllCourses(String email);
    //Build course entity outside and save it.
    Course createCourse(CourseDTORequest courseDTORequest);
    Course getCourse(CourseDTORequest courseDTORequest);

    //First step: Find course by title and professor_name. Second step: Get updated course entity and merge it.
    void updateCourseTitle(CourseUpdateDTORequest courseUpdateDTORequest);

    //Get course by title and prof, add student to list, upload updated entity to database
    void addStudentToCourse(CourseDTORequest courseDTORequest, Student student);
    //Common delete course by title and prof name
    void deleteCourse(CourseDTORequest courseDTORequest);
    //Uncommon delete course by course id
    void deleteCourse(Long id);

}
