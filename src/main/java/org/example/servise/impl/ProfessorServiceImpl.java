package org.example.servise.impl;

import org.example.pojo.*;
import org.example.servise.ProfessorServ;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorServiceImpl extends ParrentService implements ProfessorServ {

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

        return new ArrayList<>(course.getStudents());
    }

    @Override
    public Solution getSolution(Student student, Task task) throws EntityNotFoundException {

        return getSolutionDao().getByStudentTask(student, task);
    }


    @Override
    public List<Solution> getAllReadySolutions(Task task) {

        return getSolutionDao().getAllReadyByTask(task);
    }

    @Override
    public void addTasks(Course course, Task task) {

        task.setCourse(course);
        getTaskDao().update(task);
    }

    @Override
    public void updateTask(Task task, String title, String description) {

        task.setTitle(title);
        task.setDescription(description);
        getTaskDao().update(task);
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
