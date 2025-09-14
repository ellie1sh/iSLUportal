package entity;

/**
 * Enrollment entity class adapted for ISLU Student Portal
 * Represents the relationship between a student and a course
 */
public class Enrollment {
    private Long id;
    private Student student;
    private Course course;
    
    // Constructors
    public Enrollment() {}
    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    /**
     * Returns enrollment data as an array for table display
     * @return Object array for JTable
     */
    public Object[] toTableRow() {
        return new Object[]{
            student != null ? student.getStudentId() : "N/A",
            student != null ? student.getFullName() : "N/A",
            course != null ? course.getClassCode() : "N/A",
            course != null ? course.getCourseNumber() : "N/A",
            course != null ? course.getCourseDescription() : "N/A",
            course != null ? course.getUnits() : 0
        };
    }
    
    @Override
    public String toString() {
        return String.format("Enrollment: %s enrolled in %s", 
            student != null ? student.getFullName() : "Unknown Student",
            course != null ? course.getCourseNumber() : "Unknown Course");
    }
}