package org.example.servise;

import org.example.pojo.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

public interface ProfessorService {

    Professor getProfessorByEmail(String Email);

    List<Course> getMyCourses(Professor professor);

    List<Task> getAllTasks(Course course) throws NoResultException;

    List<Student> getAllStudents(Course course);


    Solution getSolution(Student student, Task task) throws EntityNotFoundException;

    List<Solution> getAllReadySolutions(Task task);


    Task addTask(Course course, String title, String description);

    Task updateTask(Task task, String title, String description);

    void deleteTask(Task task) throws EntityNotFoundException;


    void review(Solution solution, Integer mark, String review);
}
