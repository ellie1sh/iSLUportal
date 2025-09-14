package com.islu.portal.config;

import com.islu.portal.entity.Course;
import com.islu.portal.entity.Enrollment;
import com.islu.portal.entity.Student;
import com.islu.portal.repository.CourseRepository;
import com.islu.portal.repository.EnrollmentRepository;
import com.islu.portal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (studentRepository.findByStudentId("STUDENT123").isPresent()) {
            return;
        }

        Student student = new Student("STUDENT123", "SHERLIE O. RIVERA", "BSIT 2-3", "2025-2026", "FIRST SEMESTER");
        student = studentRepository.save(student);

        Course[] courses = new Course[]{
            new Course("7024", "NSTP-CWTS 1", "FOUNDATIONS OF SERVICE", 3,
                    LocalTime.of(13, 30), LocalTime.of(14, 30), "MWF", "D906"),
            new Course("9454", "GSTS", "SCIENCE, TECHNOLOGY, AND SOCIETY", 3,
                    LocalTime.of(9, 30), LocalTime.of(10, 30), "TTHS", "D504"),
            new Course("9455", "GENVI", "ENVIRONMENTAL SCIENCE", 3,
                    LocalTime.of(9, 30), LocalTime.of(10, 30), "MWF", "D503"),
            new Course("9456", "CFE 103", "CATHOLIC FOUNDATION OF MISSION", 3,
                    LocalTime.of(13, 30), LocalTime.of(14, 30), "TTHS", "D503"),
            new Course("9457", "IT 211", "REQUIREMENTS ANALYSIS AND MODELING", 3,
                    LocalTime.of(10, 30), LocalTime.of(11, 30), "MWF", "D511"),
            new Course("9458A", "IT 212", "DATA STRUCTURES (LEC)", 2,
                    LocalTime.of(14, 30), LocalTime.of(15, 30), "TF", "D513"),
            new Course("9458B", "IT 212L", "DATA STRUCTURES (LAB)", 1,
                    LocalTime.of(16, 0), LocalTime.of(17, 30), "TF", "D522"),
            new Course("9459A", "IT 213", "NETWORK FUNDAMENTALS (LEC)", 2,
                    LocalTime.of(8, 30), LocalTime.of(9, 30), "TF", "D513"),
            new Course("9459B", "IT 213L", "NETWORK FUNDAMENTALS (LAB)", 1,
                    LocalTime.of(11, 30), LocalTime.of(13, 0), "TF", "D528"),
            new Course("9547", "FIT OA", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (OUTDOOR AND ADVENTURE ACTIVITIES)", 2,
                    LocalTime.of(15, 30), LocalTime.of(17, 30), "TH", "D221")
        };

        for (Course course : courses) {
            Course savedCourse = courseRepository.save(course);
            Enrollment enrollment = new Enrollment(student, savedCourse);
            enrollmentRepository.save(enrollment);
        }

        System.out.println("Sample data initialized successfully!");
    }
}

