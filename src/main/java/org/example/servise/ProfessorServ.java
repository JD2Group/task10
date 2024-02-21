package org.example.servise;

import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.pojo.Solution;
import org.example.pojo.Task;

import java.util.List;

public interface ProfessorServ {

    List<Course> getMyCourses(Professor professor);
    List<Task> getTasksFromCourse(Course course);

    List<Task> addTasksToCourse(Course course, Task task);
    List<Task> removeTasksFromCourse(Long taskId);
    void updateTask(Task task, String title, String description);
    void deleteTask(Long taskId);

    List<Solution> getAllReadySolutions(Task task);
    void review(Solution solution, Integer mark, String review);


}
