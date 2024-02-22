package org.example.dao;

import org.example.pojo.Course;

import java.util.List;

public interface CourseDao extends DAO<Course,Long> {

    List<Course> readAllByProfId(Long id);

    List<Course> getCourseByTitleAndEmail(String title, String profEmail);
}
