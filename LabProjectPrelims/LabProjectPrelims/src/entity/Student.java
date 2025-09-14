package entity;

import java.util.List;
import java.util.ArrayList;

/**
 * Student entity class adapted for ISLU Student Portal
 * Contains student information and enrollment details
 */
public class Student {
    private Long id;
    private String studentId;
    private String fullName;
    private String block;
    private String academicYear;
    private String semester;
    private List<Enrollment> enrollments;
    
    // Constructors
    public Student() {
        this.enrollments = new ArrayList<>();
    }
    
    public Student(String studentId, String fullName, String block, String academicYear, String semester) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.block = block;
        this.academicYear = academicYear;
        this.semester = semester;
        this.enrollments = new ArrayList<>();
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
    
    /**
     * Add an enrollment to this student
     * @param enrollment The enrollment to add
     */
    public void addEnrollment(Enrollment enrollment) {
        if (enrollments == null) {
            enrollments = new ArrayList<>();
        }
        enrollments.add(enrollment);
        enrollment.setStudent(this);
    }
    
    /**
     * Get all courses this student is enrolled in
     * @return List of courses
     */
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        if (enrollments != null) {
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourse() != null) {
                    courses.add(enrollment.getCourse());
                }
            }
        }
        return courses;
    }
    
    /**
     * Calculate total units for enrolled courses
     * @return Total units
     */
    public int getTotalUnits() {
        return getCourses().stream()
                          .mapToInt(Course::getUnits)
                          .sum();
    }
    
    /**
     * Returns student data as an array for table display
     * @return Object array for JTable
     */
    public Object[] toTableRow() {
        return new Object[]{
            studentId,
            fullName,
            block,
            academicYear,
            semester,
            getTotalUnits()
        };
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s) - %s %s", 
            studentId, fullName, block, academicYear, semester);
    }
}