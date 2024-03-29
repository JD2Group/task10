package org.example.service;

import org.example.excepion.Exceptions;
import org.example.pojo.Course;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;

import javax.persistence.NoResultException;
import java.util.List;

public interface StudentService {

    Student getStudentByEmail(String Email);

    List<Course> getAllCourses();

    List<Course> getMyCourses(Student student) throws NoResultException;


    void checkInCourse(Course course, Student student);

    void checkOutCourse(Course course, Student student);


    List<Task> getTasksFromCourse(Course course) throws NoResultException;

    List<Task> getAllMyTasks(Student student) throws NoResultException;

    Solution getSolution(Task task, Student student);


    void solveTask(Solution solution, boolean readyForReview, String response) throws Exceptions.SolutionIsResolvedException;
}
