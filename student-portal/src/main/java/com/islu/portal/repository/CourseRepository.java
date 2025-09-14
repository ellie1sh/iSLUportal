package com.islu.portal.repository;

import com.islu.portal.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c JOIN Enrollment e ON c.id = e.course.id WHERE e.student.studentId = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") String studentId);
}

