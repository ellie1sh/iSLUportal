// Student.java - Entity class for students
package com.islu.portal.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_id")
    private String studentId;
    
    @Column(name = "full_name")
    private String fullName;
    
    private String block;
    
    @Column(name = "academic_year")
    private String academicYear;
    
    private String semester;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments;
    
    // Constructors
    public Student() {}
    
    public Student(String studentId, String fullName, String block, String academicYear, String semester) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.block = block;
        this.academicYear = academicYear;
        this.semester = semester;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}