import java.util.LinkedList;
import java.util.List;

/**
 * Utility class that demonstrates integration of all classes in the Student Portal system
 * This class shows how MyDoublyLinkedList, MenuItem, DataManager, and other classes work together
 */
public class PortalUtils {
    
    /**
     * Creates a comprehensive menu system using MyDoublyLinkedList and MenuItem
     * @return A doubly linked list containing all menu items
     */
    public static MyDoublyLinkedList<MenuItem> createIntegratedMenuSystem() {
        MyDoublyLinkedList<MenuItem> menu = new MyDoublyLinkedList<>();
        
        // Create sub-items for each menu category
        LinkedList<String> homeSubList = createHomeSublist();
        LinkedList<String> attendanceSubList = createAttendanceSubList();
        LinkedList<String> scheduleSubList = createScheduleSubList();
        LinkedList<String> gradesSubList = createGradeSubList();
        LinkedList<String> soaSubList = createSOASubList();
        LinkedList<String> torSubList = createTORSubList();
        LinkedList<String> personalDetailsSubList = createPersonalDetailsSubList();
        
        // Add menu items to the doubly linked list
        menu.add(new MenuItem("🏠 Home", homeSubList));
        menu.add(new MenuItem("📌 Attendance", attendanceSubList));
        menu.add(new MenuItem("📅 Schedule", scheduleSubList));
        menu.add(new MenuItem("🧮 Statement of Accounts", soaSubList));
        menu.add(new MenuItem("📊 Grades", gradesSubList));
        menu.add(new MenuItem("📋 Transcript of Records", torSubList));
        menu.add(new MenuItem("👤 Personal Details", personalDetailsSubList));
        
        return menu;
    }
    
    /**
     * Demonstrates data integration by creating a student management system
     * @return A doubly linked list of student information
     */
    public static MyDoublyLinkedList<StudentInfo> createStudentManagementSystem() {
        MyDoublyLinkedList<StudentInfo> students = new MyDoublyLinkedList<>();
        
        // Get all students from DataManager
        List<StudentInfo> allStudents = DataManager.getAllStudents();
        
        // Add students to the doubly linked list
        for (StudentInfo student : allStudents) {
            students.add(student);
        }
        
        return students;
    }
    
    /**
     * Demonstrates menu navigation using the doubly linked list
     * @param menu The menu system
     * @param currentIndex Current menu index
     * @param direction Direction to navigate (1 for next, -1 for previous)
     * @return The menu item at the new position
     */
    public static MenuItem navigateMenu(MyDoublyLinkedList<MenuItem> menu, int currentIndex, int direction) {
        int newIndex = currentIndex + direction;
        
        if (newIndex < 0) {
            newIndex = menu.getSize() - 1; // Wrap to last item
        } else if (newIndex >= menu.getSize()) {
            newIndex = 0; // Wrap to first item
        }
        
        return menu.get(newIndex);
    }
    
    /**
     * Validates student data using integrated systems
     * @param studentID The student ID to validate
     * @param password The password to validate
     * @return true if valid, false otherwise
     */
    public static boolean validateStudentCredentials(String studentID, String password) {
        // Use DataManager for authentication
        boolean isValid = DataManager.authenticateUser(studentID, password);
        
        if (isValid) {
            // Get student info for additional validation
            StudentInfo studentInfo = DataManager.getStudentInfo(studentID);
            if (studentInfo != null) {
                System.out.println("Welcome, " + studentInfo.getFullName() + "!");
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Creates a comprehensive student portal session
     * @param studentID The student ID
     * @return A portal session object containing all integrated data
     */
    public static PortalSession createPortalSession(String studentID) {
        StudentInfo studentInfo = DataManager.getStudentInfo(studentID);
        if (studentInfo == null) {
            // Fallback to a minimal placeholder to avoid nulls during integration demos/tests
            studentInfo = new StudentInfo(studentID, "N/A", "Unknown", "", "", "");
        }
        MyDoublyLinkedList<MenuItem> menu = createIntegratedMenuSystem();
        List<PaymentTransaction> transactions = DataManager.loadPaymentTransactions(studentID);
        
        return new PortalSession(studentInfo, menu, transactions);
    }
    
    // Helper methods for creating sub-lists
    private static LinkedList<String> createHomeSublist() {
        LinkedList<String> homeSubList = new LinkedList<>();
        homeSubList.add("news");
        homeSubList.add("Status");
        return homeSubList;
    }
    
    private static LinkedList<String> createAttendanceSubList() {
        LinkedList<String> attendanceSubList = new LinkedList<>();
        attendanceSubList.add("Subject");
        attendanceSubList.add("Present");
        attendanceSubList.add("Absent");
        attendanceSubList.add("Late");
        attendanceSubList.add("Percentage");
        return attendanceSubList;
    }
    
    private static LinkedList<String> createScheduleSubList() {
        LinkedList<String> scheduleSubList = new LinkedList<>();
        scheduleSubList.add("Time");
        scheduleSubList.add("Monday");
        scheduleSubList.add("Tuesday");
        scheduleSubList.add("Wednesday");
        scheduleSubList.add("Thursday");
        scheduleSubList.add("Friday");
        scheduleSubList.add("Saturday");
        return scheduleSubList;
    }
    
    private static LinkedList<String> createGradeSubList() {
        LinkedList<String> gradesSublist = new LinkedList<>();
        gradesSublist.add("Subject");
        gradesSublist.add("Prelim Grade");
        gradesSublist.add("Midterm Grade");
        gradesSublist.add("Tentative Final Grade");
        gradesSublist.add("Final Grade");
        return gradesSublist;
    }
    
    private static LinkedList<String> createSOASubList() {
        LinkedList<String> sOASubList = new LinkedList<>();
        sOASubList.add("Amount Due");
        sOASubList.add("Overpayment");
        sOASubList.add("Payment History");
        return sOASubList;
    }
    
    private static LinkedList<String> createTORSubList() {
        LinkedList<String> TORSubList = new LinkedList<>();
        TORSubList.add("Course Number");
        TORSubList.add("Descriptive Title");
        TORSubList.add("Grade");
        TORSubList.add("Units");
        return TORSubList;
    }
    
    private static LinkedList<String> createPersonalDetailsSubList() {
        LinkedList<String> personalDetailsSubList = new LinkedList<>();
        personalDetailsSubList.add("Student Name");
        personalDetailsSubList.add("Student ID");
        personalDetailsSubList.add("Course");
        personalDetailsSubList.add("Year Level");
        personalDetailsSubList.add("Email");
        personalDetailsSubList.add("Contact Number");
        personalDetailsSubList.add("Address");
        return personalDetailsSubList;
    }
}

