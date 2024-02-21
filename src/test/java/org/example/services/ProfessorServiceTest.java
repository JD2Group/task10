package org.example.services;

import org.example.dao.CourseDao;
import org.example.dao.SolutionDao;
import org.example.dao.TaskDao;
import org.example.dao.impl.entityImpl.CourseDaoImpl;
import org.example.dao.impl.entityImpl.SolutionDaoImpl;
import org.example.dao.impl.entityImpl.TaskDaoImpl;
import org.example.pojo.Course;
import org.example.pojo.Solution;
import org.example.pojo.Task;
import org.example.utils.HibernateUtil;
import org.example.utils.MockUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProfessorServiceTest {
    private final CourseDao courseDao = new CourseDaoImpl();
    private final TaskDao taskDao = new TaskDaoImpl();
    private final SolutionDao solutionDao = new SolutionDaoImpl();
    private List<Course> courses;
    private List<Task> tasks;
    private Solution solution;

    @AfterAll
    public static void deleteAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query deleteAllCourses = manager.createNativeQuery(DELETE_ALL_TASK);
        deleteAllCourses.executeUpdate();
        manager.flush();
        Query deleteAllProfessors = manager.createNativeQuery(DELETE_ALL_COURSES);
        deleteAllProfessors.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    @BeforeEach
    public void createData() {
        CourseDao courseDao = new CourseDaoImpl();
        TaskDao taskDao = new TaskDaoImpl();
        SolutionDao solutionDao = new SolutionDaoImpl();
        courses = new ArrayList<>(MockUtils.generateRandomCourses());
        courses.forEach(courseDao::create);
        tasks = new ArrayList<>(MockUtils.generateRandomTasks());
        tasks.forEach(taskDao::create);
        solution = MockUtils.generateSolution();
        solutionDao.create(solution);
    }

    @Test
    public void addTasksTest() {
        Long randomId = courses.get(RANDOM.nextInt(courses.size())).getId();
        Course course = courseDao.read(randomId);
        tasks.forEach(t -> {
            t.setCourse(course);
            taskDao.update(t);
        });

        List<Long> actual = tasks.stream()
                .map(t -> taskDao.read(t.getId()).getCourse().getId())
                .collect(Collectors.toList());

        assertEquals(course.getId(), actual.get(0));
        assertEquals(course.getId(), actual.get(1));
        assertEquals(course.getId(), actual.get(2));
        assertEquals(course.getId(), actual.get(3));
        assertEquals(course.getId(), actual.get(4));
    }

    @Test
    public void deleteTasksTest() {
        Long randomId = courses.get(RANDOM.nextInt(courses.size())).getId();
        Course course = courseDao.read(randomId);
        Task task = tasks.get(0);
        task.setCourse(course);
        taskDao.update(task);

        taskDao.delete(task.getId());
        assertNull(taskDao.read(task.getId()));
    }

    @Test
    public void reviewTaskTest() {
        solution.setResponse(RESPONSE);
        solution.setMark(MARK);
        solutionDao.update(solution);
        String actualResponse = solutionDao.read(solution.getId()).getResponse();
        int actualMark = solutionDao.read(solution.getId()).getMark();

        assertEquals(RESPONSE, actualResponse);
        assertEquals(MARK, actualMark);
    }
}
