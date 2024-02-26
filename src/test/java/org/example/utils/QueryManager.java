package org.example.utils;

import org.example.dao.*;
import org.example.dao.impl.*;
import org.example.pojo.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

import static org.example.utils.MockConstants.*;
import static org.example.utils.MockConstants.DELETE_ALL_STUDENTS;

public class QueryManager {
    private EntityManager manager;

    public QueryManager() {
        this.manager = HibernateUtil.getEntityManager();
    }

    public void createTestData(String professorEmail) {
        ProfessorDao professorDao = new ProfessorDaoImpl();
        CourseDao courseDao = new CourseDaoImpl();
        StudentDao studentDao = new StudentDaoImpl();
        TaskDao taskDao = new TaskDaoImpl();
        SolutionDao solutionDao = new SolutionDaoImpl();

        Professor professor = MockUtils.generateProfessor();
        professor.setEmail(professorEmail);
        professorDao.create(professor);

        List<Course> courses = MockUtils.generateRandomCourses();
        courses.forEach(c -> {
            courseDao.create(c);
            c.setProfessor(professor);
            courseDao.update(c);
        });

        List<Student> students = MockUtils.generateRandomStudents();
        students.forEach(s -> {
            courses.forEach(s::addCourse);
            studentDao.create(s);
        });

        IntStream.range(0, LIST_SIZE).forEach(i -> MockUtils.generateRandomTasks().forEach(t -> {
                    t.setCourse(courses.get(i));
                    taskDao.create(t);
                    Solution solution = MockUtils.generateSolution();
                    solution.setTask(t);
                    solution.setStudent(students.get(i));
                    solution.setReadyForReview(i % 2 == 0);
                    solutionDao.create(solution);
                }));

        professorDao.closeManager();
        courseDao.closeManager();
        studentDao.closeManager();
        taskDao.closeManager();
        solutionDao.closeManager();
    }

    public void deleteAll() {
        checkEntityManager();
        manager.getTransaction().begin();
        executeUpdateQuery(DELETE_ALL_SOLUTIONS);
        executeUpdateQuery(DELETE_ALL_TASK);
        executeUpdateQuery(DELETE_ALL_STUDENTS_COURSES);
        executeUpdateQuery(DELETE_ALL_COURSES);
        executeUpdateQuery(DELETE_ALL_PROFESSORS);
        executeUpdateQuery(DELETE_ALL_STUDENTS);
        manager.getTransaction().commit();
        manager.close();
    }

    private void executeUpdateQuery(String query) {
        Query deleteQuery = manager.createNativeQuery(query);
        deleteQuery.executeUpdate();
        manager.flush();
    }

    public int getNumberOfObjects(String query, Long... id) {
        checkEntityManager();
        Query selectNumberOfObjects = manager.createNativeQuery(query);
        for (int i = 1; i <= id.length; i++) {
            selectNumberOfObjects.setParameter(i, id[i - 1]);
        }
        BigInteger result = (BigInteger) selectNumberOfObjects.getSingleResult();
        return result.intValue();
    }

    private void checkEntityManager() {
        if (!manager.isOpen()) {
            manager = HibernateUtil.getEntityManager();
        }
    }

    public void closeSession() {
        if (manager.isOpen()) {
            manager.close();
        }
    }
}
