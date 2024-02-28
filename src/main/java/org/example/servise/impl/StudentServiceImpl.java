package org.example.servise.impl;

import lombok.Getter;
import org.example.excepion.Exceptions;
import org.example.pojo.Course;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;
import org.example.servise.StudentService;

import javax.persistence.NoResultException;
import java.util.List;

@Getter
public class StudentServiceImpl extends ParrentService implements StudentService {

    @Override
    public Student getStudentByEmail(String email) {

        return getStudentDao().getByEmail(email);
    }

    @Override
    public List<Course> getAllCourses() {

        return getCourseDao().readAll();
    }

    @Override
    public List<Course> getMyCourses(Student student) throws NoResultException {

        return getStudentDao().readAllCoursesByStudentId(student.getId());
    }

    @Override
    public void checkInCourse(Course course, Student student) {

        student.addCourse(course);
        getStudentDao().update(student);
    }

    @Override
    public void checkOutCourse(Course course, Student student) {

        student.getCourses().remove(course);
        getStudentDao().update(student);
    }

    @Override
    public List<Task> getTasksFromCourse(Course course) throws NoResultException {

        return getTaskDao().readAllByCourseId(course.getId());
    }

    @Override
    public List<Task> getAllMyTasks(Student student) throws NoResultException {

        return getStudentDao().readTasksByStudentId(student.getId());
    }

    @Override
    public Solution getSolution(Task task, Student student) {

        Solution solution;
        try {
            solution = getSolutionDao().getByStudentTask(student.getId(), task.getId());
        } catch (NoResultException e) {
            solution = getSolutionDao().create(Solution.builder()
                                                   .task(task)
                                                   .student(student)
                                                   .build());
        }


        return solution;
    }

    @Override
    public void solveTask(Solution solution, boolean readyForReview, String response) throws Exceptions.SolutionIsResolvedException {

        if (solution.getMark() == null && solution.getReview() == null) {
            solution.setResponse(response);
            solution.setReadyForReview(readyForReview);
            getSolutionDao().update(solution);
        } else {
            throw new Exceptions.SolutionIsResolvedException();
        }
    }
}
