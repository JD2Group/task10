package org.example.service.impl;

import lombok.Getter;
import org.example.excepion.Exceptions;
import org.example.pojo.Course;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;
import org.example.servise.StudentService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

@Getter
public class StudentServiceImpl extends ParrentService implements StudentService {

    @Override
    public Student getStudentByEmail(String email) throws NoResultException {

        return getStudentDao().getByEmail(email);
    }

    @Override
    public List<Course> getAllCourses() {

        return getCourseDao().readAll();
    }

    @Override
    public List<Course> getMyCourses(Student student) throws NoResultException {

        if (student != null) {
            return getStudentDao().readAllCoursesByStudentId(student.getId());
        } else {
            throw new NoResultException();
        }
    }

    @Override
    public void checkInCourse(Course course, Student student) {

        if (course != null && student != null) {
            student.addCourse(course);
            getStudentDao().update(student);
        }
    }

    @Override
    public void checkOutCourse(Course course, Student student) {

        if (course != null && student != null) {
            student.getCourses().remove(course);
            getStudentDao().update(student);
        }
    }

    @Override
    public List<Task> getTasksFromCourse(Course course) throws NoResultException {

        if (course != null) {
            return getTaskDao().readAllByCourseId(course.getId());
        } else {
            throw new NoResultException();
        }
    }

    @Override
    public List<Task> getAllMyTasks(Student student) throws NoResultException {

        if (student != null) {
            return getStudentDao().readTasksByStudentId(student.getId());
        } else {
            throw new NoResultException();
        }
    }

    @Override
    public Solution getSolution(Task task, Student student) throws NoResultException {

        if (student != null && task != null) {
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
        } else {
            throw new NoResultException();
        }
    }

    @Override
    public void solveTask(Solution solution, boolean readyForReview, String response) throws Exceptions.SolutionIsResolvedException, EntityNotFoundException {

        if (solution != null) {
            if (solution.getMark() == null && solution.getReview() == null) {
                solution.setResponse(response);
                solution.setReadyForReview(readyForReview);
                getSolutionDao().update(solution);
            } else {
                throw new Exceptions.SolutionIsResolvedException();
            }
        } else {
            throw new EntityNotFoundException();
        }
    }
}
