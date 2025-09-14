import java.util.List;
import java.util.ArrayList;

/**
 * Extended Student entity class that builds upon the existing StudentInfo structure
 * Provides additional functionality for course enrollment and academic information
 */
public class Student {
    private Long id;
    private String studentId;
    private String fullName;
    private String block;
    private String academicYear;
    private String semester;
    private List<Enrollment> enrollments;
    
    // Fields from existing StudentInfo for compatibility
    private String lastName;
    private String firstName;
    private String middleName;
    private String dateOfBirth;
    private String password;
    
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
    
    /**
     * Constructor that creates a Student from existing StudentInfo
     * @param studentInfo Existing StudentInfo object
     * @param block Student's block/section
     * @param academicYear Academic year
     * @param semester Current semester
     */
    public Student(StudentInfo studentInfo, String block, String academicYear, String semester) {
        this.studentId = studentInfo.getId();
        this.lastName = studentInfo.getLastName();
        this.firstName = studentInfo.getFirstName();
        this.middleName = studentInfo.getMiddleName();
        this.dateOfBirth = studentInfo.getDateOfBirth();
        this.password = studentInfo.getPassword();
        this.fullName = studentInfo.getFullName();
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
    
    public String getFullName() { 
        if (fullName != null) {
            return fullName;
        } else if (firstName != null && lastName != null) {
            return firstName.toUpperCase() + " " + lastName.toUpperCase();
        }
        return null;
    }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getBlock() { return block; }
    public void setBlock(String block) { this.block = block; }
    
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
    
    // Compatibility getters for existing StudentInfo fields
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
    
    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    /**
     * Adds an enrollment to this student
     * @param enrollment The enrollment to add
     */
    public void addEnrollment(Enrollment enrollment) {
        if (enrollments == null) {
            enrollments = new ArrayList<>();
        }
        enrollments.add(enrollment);
    }
    
    /**
     * Gets all courses this student is enrolled in
     * @return List of Course objects
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
     * Gets total units this student is enrolled in
     * @return Total units
     */
    public int getTotalUnits() {
        int totalUnits = 0;
        List<Course> courses = getCourses();
        for (Course course : courses) {
            if (course.getUnits() != null) {
                totalUnits += course.getUnits();
            }
        }
        return totalUnits;
    }
    
    /**
     * Gets courses scheduled for a specific day
     * @param day Day abbreviation (M, T, W, TH, F)
     * @return List of courses scheduled for the day
     */
    public List<Course> getCoursesForDay(String day) {
        List<Course> daysCourses = new ArrayList<>();
        List<Course> allCourses = getCourses();
        
        for (Course course : allCourses) {
            if (course.isScheduledOnDay(day)) {
                daysCourses.add(course);
            }
        }
        
        return daysCourses;
    }
    
    /**
     * Creates a StudentInfo object from this Student for compatibility
     * @return StudentInfo object
     */
    public StudentInfo toStudentInfo() {
        return new StudentInfo(studentId, lastName, firstName, middleName, dateOfBirth, password);
    }
    
    /**
     * Converts student data to database format for persistence
     * Format: id|studentId|lastName|firstName|middleName|fullName|block|academicYear|semester|dateOfBirth|password
     * @return Database format string
     */
    public String toDatabaseFormat() {
        return String.join("|",
            id != null ? id.toString() : "",
            studentId != null ? studentId : "",
            lastName != null ? lastName : "",
            firstName != null ? firstName : "",
            middleName != null ? middleName : "",
            fullName != null ? fullName : "",
            block != null ? block : "",
            academicYear != null ? academicYear : "",
            semester != null ? semester : "",
            dateOfBirth != null ? dateOfBirth : "",
            password != null ? password : ""
        );
    }
    
    /**
     * Creates a Student object from database format string
     * @param databaseString Database format string
     * @return Student object or null if parsing fails
     */
    public static Student fromDatabaseFormat(String databaseString) {
        try {
            String[] parts = databaseString.split("\\|", -1); // -1 to include empty trailing strings
            if (parts.length < 11) {
                return null;
            }
            
            Student student = new Student();
            student.setId(parts[0].isEmpty() ? null : Long.parseLong(parts[0]));
            student.setStudentId(parts[1].isEmpty() ? null : parts[1]);
            student.setLastName(parts[2].isEmpty() ? null : parts[2]);
            student.setFirstName(parts[3].isEmpty() ? null : parts[3]);
            student.setMiddleName(parts[4].isEmpty() ? null : parts[4]);
            student.setFullName(parts[5].isEmpty() ? null : parts[5]);
            student.setBlock(parts[6].isEmpty() ? null : parts[6]);
            student.setAcademicYear(parts[7].isEmpty() ? null : parts[7]);
            student.setSemester(parts[8].isEmpty() ? null : parts[8]);
            student.setDateOfBirth(parts[9].isEmpty() ? null : parts[9]);
            student.setPassword(parts[10].isEmpty() ? null : parts[10]);
            
            return student;
        } catch (Exception e) {
            System.err.println("Error parsing student from database format: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Gets a formatted display string for the current semester and academic year
     * @return Formatted semester display (e.g., "FIRST SEMESTER, 2025-2026")
     */
    public String getSemesterDisplay() {
        if (semester != null && academicYear != null) {
            return semester.toUpperCase() + ", " + academicYear;
        } else if (semester != null) {
            return semester.toUpperCase();
        } else if (academicYear != null) {
            return academicYear;
        }
        return "";
    }
    
    /**
     * Gets student status string for display
     * @return Status string
     */
    public String getStatusDisplay() {
        StringBuilder status = new StringBuilder("CURRENTLY ENROLLED THIS ");
        if (semester != null) {
            status.append(semester.toUpperCase());
        }
        if (academicYear != null) {
            status.append(", ").append(academicYear);
        }
        if (block != null) {
            status.append(" IN ").append(block.toUpperCase());
        }
        status.append(".");
        return status.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Student{id=%d, studentId='%s', fullName='%s', block='%s', academicYear='%s', semester='%s', enrollments=%d}",
            id, studentId, getFullName(), block, academicYear, semester, 
            enrollments != null ? enrollments.size() : 0);
    }
}