package org.example.service;

import org.example.pojo.Course;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;

import java.util.List;

public interface StudentServ {


    List<Course> getMyCourses(String email);

    //Не пон
    void checkInCourse(Course course, Student student);
    //Не пон
    void checkOutCourse(Course course, Student student);


    List<Task> getAllMyTasks(String prof_email, String course_title, String student_email);

    List<Task> getAllMyTasks(String student_email);


    Solution createSolution(String task_id, String student_email);

    Solution getSolution(String task_id, String student_email);


    Solution solveTask(String solution_id, boolean readyForReview, String response);
}
