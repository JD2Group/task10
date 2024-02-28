package org.example.service.impl;

import org.example.pojo.*;
import org.example.servise.ProfessorService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

public class ProfessorServiceImpl extends ParrentService implements ProfessorService {

    @Override
    public Professor getProfessorByEmail(String email) throws NoResultException {

        return getProfessorDao().getByEmail(email);
    }

    @Override
    public List<Course> getMyCourses(Professor professor) throws NoResultException {

        if (professor != null) {
            return getCourseDao().readAllByProfId(professor.getId());
        } else {
            throw new NoResultException();
        }
    }

    @Override
    public List<Task> getAllTasks(Course course) throws EntityNotFoundException {

        if (course != null) {
            return getTaskDao().readAllByCourseId(course.getId());
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Student> getAllStudents(Course course) throws EntityNotFoundException {

        if (course != null) {
            return getStudentDao().getAllStudentsByCourse(course);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Solution getSolution(Student student, Task task) throws NoResultException {

        if (student != null && task != null) {
            return getSolutionDao().getByStudentTask(student.getId(), task.getId());
        } else {
            throw new NoResultException();
        }
    }


    @Override
    public List<Solution> getAllReadySolutions(Task task) throws EntityNotFoundException {

        if (task != null) {
            return getSolutionDao().getAllReadyByTask(task);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Task addTask(Course course, String title, String description) throws EntityNotFoundException {

        if (course != null) {
            Task task = Task.builder()
                            .title(title)
                            .description(description)
                            .course(course)
                            .build();
            return getTaskDao().update(task);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public Task updateTask(Task task, String title, String description) throws EntityNotFoundException {

        if (task != null) {
            task.setTitle(title);
            task.setDescription(description);
            return getTaskDao().update(task);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteTask(Task task) throws EntityNotFoundException {

        if (task != null) {
            getTaskDao().delete(task.getId());
        }
    }

    @Override
    public void review(Solution solution, Integer mark, String review) throws EntityNotFoundException {

        if (solution != null) {
            solution.setMark(mark);
            solution.setReview(review);
            getSolutionDao().update(solution);
        } else {
            throw new EntityNotFoundException();
        }
    }
}
