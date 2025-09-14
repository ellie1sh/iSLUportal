import java.io.*;
import java.util.*;

/**
 * Centralized data management class for the Student Portal system
 * Handles all file operations and data persistence
 */
public class DataManager {
    
    // File paths
    private static final String DATABASE_FILE = "Database.txt";
    private static final String USER_PASSWORD_FILE = "UserPasswordID.txt";
    private static final String PAYMENT_LOGS_FILE = "paymentLogs.txt";
    private static final String LOGIN_LOGS_FILE = "loginLogs.txt";

    // Resolve data files robustly regardless of current working directory
    private static File resolveDataFile(String fileName) {
        List<File> candidates = new ArrayList<>();

        // 1) Current working directory
        String userDir = System.getProperty("user.dir");
        if (userDir != null) {
            candidates.add(new File(userDir, fileName));
        }

        // 2) Code source directory (e.g., out/production or JAR location) and its parents
        try {
            java.net.URL codeSourceUrl = DataManager.class.getProtectionDomain().getCodeSource().getLocation();
            File codeSource = new File(codeSourceUrl.toURI());
            File dir = codeSource.isFile() ? codeSource.getParentFile() : codeSource;
            for (int i = 0; i < 6 && dir != null; i++) {
                candidates.add(new File(dir, fileName));
                dir = dir.getParentFile();
            }
        } catch (Exception ignored) {}

        // 3) Project-specific known relative directory
        candidates.add(new File("LabProjectPrelims/LabProjectPrelims/" + fileName));
        candidates.add(new File("../LabProjectPrelims/LabProjectPrelims/" + fileName));

        // 4) Parent of current working directory
        if (userDir != null) {
            File parent = new File(userDir).getParentFile();
            if (parent != null) {
                candidates.add(new File(parent, fileName));
            }
        }

        // Return the first existing candidate
        for (File f : candidates) {
            if (f.exists()) {
                return f;
            }
        }

        // If none exist, prefer writing into a stable, project-friendly location if available
        File preferredProjectDir = new File("LabProjectPrelims/LabProjectPrelims");
        if (preferredProjectDir.isDirectory()) {
            return new File(preferredProjectDir, fileName);
        }

        // Fallback to current working directory
        return new File(fileName);
    }

    private static File getDatabaseFile() { return resolveDataFile(DATABASE_FILE); }
    private static File getUserPasswordFile() { return resolveDataFile(USER_PASSWORD_FILE); }
    private static File getPaymentLogsFile() { return resolveDataFile(PAYMENT_LOGS_FILE); }
    private static File getLoginLogsFile() { return resolveDataFile(LOGIN_LOGS_FILE); }

    public static boolean databaseExists() { return getDatabaseFile().exists(); }

    public static void ensureStorageInitialized() {
        try {
            // Ensure database and credentials files exist
            File db = getDatabaseFile();
            if (!db.exists()) {
                File parent = db.getParentFile();
                if (parent != null) parent.mkdirs();
                db.createNewFile();
            }
            File creds = getUserPasswordFile();
            if (!creds.exists()) {
                File parent = creds.getParentFile();
                if (parent != null) parent.mkdirs();
                creds.createNewFile();
            }
            File loginLog = getLoginLogsFile();
            if (!loginLog.exists()) {
                File parent = loginLog.getParentFile();
                if (parent != null) parent.mkdirs();
                loginLog.createNewFile();
            }
        } catch (IOException ignored) {}
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
            ensureStorageInitialized();

            // Save to Database.txt
            File db = getDatabaseFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(db, true))) {
                writer.write(studentInfo.toDatabaseFormat());
                writer.newLine();
            }
            
            // Save to UserPasswordID.txt
            File userPass = getUserPasswordFile();
            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(userPass, true))) {
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
     * Logs a login attempt (success or failure) with timestamp
     * @param studentID The attempted student ID
     * @param success Whether the login was successful
     */
    public static void logLoginAttempt(String studentID, boolean success) {
        try {
            File logFile = getLoginLogsFile();
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy hh:mm a");
            String currentDateTime = dateFormat.format(new java.util.Date());
            String status = success ? "SUCCESS" : "FAILURE";
            String entry = currentDateTime + "," + studentID + "," + status;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(entry);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to login log: " + e.getMessage());
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

