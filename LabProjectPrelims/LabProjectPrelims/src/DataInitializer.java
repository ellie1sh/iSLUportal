import java.time.LocalTime;
import java.util.List;

/**
 * Data initializer class that populates sample course and enrollment data
 * Based on the original Spring Boot DataInitializer but adapted for the Java Swing application
 */
public class DataInitializer {
    
    /**
     * Initializes sample data including courses and student enrollments
     * This method should be called once when the application starts
     */
    public static void initializeSampleData() {
        System.out.println("Initializing sample course and enrollment data...");
        
        // Clear existing data (for testing purposes)
        // CourseDataManager.clearAllData(); // Uncomment if you want to reset data
        
        // Check if data already exists
        List<Course> existingCourses = CourseDataManager.getAllCourses();
        if (!existingCourses.isEmpty()) {
            System.out.println("Sample data already exists. Skipping initialization.");
            return;
        }
        
        // Initialize sample courses based on SHERLIE O. RIVERA's schedule
        initializeSampleCourses();
        
        // Initialize sample student and enrollments
        initializeSampleEnrollments();
        
        System.out.println("Sample data initialization completed successfully!");
    }
    
    /**
     * Initializes sample courses
     */
    private static void initializeSampleCourses() {
        // Create courses based on the original HTML data structure
        Course[] courses = {
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
        
        // Save all courses
        for (Course course : courses) {
            boolean saved = CourseDataManager.saveCourse(course);
            if (saved) {
                System.out.println("Saved course: " + course.getClassCode() + " - " + course.getCourseDescription());
            } else {
                System.err.println("Failed to save course: " + course.getClassCode());
            }
        }
        
        // Add some additional sample courses for variety
        Course[] additionalCourses = {
            new Course("8001", "MATH 101", "COLLEGE ALGEBRA", 3,
                      LocalTime.of(7, 30), LocalTime.of(8, 30), "MWF", "D401"),
            new Course("8002", "ENG 101", "ENGLISH COMPOSITION", 3,
                      LocalTime.of(8, 30), LocalTime.of(9, 30), "MWF", "D402"),
            new Course("8003", "PHYS 101", "GENERAL PHYSICS", 3,
                      LocalTime.of(14, 0), LocalTime.of(15, 0), "MW", "D501"),
            new Course("8004", "CHEM 101", "GENERAL CHEMISTRY", 3,
                      LocalTime.of(15, 0), LocalTime.of(16, 0), "MW", "D502")
        };
        
        for (Course course : additionalCourses) {
            CourseDataManager.saveCourse(course);
        }
    }
    
    /**
     * Initializes sample student and enrollments
     */
    private static void initializeSampleEnrollments() {
        // Create sample student based on existing data
        String sampleStudentId = "STUDENT123"; // This should match existing student in Database.txt
        
        // Check if this student exists in the main database
        StudentInfo existingStudentInfo = DataManager.getStudentInfo(sampleStudentId);
        if (existingStudentInfo == null) {
            // Create a sample student if not exists
            StudentInfo sampleStudentInfo = new StudentInfo(
                sampleStudentId, 
                "RIVERA", 
                "SHERLIE", 
                "O", 
                "1990-01-01", 
                "password123"
            );
            DataManager.saveStudentAccount(sampleStudentInfo);
            System.out.println("Created sample student: " + sampleStudentInfo.getFullName());
        }
        
        // Create extended student record
        Student student = new Student();
        student.setStudentId(sampleStudentId);
        student.setLastName("RIVERA");
        student.setFirstName("SHERLIE");
        student.setMiddleName("O");
        student.setFullName("SHERLIE O. RIVERA");
        student.setBlock("BSIT 2-3");
        student.setAcademicYear("2025-2026");
        student.setSemester("FIRST SEMESTER");
        student.setDateOfBirth("1990-01-01");
        student.setPassword("password123");
        
        // Save extended student record
        boolean studentSaved = CourseDataManager.saveStudentExtended(student);
        if (studentSaved) {
            System.out.println("Saved extended student record for: " + student.getFullName());
        }
        
        // Get all courses and enroll the student in the first 10 courses (matching the original data)
        List<Course> allCourses = CourseDataManager.getAllCourses();
        int coursesToEnroll = Math.min(10, allCourses.size());
        
        for (int i = 0; i < coursesToEnroll; i++) {
            Course course = allCourses.get(i);
            boolean enrolled = CourseDataManager.enrollStudentInCourse(sampleStudentId, course.getId());
            if (enrolled) {
                System.out.println("Enrolled " + sampleStudentId + " in course: " + course.getClassCode());
            } else {
                System.err.println("Failed to enroll " + sampleStudentId + " in course: " + course.getClassCode());
            }
        }
        
        // Create additional sample students for testing
        createAdditionalSampleStudents();
    }
    
    /**
     * Creates additional sample students for testing purposes
     */
    private static void createAdditionalSampleStudents() {
        String[] sampleStudents = {
            "STUDENT124,DOE,JOHN,A,1995-05-15,pass124",
            "STUDENT125,SMITH,JANE,B,1994-03-20,pass125",
            "STUDENT126,JOHNSON,MIKE,C,1993-07-10,pass126"
        };
        
        for (String studentData : sampleStudents) {
            String[] parts = studentData.split(",");
            if (parts.length >= 6) {
                // Check if student already exists
                if (DataManager.getStudentInfo(parts[0]) == null) {
                    StudentInfo studentInfo = new StudentInfo(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    DataManager.saveStudentAccount(studentInfo);
                    
                    // Create extended record
                    Student student = new Student(studentInfo, "BSIT 2-1", "2025-2026", "FIRST SEMESTER");
                    CourseDataManager.saveStudentExtended(student);
                    
                    // Enroll in a few random courses
                    List<Course> allCourses = CourseDataManager.getAllCourses();
                    int numCoursesToEnroll = 3 + (int)(Math.random() * 5); // 3-7 courses
                    
                    for (int i = 0; i < numCoursesToEnroll && i < allCourses.size(); i++) {
                        Course course = allCourses.get(i);
                        CourseDataManager.enrollStudentInCourse(parts[0], course.getId());
                    }
                    
                    System.out.println("Created additional sample student: " + parts[2] + " " + parts[1]);
                }
            }
        }
    }
    
    /**
     * Prints a summary of the initialized data
     */
    public static void printDataSummary() {
        System.out.println("\n=== Data Summary ===");
        
        List<Course> courses = CourseDataManager.getAllCourses();
        System.out.println("Total Courses: " + courses.size());
        
        List<Student> students = CourseDataManager.getAllStudentsExtended();
        System.out.println("Total Extended Students: " + students.size());
        
        System.out.println("\nSample Courses:");
        for (int i = 0; i < Math.min(5, courses.size()); i++) {
            Course course = courses.get(i);
            System.out.println("  " + course.getClassCode() + " - " + course.getCourseDescription());
        }
        
        System.out.println("\nSample Students:");
        for (int i = 0; i < Math.min(3, students.size()); i++) {
            Student student = students.get(i);
            int totalUnits = CourseDataManager.getTotalUnitsByStudentId(student.getStudentId());
            System.out.println("  " + student.getFullName() + " (" + student.getStudentId() + ") - " + totalUnits + " units");
        }
        
        System.out.println("===================\n");
    }
    
    /**
     * Verifies that the sample data was created correctly
     * @return true if verification passes, false otherwise
     */
    public static boolean verifySampleData() {
        try {
            // Check courses
            List<Course> courses = CourseDataManager.getAllCourses();
            if (courses.isEmpty()) {
                System.err.println("Verification failed: No courses found");
                return false;
            }
            
            // Check students
            List<Student> students = CourseDataManager.getAllStudentsExtended();
            if (students.isEmpty()) {
                System.err.println("Verification failed: No extended students found");
                return false;
            }
            
            // Check sample student enrollment
            String sampleStudentId = "STUDENT123";
            List<Course> studentCourses = CourseDataManager.getCoursesByStudentId(sampleStudentId);
            if (studentCourses.isEmpty()) {
                System.err.println("Verification failed: Sample student has no enrolled courses");
                return false;
            }
            
            System.out.println("Data verification passed successfully!");
            return true;
            
        } catch (Exception e) {
            System.err.println("Verification failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Main method for testing the data initializer
     */
    public static void main(String[] args) {
        System.out.println("Running DataInitializer test...");
        
        // Initialize sample data
        initializeSampleData();
        
        // Print summary
        printDataSummary();
        
        // Verify data
        boolean verified = verifySampleData();
        
        if (verified) {
            System.out.println("DataInitializer test completed successfully!");
        } else {
            System.err.println("DataInitializer test failed!");
        }
    }
}