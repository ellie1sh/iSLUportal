// StudentService.java - Business logic layer
package com.islu.portal.service;

import com.islu.portal.entity.Course;
import com.islu.portal.entity.Student;
import com.islu.portal.repository.CourseRepository;
import com.islu.portal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    public Optional<Student> getStudentById(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
    
    public Optional<Student> getStudentWithCourses(String studentId) {
        return studentRepository.findByStudentIdWithCourses(studentId);
    }
    
    public List<Course> getStudentCourses(String studentId) {
        return courseRepository.findCoursesByStudentId(studentId);
    }
    
    public int getTotalUnits(String studentId) {
        List<Course> courses = getStudentCourses(studentId);
        return courses.stream()
                     .mapToInt(Course::getUnits)
                     .sum();
    }
}