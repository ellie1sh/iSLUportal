package entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Student service class adapted for ISLU Student Portal
 * Provides business logic for student and course management
 */
public class StudentService {
    private static StudentService instance;
    private List<Student> students;
    private List<Course> courses;
    private List<Enrollment> enrollments;
    
    private StudentService() {
        this.students = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.enrollments = new ArrayList<>();
        initializeSampleData();
    }
    
    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }
    
    /**
     * Initialize sample data matching the provided course information
     */
    private void initializeSampleData() {
        // Initialize sample student
        Student student = new Student("STUDENT123", "SHERLIE O. RIVERA", "BSIT 2-3", "2025-2026", "FIRST SEMESTER");
        student.setId(1L);
        students.add(student);
        
        // Initialize sample courses (based on the HTML data)
        Course[] sampleCourses = {
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
        
        // Add courses and create enrollments
        Long courseId = 1L;
        for (Course course : sampleCourses) {
            course.setId(courseId++);
            courses.add(course);
            
            // Create enrollment
            Enrollment enrollment = new Enrollment(student, course);
            enrollment.setId(courseId);
            enrollments.add(enrollment);
            student.addEnrollment(enrollment);
        }
    }
    
    public Optional<Student> getStudentById(String studentId) {
        return students.stream()
                      .filter(s -> s.getStudentId().equals(studentId))
                      .findFirst();
    }
    
    public Optional<Student> getStudentWithCourses(String studentId) {
        return getStudentById(studentId);
    }
    
    public List<Course> getStudentCourses(String studentId) {
        Optional<Student> studentOpt = getStudentById(studentId);
        if (studentOpt.isPresent()) {
            return studentOpt.get().getCourses();
        }
        return new ArrayList<>();
    }
    
    public int getTotalUnits(String studentId) {
        List<Course> courses = getStudentCourses(studentId);
        return courses.stream()
                     .mapToInt(Course::getUnits)
                     .sum();
    }
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments);
    }
    
    /**
     * Get course schedule data formatted for table display
     * @param studentId Student ID to get courses for
     * @return 2D array for JTable
     */
    public Object[][] getCourseTableData(String studentId) {
        List<Course> studentCourses = getStudentCourses(studentId);
        Object[][] data = new Object[studentCourses.size()][7];
        
        for (int i = 0; i < studentCourses.size(); i++) {
            data[i] = studentCourses.get(i).toTableRow();
        }
        
        return data;
    }
    
    /**
     * Get enrollment data formatted for table display
     * @param studentId Student ID to get enrollments for
     * @return 2D array for JTable
     */
    public Object[][] getEnrollmentTableData(String studentId) {
        Optional<Student> studentOpt = getStudentById(studentId);
        if (studentOpt.isPresent()) {
            List<Enrollment> studentEnrollments = studentOpt.get().getEnrollments();
            Object[][] data = new Object[studentEnrollments.size()][6];
            
            for (int i = 0; i < studentEnrollments.size(); i++) {
                data[i] = studentEnrollments.get(i).toTableRow();
            }
            
            return data;
        }
        return new Object[0][6];
    }
}