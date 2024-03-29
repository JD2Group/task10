package org.example.service.impl;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.example.excepion.Exceptions;
import org.example.pojo.Course;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;
import org.example.service.StudentService;

import javax.persistence.NoResultException;
import java.util.List;

import static org.example.utils.Constants.*;

@Getter
public class StudentServiceImpl extends ParentService implements StudentService {
    private final Logger log = Logger.getLogger(StudentServiceImpl.class);

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
        log.info(String.format(STUDENT_CHECK_IN_COURSE_MESSAGE, student.toString(), course.toString()));
        getStudentDao().update(student);
    }

    @Override
    public void checkOutCourse(Course course, Student student) {

        student.getCourses().remove(course);
        log.info(String.format(STUDENT_CHECK_OUT_COURSE_MESSAGE, student.toString(), course.toString()));
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
            log.info(String.format(OBJECT_CREATED_MESSAGE, solution));
        }


        return solution;
    }

    @Override
    public void solveTask(Solution solution, boolean readyForReview, String response) throws Exceptions.SolutionIsResolvedException {

        if (solution.getMark() == null && solution.getReview() == null) {
            solution.setResponse(response);
            solution.setReadyForReview(readyForReview);
            getSolutionDao().update(solution);
            log.info(String.format(OBJECT_UPDATED_MESSAGE, solution));
        } else {
            log.error(String.format(SOLUTION_RESOLVED_MESSAGE, solution.toString()));
            throw new Exceptions.SolutionIsResolvedException();
        }
    }
}
