package org.example.service;

import org.example.pojo.*;

import java.util.List;

public interface ProfessorServ {


    List<Course> getMyCourses(Professor professor);

    List<Task> getAllTasks(Course course);

    List<Student> getAllStudents(Course course);


    List<Solution> getAllSolutions(Course course);

    List<Solution> getAllSolutions(Task task);

    List<Solution> getAllSolutions(Course course, Student student);

    Solution getSolution(Student student, Task task);


    List<Solution> getAllReadySolutions(Course course);

    List<Solution> getAllReadySolutions(Course course, Student student);

    List<Solution> getAllReadySolutions(Task task);


    void addTasks(Course course, Task task);

    void updateTask(Task task, String title, String description);

    void deleteTask(Long taskId);


    void review(Solution solution, Integer mark, String review);
}
