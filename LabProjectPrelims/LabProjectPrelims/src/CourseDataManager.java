import java.io.*;
import java.util.*;
import java.time.LocalTime;

/**
 * Extended data management class for courses and enrollments
 * Works alongside the existing DataManager to provide course-related functionality
 */
public class CourseDataManager {
    
    // File paths
    private static final String COURSES_FILE = "courses.txt";
    private static final String ENROLLMENTS_FILE = "enrollments.txt";
    private static final String STUDENTS_EXTENDED_FILE = "studentsExtended.txt";
    
    // ID counters for auto-generated IDs
    private static long nextCourseId = 1;
    private static long nextEnrollmentId = 1;
    private static long nextStudentId = 1;
    
    /**
     * Resolve a data file by searching from the working directory and then walking up
     * from the compiled classes location. This makes file access robust regardless
     * of where the application is launched from.
     */
    private static File resolveFile(String filename) {
        // 1) Try working directory
        File direct = new File(filename);
        if (direct.exists()) {
            return direct.getAbsoluteFile();
        }

        // 2) Try walking up from the code source (e.g., out/production/...)
        try {
            java.net.URL codeSourceUrl = CourseDataManager.class.getProtectionDomain().getCodeSource().getLocation();
            File location = new File(codeSourceUrl.toURI());
            File dir = location.isFile() ? location.getParentFile() : location;

            for (int i = 0; i < 8 && dir != null; i++) {
                File candidate = new File(dir, filename);
                if (candidate.exists()) {
                    return candidate.getAbsoluteFile();
                }
                dir = dir.getParentFile();
            }
        } catch (java.net.URISyntaxException ignored) {
        }

        // 3) Fallback to working directory path (even if it does not exist yet)
        return direct.getAbsoluteFile();
    }

    private static File getCoursesFile() { return resolveFile(COURSES_FILE); }
    private static File getEnrollmentsFile() { return resolveFile(ENROLLMENTS_FILE); }
    private static File getStudentsExtendedFile() { return resolveFile(STUDENTS_EXTENDED_FILE); }
    
