package org.example.service.impl;

import org.example.dao.*;
import org.example.dao.impl.*;


public class ParrentService {

    private static StudentDao studentDao = new StudentDaoImpl();
    private static CourseDao courseDao = new CourseDaoImpl();
    private static TaskDao taskDao = new TaskDaoImpl();
    private static SolutionDao solutionDao = new SolutionDaoImpl();
    private static ProfessorDao professorDao = new ProfessorDaoImpl();

    protected StudentDao getStudentDao() {
        return studentDao;
    }

    protected CourseDao getCourseDao() {
        return courseDao;
    }

    protected TaskDao getTaskDao() {
        return taskDao;
    }

    protected SolutionDao getSolutionDao() {
        return solutionDao;
    }

    protected ProfessorDao getProfessorDao() {
        return professorDao;
    }
}
