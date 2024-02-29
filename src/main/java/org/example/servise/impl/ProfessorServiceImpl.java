package org.example.servise.impl;

import org.apache.log4j.Logger;
import org.example.pojo.*;
import org.example.servise.ProfessorService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.Constants.OBJECT_CREATED_MESSAGE;
import static org.example.utils.Constants.OBJECT_UPDATED_MESSAGE;

public class ProfessorServiceImpl extends ParentService implements ProfessorService {
    private final Logger log = Logger.getLogger(ProfessorServiceImpl.class);

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
        return new ArrayList<>(course.getStudents());
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
        log.info(String.format(OBJECT_CREATED_MESSAGE, task));
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
        log.info(String.format(OBJECT_UPDATED_MESSAGE, solution));
        getSolutionDao().update(solution);
    }
}
