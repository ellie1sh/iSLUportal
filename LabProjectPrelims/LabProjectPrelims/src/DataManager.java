import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.URISyntaxException;

/**
 * Centralized data management class for the Student Portal system
 * Handles all file operations and data persistence
 */
public class DataManager {
    
    // File paths
    private static final String DATABASE_FILE = "Database.txt";
    private static final String USER_PASSWORD_FILE = "UserPasswordID.txt";
    private static final String PAYMENT_LOGS_FILE = "paymentLogs.txt";
    
    // In-memory fallback schedule per student (since we don't use a DB here)
    private static final Map<String, java.util.List<Course>> studentIdToCourses = new HashMap<>();
    
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
            URL codeSourceUrl = DataManager.class.getProtectionDomain().getCodeSource().getLocation();
            File location = new File(codeSourceUrl.toURI());
            File dir = location.isFile() ? location.getParentFile() : location;

            for (int i = 0; i < 8 && dir != null; i++) {
                File candidate = new File(dir, filename);
                if (candidate.exists()) {
                    return candidate.getAbsoluteFile();
                }
                dir = dir.getParentFile();
            }
        } catch (URISyntaxException ignored) {
        }

        // 3) Fallback to working directory path (even if it does not exist yet)
        return direct.getAbsoluteFile();
    }

    private static File getDatabaseFile() { return resolveFile(DATABASE_FILE); }
    private static File getUserPasswordFile() { return resolveFile(USER_PASSWORD_FILE); }
    private static File getPaymentLogsFile() { return resolveFile(PAYMENT_LOGS_FILE); }

    public static boolean databaseExists() {
        return getDatabaseFile().exists();
    }
    
    /**
     * Returns a list of courses for the given student. If none exist yet, seeds
     * the sample dataset aligned with the user's provided schedule.
     */
    public static List<Course> getStudentCourses(String studentID) {
        if (!studentIdToCourses.containsKey(studentID)) {
            seedSampleCourses(studentID);
        }
        return new ArrayList<>(studentIdToCourses.getOrDefault(studentID, Collections.emptyList()));
    }
    
    /**
     * Calculates total units for the student's current courses.
     */
    public static int getTotalUnits(String studentID) {
        List<Course> courses = getStudentCourses(studentID);
        int total = 0;
        for (Course c : courses) {
            total += c.getUnits();
        }
        return total;
    }
    
    private static void seedSampleCourses(String studentID) {
        java.time.LocalTime t1330 = java.time.LocalTime.of(13, 30);
        java.time.LocalTime t1430 = java.time.LocalTime.of(14, 30);
        java.time.LocalTime t0930 = java.time.LocalTime.of(9, 30);
        java.time.LocalTime t1030 = java.time.LocalTime.of(10, 30);
        java.time.LocalTime t1030a = java.time.LocalTime.of(10, 30);
        java.time.LocalTime t1130 = java.time.LocalTime.of(11, 30);
        java.time.LocalTime t1430b = java.time.LocalTime.of(14, 30);
        java.time.LocalTime t1530 = java.time.LocalTime.of(15, 30);
        java.time.LocalTime t1600 = java.time.LocalTime.of(16, 0);
        java.time.LocalTime t1730 = java.time.LocalTime.of(17, 30);
        java.time.LocalTime t0830 = java.time.LocalTime.of(8, 30);
        java.time.LocalTime t0900 = java.time.LocalTime.of(9, 0);
        java.time.LocalTime t1100 = java.time.LocalTime.of(11, 0);
        java.time.LocalTime t1230 = java.time.LocalTime.of(12, 30);
        java.time.LocalTime t1300 = java.time.LocalTime.of(13, 0);
        java.time.LocalTime t1530_2h = java.time.LocalTime.of(17, 30);
        
        java.util.List<Course> courses = new ArrayList<>();
        courses.add(new Course("7024", "NSTP-CWTS 1", "FOUNDATIONS OF SERVICE", 3, t1330, t1430, "MWF", "D906"));
        courses.add(new Course("9454", "GSTS", "SCIENCE, TECHNOLOGY, AND SOCIETY", 3, t0930, t1030, "TTHS", "D504"));
        courses.add(new Course("9455", "GENVI", "ENVIRONMENTAL SCIENCE", 3, t0930, t1030, "MWF", "D503"));
        courses.add(new Course("9456", "CFE 103", "CATHOLIC FOUNDATION OF MISSION", 3, t1330, t1430, "TTHS", "D503"));
        courses.add(new Course("9457", "IT 211", "REQUIREMENTS ANALYSIS AND MODELING", 3, t1030a, t1100, "MWF", "D511"));
        courses.add(new Course("9458A", "IT 212", "DATA STRUCTURES (LEC)", 2, t1430b, t1530, "TF", "D513"));
        courses.add(new Course("9458B", "IT 212L", "DATA STRUCTURES (LAB)", 1, t1600, t1730, "TF", "D522"));
        courses.add(new Course("9459A", "IT 213", "NETWORK FUNDAMENTALS (LEC)", 2, t0830, t0900, "TF", "D513"));
        courses.add(new Course("9459B", "IT 213L", "NETWORK FUNDAMENTALS (LAB)", 1, t1130, t1300, "TF", "D528"));
        courses.add(new Course("9547", "FIT OA", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (OUTDOOR AND ADVENTURE ACTIVITIES)", 2, t1530, t1730, "TH", "D221"));
        studentIdToCourses.put(studentID, courses);
    }
    
    /**
     * Authenticates user credentials against the database
     * @param studentID The student ID to authenticate
     * @param password The password to authenticate
     * @return true if credentials are valid, false otherwise
     */
    public static boolean authenticateUser(String studentID, String password) {
        try {
            File databaseFile = getDatabaseFile();
            if (!databaseFile.exists()) {
                return false;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        String storedID = parts[0].trim();
                        String storedPassword = parts[5].trim();
                        
                        if (studentID.equals(storedID) && password.equals(storedPassword)) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading database: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Retrieves student information from the database
     * @param studentID The student ID to look up
     * @return StudentInfo object containing student details, or null if not found
     */
    public static StudentInfo getStudentInfo(String studentID) {
        try {
            File databaseFile = getDatabaseFile();
            if (!databaseFile.exists()) {
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        String storedID = parts[0].trim();
                        
                        if (studentID.equals(storedID)) {
                            return new StudentInfo(
                                parts[0].trim(), // ID
                                parts[1].trim(), // Last Name
                                parts[2].trim(), // First Name
                                parts[3].trim(), // Middle Name
                                parts[4].trim(), // Date of Birth
                                parts[5].trim()  // Password
                            );
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading database: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Saves a new student account to the database
     * @param studentInfo The student information to save
     * @return true if successful, false otherwise
     */
    public static boolean saveStudentAccount(StudentInfo studentInfo) {
        try {
            // Save to Database.txt
            File dbFile = getDatabaseFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile, true))) {
                writer.write(studentInfo.toDatabaseFormat());
                writer.newLine();
            }
            
            // Save to UserPasswordID.txt
            File credsFile = getUserPasswordFile();
            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(credsFile, true))) {
                logWriter.write("ID: " + studentInfo.getId() + " | Password: " + studentInfo.getPassword());
                logWriter.newLine();
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving student account: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generates a unique student ID
     * @return A unique 7-digit ID starting with "225"
     */
    public static String generateUniqueStudentID() {
        Set<String> usedIDs = new HashSet<>();
        
        try {
            File file = getDatabaseFile();
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length > 0) {
                            usedIDs.add(parts[0]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading existing IDs: " + e.getMessage());
        }
        
        Random rand = new Random();
        String newID;
        do {
            int lastFour = rand.nextInt(10000);
            newID = "225" + String.format("%04d", lastFour);
        } while (usedIDs.contains(newID));
        
        return newID;
    }
    
    /**
     * Logs a payment transaction
     * @param channelName The payment channel used
     * @param amount The amount paid
     * @param studentID The student ID making the payment
     */
    public static void logPaymentTransaction(String channelName, double amount, String studentID) {
        try {
            File logFile = getPaymentLogsFile();
            
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
            String currentDateTime = dateFormat.format(new java.util.Date());
            
            String reference = "FIRST SEMESTER 2025-2026 Enrollme.";
            String formattedAmount = String.format("P %,.2f", amount);
            
            String logEntry = currentDateTime + "," + channelName + "," + reference + "," + formattedAmount + "," + studentID;
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(logEntry);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to payment log: " + e.getMessage());
        }
    }
    
    /**
     * Loads payment transactions for a specific student
     * @param studentID The student ID to load transactions for
     * @return List of payment transactions
     */
    public static List<PaymentTransaction> loadPaymentTransactions(String studentID) {
        List<PaymentTransaction> transactions = new ArrayList<>();
        
        try {
            File logFile = getPaymentLogsFile();
            if (logFile.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length >= 5) {
                            String transactionStudentID = parts[4].trim();
                            if (studentID.equals(transactionStudentID)) {
                                transactions.add(new PaymentTransaction(
                                    parts[0].trim(), // Date
                                    parts[1].trim(), // Channel
                                    parts[2].trim(), // Reference
                                    parts[3].trim()  // Amount
                                ));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading payment logs: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Gets all students from the database
     * @return List of all student information
     */
    public static List<StudentInfo> getAllStudents() {
        List<StudentInfo> students = new ArrayList<>();
        
        try {
            File databaseFile = getDatabaseFile();
            if (!databaseFile.exists()) {
                return students;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        students.add(new StudentInfo(
                            parts[0].trim(), // ID
                            parts[1].trim(), // Last Name
                            parts[2].trim(), // First Name
                            parts[3].trim(), // Middle Name
                            parts[4].trim(), // Date of Birth
                            parts[5].trim()  // Password
                        ));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading all students: " + e.getMessage());
        }
        
        return students;
    }
}

