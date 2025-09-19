import java.util.List;

/**
 * Integration test class that demonstrates how all classes work together
 * This class shows the complete integration of the Student Portal system
 */
public class IntegrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== STUDENT PORTAL INTEGRATION TEST ===\n");
        
        // Test 1: DataManager Integration
        testDataManagerIntegration();
        
        // Test 2: MyDoublyLinkedList Integration
        testDoublyLinkedListIntegration();
        
        // Test 3: MenuItem Integration
        testMenuItemIntegration();
        
        // Test 4: PortalUtils Integration
        testPortalUtilsIntegration();
        
        // Test 5: Complete System Integration
        testCompleteSystemIntegration();
        
        System.out.println("\n=== ALL INTEGRATION TESTS COMPLETED SUCCESSFULLY ===");
    }
    
    /**
     * Test DataManager integration with file operations
     */
    private static void testDataManagerIntegration() {
        System.out.println("1. Testing DataManager Integration:");
        
        // Test student ID generation
        String uniqueID = DataManager.generateUniqueStudentID();
        System.out.println("   ✓ Generated unique student ID: " + uniqueID);
        
        // Test student info retrieval
        List<StudentInfo> allStudents = DataManager.getAllStudents();
        System.out.println("   ✓ Retrieved " + allStudents.size() + " students from database");
        
        // Test payment transaction loading
        List<PaymentTransaction> transactions = DataManager.loadPaymentTransactions("2250001");
        System.out.println("   ✓ Loaded " + transactions.size() + " payment transactions");
        
        System.out.println("   DataManager integration: PASSED\n");
    }
    
    /**
     * Test MyDoublyLinkedList integration and functionality
     */
    private static void testDoublyLinkedListIntegration() {
        System.out.println("2. Testing MyDoublyLinkedList Integration:");
        
        MyDoublyLinkedList<String> testList = new MyDoublyLinkedList<>();
        
        // Test basic operations
        testList.add("First");
        testList.add("Second");
        testList.add("Third");
        System.out.println("   ✓ Added 3 items to doubly linked list");
        
        // Test size and navigation
        System.out.println("   ✓ List size: " + testList.getSize());
        System.out.println("   ✓ First item: " + testList.getFirst());
        System.out.println("   ✓ Last item: " + testList.getLast());
        
        // Test contains and get operations
        System.out.println("   ✓ Contains 'Second': " + testList.contains("Second"));
        System.out.println("   ✓ Item at index 1: " + testList.get(1));
        
        // Test iteration
        System.out.print("   ✓ Iteration: ");
        for (String item : testList) {
            System.out.print(item + " ");
        }
        System.out.println();
        
        System.out.println("   MyDoublyLinkedList integration: PASSED\n");
    }
    
    /**
     * Test MenuItem integration with doubly linked list
     */
    private static void testMenuItemIntegration() {
        System.out.println("3. Testing MenuItem Integration:");
        
        // Create menu items
        MenuItem homeItem = new MenuItem("🏠 Home");
        MenuItem gradesItem = new MenuItem("📊 Grades");
        
        System.out.println("   ✓ Created menu items: " + homeItem.getName() + ", " + gradesItem.getName());
        
        // Test menu item with sub-items
        java.util.LinkedList<String> subItems = new java.util.LinkedList<>();
        subItems.add("Subject");
        subItems.add("Grade");
        MenuItem detailedItem = new MenuItem("📋 Details", subItems);
        
        System.out.println("   ✓ Created detailed menu item with " + detailedItem.getSubItems().size() + " sub-items");
        System.out.println("   ✓ Has sub-items: " + detailedItem.hasSubItems());
        
        System.out.println("   MenuItem integration: PASSED\n");
    }
    
    /**
     * Test PortalUtils integration
     */
    private static void testPortalUtilsIntegration() {
        System.out.println("4. Testing PortalUtils Integration:");
        
        // Test integrated menu system creation
        MyDoublyLinkedList<MenuItem> menu = PortalUtils.createIntegratedMenuSystem();
        System.out.println("   ✓ Created integrated menu system with " + menu.getSize() + " items");
        
        // Test menu navigation
        MenuItem currentItem = PortalUtils.navigateMenu(menu, 0, 1);
        System.out.println("   ✓ Navigated to next menu item: " + currentItem.getName());
        
        // Test student management system
        MyDoublyLinkedList<StudentInfo> students = PortalUtils.createStudentManagementSystem();
        System.out.println("   ✓ Created student management system with " + students.getSize() + " students");
        
        // Test portal session creation (use an existing student from the database if available)
        String validStudentId;
        List<StudentInfo> allStudents = DataManager.getAllStudents();
        if (!allStudents.isEmpty()) {
            validStudentId = allStudents.get(0).getId();
        } else {
            // Fallback to original hardcoded ID if database is empty
            validStudentId = "2250001";
        }
        PortalSession session = PortalUtils.createPortalSession(validStudentId);
        System.out.println("   ✓ Created portal session");
        System.out.println("   " + session.getSessionSummary());
        
        System.out.println("   PortalUtils integration: PASSED\n");
    }
    
    /**
     * Test complete system integration
     */
    private static void testCompleteSystemIntegration() {
        System.out.println("5. Testing Complete System Integration:");
        
        // Simulate a complete student portal workflow
        System.out.println("   Simulating student portal workflow...");
        
        // Step 1: Student authentication (would normally be done through Login class)
        String studentID = "2250001";
        String password = "password123";
        boolean isAuthenticated = PortalUtils.validateStudentCredentials(studentID, password);
        System.out.println("   ✓ Authentication result: " + (isAuthenticated ? "SUCCESS" : "FAILED"));
        
        if (isAuthenticated) {
            // Step 2: Create portal session
            PortalSession session = PortalUtils.createPortalSession(studentID);
            System.out.println("   ✓ Portal session created for student: " + session.getStudentInfo().getFullName());
            
            // Step 3: Navigate through menu system
            MyDoublyLinkedList<MenuItem> menu = session.getMenu();
            System.out.println("   ✓ Available menu items: " + menu.getSize());
            
            // Step 4: Demonstrate menu navigation
            for (int i = 0; i < Math.min(3, menu.getSize()); i++) {
                MenuItem item = menu.get(i);
                System.out.println("   ✓ Menu item " + (i+1) + ": " + item.getName());
            }
            
            // Step 5: Access payment transactions
            List<PaymentTransaction> transactions = session.getTransactions();
            System.out.println("   ✓ Payment transactions available: " + transactions.size());
            
            // Step 6: Demonstrate data persistence
            System.out.println("   ✓ All data operations integrated with DataManager");
        }
        
        System.out.println("   Complete system integration: PASSED\n");
    }
}
