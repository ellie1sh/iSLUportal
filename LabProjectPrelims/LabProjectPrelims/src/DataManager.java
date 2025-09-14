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