    /**
     * Initialize ID counters by reading existing data
     */
    private static void initializeIdCounters() {
        // Initialize course ID counter
        List<Course> courses = getAllCourses();
        for (Course course : courses) {
            if (course.getId() != null && course.getId() >= nextCourseId) {
                nextCourseId = course.getId() + 1;
            }
        }
        
        // Initialize enrollment ID counter
        List<Enrollment> enrollments = getAllEnrollments();
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getId() != null && enrollment.getId() >= nextEnrollmentId) {
                nextEnrollmentId = enrollment.getId() + 1;
            }
        }
        
        // Initialize student ID counter
        List<Student> students = getAllStudentsExtended();
        for (Student student : students) {
            if (student.getId() != null && student.getId() >= nextStudentId) {
                nextStudentId = student.getId() + 1;
            }
        }
    }
    
    /**
     * Saves a course to the database
     * @param course The course to save
     * @return true if successful, false otherwise
     */
    public static boolean saveCourse(Course course) {
        try {
            if (course.getId() == null) {
                initializeIdCounters();
                course.setId(nextCourseId++);
            }
            
            File coursesFile = getCoursesFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(coursesFile, true))) {
                writer.write(course.toDatabaseFormat());
                writer.newLine();
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving course: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all courses from the database
     * @return List of all courses
     */
    public static List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        
        try {
            File coursesFile = getCoursesFile();
            if (!coursesFile.exists()) {
                return courses;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(coursesFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Course course = Course.fromDatabaseFormat(line);
                    if (course != null) {
                        courses.add(course);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading courses: " + e.getMessage());
        }
        
        return courses;
    }
    
    /**
     * Finds a course by its ID
     * @param courseId The course ID to search for
     * @return Course object or null if not found
     */
    public static Course getCourseById(Long courseId) {
        List<Course> courses = getAllCourses();
        for (Course course : courses) {
            if (courseId.equals(course.getId())) {
                return course;
            }
        }
        return null;
    }
    
    /**
     * Finds a course by its class code
     * @param classCode The class code to search for
     * @return Course object or null if not found
     */
    public static Course getCourseByClassCode(String classCode) {
        List<Course> courses = getAllCourses();
        for (Course course : courses) {
            if (classCode.equals(course.getClassCode())) {
                return course;
            }
        }
        return null;
    }
    
    /**
     * Saves an enrollment to the database
     * @param enrollment The enrollment to save
     * @return true if successful, false otherwise
     */
    public static boolean saveEnrollment(Enrollment enrollment) {
        try {
            if (enrollment.getId() == null) {
                initializeIdCounters();
                enrollment.setId(nextEnrollmentId++);
            }
            
            File enrollmentsFile = getEnrollmentsFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(enrollmentsFile, true))) {
                writer.write(enrollment.toDatabaseFormat());
                writer.newLine();
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving enrollment: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all enrollments from the database
     * @return List of all enrollments (with partial data - IDs only)
     */
    public static List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        
        try {
            File enrollmentsFile = getEnrollmentsFile();
            if (!enrollmentsFile.exists()) {
                return enrollments;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(enrollmentsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Enrollment enrollment = Enrollment.fromDatabaseFormat(line);
                    if (enrollment != null) {
                        enrollments.add(enrollment);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading enrollments: " + e.getMessage());
        }
        
        return enrollments;
    }
    
    /**
     * Gets courses for a specific student ID
     * @param studentId The student ID to get courses for
     * @return List of courses the student is enrolled in
     */
    public static List<Course> getCoursesByStudentId(String studentId) {
        List<Course> studentCourses = new ArrayList<>();
        
        try {
            File enrollmentsFile = getEnrollmentsFile();
            if (!enrollmentsFile.exists()) {
                return studentCourses;
            }

            // Get enrolled course IDs for the student
            Set<Long> enrolledCourseIds = new HashSet<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(enrollmentsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|", -1);
                    if (parts.length >= 5) {
                        String enrollmentStudentId = parts[1].trim();
                        String status = parts[4].trim();
                        
                        if (studentId.equals(enrollmentStudentId) && "ENROLLED".equals(status)) {
                            try {
                                Long courseId = Long.parseLong(parts[2].trim());
                                enrolledCourseIds.add(courseId);
                            } catch (NumberFormatException e) {
                                // Skip invalid course ID
                            }
                        }
                    }
                }
            }
            
            // Get courses by IDs
            List<Course> allCourses = getAllCourses();
            for (Course course : allCourses) {
                if (enrolledCourseIds.contains(course.getId())) {
                    studentCourses.add(course);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading student courses: " + e.getMessage());
        }
        
        return studentCourses;
    }
    
    /**
     * Gets total units for a specific student
     * @param studentId The student ID
     * @return Total units enrolled
     */
    public static int getTotalUnitsByStudentId(String studentId) {
        List<Course> courses = getCoursesByStudentId(studentId);
        int totalUnits = 0;
        
        for (Course course : courses) {
            if (course.getUnits() != null) {
                totalUnits += course.getUnits();
            }
        }
        
        return totalUnits;
    }
    
    /**
     * Saves an extended student record
     * @param student The student to save
     * @return true if successful, false otherwise
     */
    public static boolean saveStudentExtended(Student student) {
        try {
            if (student.getId() == null) {
                initializeIdCounters();
                student.setId(nextStudentId++);
            }
            
            File studentsFile = getStudentsExtendedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(studentsFile, true))) {
                writer.write(student.toDatabaseFormat());
                writer.newLine();
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving extended student: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gets all extended student records
     * @return List of all extended student records
     */
    public static List<Student> getAllStudentsExtended() {
        List<Student> students = new ArrayList<>();
        
        try {
            File studentsFile = getStudentsExtendedFile();
            if (!studentsFile.exists()) {
                return students;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(studentsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Student student = Student.fromDatabaseFormat(line);
                    if (student != null) {
                        students.add(student);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading extended students: " + e.getMessage());
        }
        
        return students;
    }
    
    /**
     * Gets an extended student record by student ID
     * @param studentId The student ID to search for
     * @return Student object or null if not found
     */
    public static Student getStudentExtendedById(String studentId) {
        List<Student> students = getAllStudentsExtended();
        for (Student student : students) {
            if (studentId.equals(student.getStudentId())) {
                return student;
            }
        }
        return null;
    }
    
    /**
     * Creates or updates a student with enrollment data
     * @param studentId The student ID
     * @return Student object with loaded enrollment data
     */
    public static Student getStudentWithCourses(String studentId) {
        // Try to get extended student record first
        Student student = getStudentExtendedById(studentId);
        
        // If not found, create from existing StudentInfo
        if (student == null) {
            StudentInfo studentInfo = DataManager.getStudentInfo(studentId);
            if (studentInfo != null) {
                student = new Student(studentInfo, "BSIT 2-3", "2025-2026", "FIRST SEMESTER");
            } else {
                return null;
            }
        }
        
        // Load courses and create enrollments
        List<Course> courses = getCoursesByStudentId(studentId);
        student.setEnrollments(new ArrayList<>());
        
        for (Course course : courses) {
            Enrollment enrollment = new Enrollment(student, course);
            student.addEnrollment(enrollment);
        }
        
        return student;
    }
    
    /**
     * Enrolls a student in a course
     * @param studentId The student ID
     * @param courseId The course ID
     * @return true if successful, false otherwise
     */
    public static boolean enrollStudentInCourse(String studentId, Long courseId) {
        // Check if already enrolled
        List<Course> existingCourses = getCoursesByStudentId(studentId);
        for (Course course : existingCourses) {
            if (courseId.equals(course.getId())) {
                return false; // Already enrolled
            }
        }
        
        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentDate(java.time.LocalDate.now().toString());
        enrollment.setStatus("ENROLLED");
        
        // Save with IDs only (actual objects will be loaded when needed)
        try {
            if (enrollment.getId() == null) {
                initializeIdCounters();
                enrollment.setId(nextEnrollmentId++);
            }
            
            File enrollmentsFile = getEnrollmentsFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(enrollmentsFile, true))) {
                writer.write(enrollment.toDatabaseFormatWithIds(studentId, courseId));
                writer.newLine();
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error enrolling student in course: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Drops a student from a course
     * @param studentId The student ID
     * @param courseId The course ID
     * @return true if successful, false otherwise
     */
    public static boolean dropStudentFromCourse(String studentId, Long courseId) {
        // This would require updating the enrollment status
        // For now, we'll implement a simple version that removes the enrollment
        // In a real system, you'd want to update the status to "DROPPED"
        System.out.println("Drop functionality not fully implemented - would update status to DROPPED");
        return true;
    }
    
    /**
     * Clears all course and enrollment data (for testing purposes)
     */
    public static void clearAllData() {
        try {
            File coursesFile = getCoursesFile();
            if (coursesFile.exists()) {
                coursesFile.delete();
            }
            
            File enrollmentsFile = getEnrollmentsFile();
            if (enrollmentsFile.exists()) {
                enrollmentsFile.delete();
            }
            
            File studentsFile = getStudentsExtendedFile();
            if (studentsFile.exists()) {
                studentsFile.delete();
            }
            
            // Reset ID counters
            nextCourseId = 1;
            nextEnrollmentId = 1;
            nextStudentId = 1;
            
        } catch (Exception e) {
            System.err.println("Error clearing data: " + e.getMessage());
        }
    }
}