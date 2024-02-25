package org.example.utils;

import org.example.dao.*;
import org.example.dao.impl.*;
import org.example.pojo.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import static org.example.utils.MockConstants.*;
import static org.example.utils.MockConstants.DELETE_ALL_STUDENTS;

public class QueryManager {

    public static void createTestData(String professorEmail) {
        CourseDao courseDao = new CourseDaoImpl();
        StudentDao studentDao = new StudentDaoImpl();
        ProfessorDao professorDao = new ProfessorDaoImpl();
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
            s.setCourses(new HashSet<>(courses.subList(0, RANDOM.nextInt(courses.size() - 1) + 1)));
            studentDao.create(s);
        });

        MockUtils.generateRandomTasks().forEach(t -> {
            t.setCourse(courses.get(RANDOM.nextInt(courses.size())));
            taskDao.create(t);
            Solution solution = MockUtils.generateSolution();
            solution.setTask(t);
            solution.setStudent(students.get(RANDOM.nextInt(students.size())));
            solution.setReadyForReview(RANDOM.nextInt(MAX_RANDOM_NUMBER) % 2 == 0);
            solutionDao.create(solution);
        });

        courseDao.closeManager();
        studentDao.closeManager();
        professorDao.closeManager();
        taskDao.closeManager();
        solutionDao.closeManager();
    }

    public static void deleteAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query deleteAllSolutions = manager.createNativeQuery(DELETE_ALL_SOLUTIONS);
        deleteAllSolutions.executeUpdate();
        manager.flush();
        Query deleteAllTasks = manager.createNativeQuery(DELETE_ALL_TASK);
        deleteAllTasks.executeUpdate();
        manager.flush();
        Query deleteAllFromStudentsCourses = manager.createNativeQuery(DELETE_ALL_STUDENTS_COURSES);
        deleteAllFromStudentsCourses.executeUpdate();
        manager.flush();
        Query deleteAll = manager.createNativeQuery(DELETE_ALL_COURSES);
        deleteAll.executeUpdate();
        manager.flush();
        Query deleteAllProfessors = manager.createNativeQuery(DELETE_ALL_PROFESSORS);
        deleteAllProfessors.executeUpdate();
        manager.flush();
        Query deleteAllStudents = manager.createNativeQuery(DELETE_ALL_STUDENTS);
        deleteAllStudents.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    public static int getNumberOfObjects(String query) {
        return getNumberOfObjects(query, null);
    }

    public static int getNumberOfObjects(String query, Long id) {
        EntityManager manager = HibernateUtil.getEntityManager();
        Query numberOfStudentsQuery = manager.createNativeQuery(query);
        if (id != null) {
            numberOfStudentsQuery.setParameter(1, id);
        }
        BigInteger result = (BigInteger) numberOfStudentsQuery.getSingleResult();

        return result.intValue();
    }
}
