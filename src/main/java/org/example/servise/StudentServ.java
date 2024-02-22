package org.example.servise;

import org.example.pojo.Course;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;

import java.util.List;

public interface StudentServ {


    List<Course> getAllCourses();

    List<Course> getMyCourses(Student student);


    void checkInCourse(Course course, Student student);

    void checkOutCourse(Course course, Student student);


    List<Task> getAllMyTasks(Course course, Student student);

    List<Task> getAllMyTasks(Student student);


    Solution createSolution(Task task, Student student);

    Solution getSolution(Task task, Student student);


    Solution solveTask(Solution solution, boolean readyForReview, String response);
}
