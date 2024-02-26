package org.example.dao;

import org.example.pojo.Course;

import javax.persistence.NoResultException;
import java.util.List;

public interface CourseDao extends DAO<Course, Long> {

    long DELETED_COURSE_ID = 1L;

    List<Course> readAllByProfId(Long id) throws NoResultException;

    List<Course> getCourseByTitleAndEmail(String title, String profEmail) throws NoResultException;
}
