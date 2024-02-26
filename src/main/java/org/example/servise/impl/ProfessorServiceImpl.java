package org.example.servise.impl;

import org.example.pojo.*;
import org.example.servise.ProfessorService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

public class ProfessorServiceImpl extends ParrentService implements ProfessorService {

    @Override
    public Professor getProfessorByEmail(String email) {

        return getProfessorDao().getByEmail(email);
    }

    @Override
    public List<Course> getMyCourses(Professor professor) throws NoResultException {

        return getCourseDao().readAllByProfId(professor.getId());
    }

    @Override
    public List<Task> getAllTasks(Course course) throws NoResultException {

        return getTaskDao().readAllByCourseId(course.getId());
    }

    @Override
    public List<Student> getAllStudents(Course course) {

        return getStudentDao().getAllStudentsByCourse(course);
    }

    @Override
    public Solution getSolution(Student student, Task task) throws EntityNotFoundException {

        return getSolutionDao().getByStudentTask(student.getId(), task.getId());
    }


    @Override
    public List<Solution> getAllReadySolutions(Task task) {

        return getSolutionDao().getAllReadyByTask(task);
    }

    @Override
    public Task addTask(Course course, String title, String description) {

        Task task = Task.builder()
                        .title(title)
                        .description(description)
                        .course(course)
                        .build();
        return getTaskDao().update(task);
    }

    @Override
    public Task updateTask(Task task, String title, String description) {

        task.setTitle(title);
        task.setDescription(description);
        return getTaskDao().update(task);
    }

    @Override
    public void deleteTask(Task task) throws EntityNotFoundException {

        getTaskDao().delete(task.getId());
    }

    @Override
    public void review(Solution solution, Integer mark, String review) {

        solution.setMark(mark);
        solution.setReview(review);
        getSolutionDao().update(solution);
    }
}
