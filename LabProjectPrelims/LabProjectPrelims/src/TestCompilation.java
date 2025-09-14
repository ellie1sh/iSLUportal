/**
 * Simple test to check if all classes can be compiled together
 */
public class TestCompilation {
    public static void main(String[] args) {
        // Test if all classes can be instantiated
        try {
            // Test DataManager
            boolean auth = DataManager.authenticateUser("test", "test");
            System.out.println("DataManager: OK");
            
            // Test MyDoublyLinkedList
            MyDoublyLinkedList<String> list = new MyDoublyLinkedList<>();
            list.add("test");
            System.out.println("MyDoublyLinkedList: OK");
            
            // Test MenuItem
            MenuItem item = new MenuItem("Test");
            System.out.println("MenuItem: OK");
            
            // Test StudentInfo
            StudentInfo student = new StudentInfo("123", "Doe", "John", "M", "01/01/2000", "pass");
            System.out.println("StudentInfo: OK");
            
            // Test PaymentTransaction
            PaymentTransaction transaction = new PaymentTransaction("01/01/2025", "Test", "Ref", "100.00");
            System.out.println("PaymentTransaction: OK");
            
            // Test PortalUtils
            MyDoublyLinkedList<MenuItem> menu = PortalUtils.createIntegratedMenuSystem();
            System.out.println("PortalUtils: OK");
            
            System.out.println("All classes compiled successfully!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
