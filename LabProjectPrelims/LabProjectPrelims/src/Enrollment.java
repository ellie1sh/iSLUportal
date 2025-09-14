/**
 * Entity class representing the relationship between a student and a course (enrollment)
 * Maintains the many-to-many relationship between students and courses
 */
public class Enrollment {
    private Long id;
    private Student student;
    private Course course;
    private String enrollmentDate;
    private String status; // ENROLLED, DROPPED, COMPLETED
    
    // Constructors
    public Enrollment() {
        this.status = "ENROLLED"; // Default status
    }
    
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.status = "ENROLLED";
        this.enrollmentDate = getCurrentDate();
    }
    
    public Enrollment(Long id, Student student, Course course, String enrollmentDate, String status) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    
    public String getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(String enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    /**
     * Checks if this enrollment is active (status is ENROLLED)
     * @return true if enrollment is active
     */
    public boolean isActive() {
        return "ENROLLED".equals(status);
    }
    
    /**
     * Gets the student ID associated with this enrollment
     * @return Student ID string
     */
    public String getStudentId() {
        return student != null ? student.getStudentId() : null;
    }
    
    /**
     * Gets the course code associated with this enrollment
     * @return Course code string
     */
    public String getCourseCode() {
        return course != null ? course.getClassCode() : null;
    }
    
    /**
     * Gets the course ID associated with this enrollment
     * @return Course ID
     */
    public Long getCourseId() {
        return course != null ? course.getId() : null;
    }
    
    /**
     * Gets the student's internal ID associated with this enrollment
     * @return Student internal ID
     */
    public Long getStudentInternalId() {
        return student != null ? student.getId() : null;
    }
    
    /**
     * Gets a display string for this enrollment
     * @return Formatted enrollment string
     */
    public String getDisplayString() {
        StringBuilder display = new StringBuilder();
        
        if (student != null && student.getFullName() != null) {
            display.append(student.getFullName());
        }
        
        if (course != null && course.getFullCourseName() != null) {
            if (display.length() > 0) {
                display.append(" - ");
            }
            display.append(course.getFullCourseName());
        }
        
        if (status != null && !"ENROLLED".equals(status)) {
            if (display.length() > 0) {
                display.append(" (").append(status).append(")");
            }
        }
        
        return display.toString();
    }
    
    /**
     * Converts enrollment data to database format for persistence
     * Format: id|studentId|courseId|enrollmentDate|status
     * @return Database format string
     */
    public String toDatabaseFormat() {
        return String.join("|",
            id != null ? id.toString() : "",
            getStudentId() != null ? getStudentId() : "",
            getCourseId() != null ? getCourseId().toString() : "",
            enrollmentDate != null ? enrollmentDate : "",
            status != null ? status : "ENROLLED"
        );
    }
    
    /**
     * Creates an Enrollment object from database format string
     * Note: This method creates a partial Enrollment with IDs only.
     * Full Student and Course objects need to be loaded separately.
     * @param databaseString Database format string
     * @return Enrollment object or null if parsing fails
     */
    public static Enrollment fromDatabaseFormat(String databaseString) {
        try {
            String[] parts = databaseString.split("\\|", -1); // -1 to include empty trailing strings
            if (parts.length < 5) {
                return null;
            }
            
            Enrollment enrollment = new Enrollment();
            enrollment.setId(parts[0].isEmpty() ? null : Long.parseLong(parts[0]));
            // Student and Course objects will be set later when loading from database
            enrollment.setEnrollmentDate(parts[3].isEmpty() ? null : parts[3]);
            enrollment.setStatus(parts[4].isEmpty() ? "ENROLLED" : parts[4]);
            
            return enrollment;
        } catch (Exception e) {
            System.err.println("Error parsing enrollment from database format: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a simplified database format with just IDs for storage
     * Format: id|studentId|courseId|enrollmentDate|status
     * @param studentId Student ID
     * @param courseId Course ID
     * @return Database format string
     */
    public String toDatabaseFormatWithIds(String studentId, Long courseId) {
        return String.join("|",
            id != null ? id.toString() : "",
            studentId != null ? studentId : "",
            courseId != null ? courseId.toString() : "",
            enrollmentDate != null ? enrollmentDate : "",
            status != null ? status : "ENROLLED"
        );
    }
    
    /**
     * Gets current date in a simple format
     * @return Current date string
     */
    private String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }
    
    /**
     * Validates that this enrollment has all required fields
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return student != null && course != null && status != null;
    }
    
    /**
     * Drops this enrollment (sets status to DROPPED)
     */
    public void drop() {
        this.status = "DROPPED";
    }
    
    /**
     * Completes this enrollment (sets status to COMPLETED)
     */
    public void complete() {
        this.status = "COMPLETED";
    }
    
    /**
     * Reactivates this enrollment (sets status back to ENROLLED)
     */
    public void reactivate() {
        this.status = "ENROLLED";
    }
    
    @Override
    public String toString() {
        return String.format("Enrollment{id=%d, studentId='%s', courseCode='%s', enrollmentDate='%s', status='%s'}",
            id, getStudentId(), getCourseCode(), enrollmentDate, status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Enrollment that = (Enrollment) obj;
        
        if (id != null && that.id != null) {
            return id.equals(that.id);
        }
        
        // If no IDs, compare by student and course
        return getStudentId() != null && getStudentId().equals(that.getStudentId()) &&
               getCourseId() != null && getCourseId().equals(that.getCourseId());
    }
    
    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        
        int result = getStudentId() != null ? getStudentId().hashCode() : 0;
        result = 31 * result + (getCourseId() != null ? getCourseId().hashCode() : 0);
        return result;
    }
}