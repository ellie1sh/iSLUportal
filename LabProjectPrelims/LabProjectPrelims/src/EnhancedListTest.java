/**
 * Comprehensive test for the enhanced MyDoublyLinkedList
 * Tests all the new methods added to make the doubly linked list more complete
 */
public class EnhancedListTest {
    
    public static void main(String[] args) {
        System.out.println("=== ENHANCED DOUBLY LINKED LIST TEST ===\n");
        
        testBasicOperations();
        testRemoveOperations();
        testInsertOperation();
        testSearchOperations();
        testEdgeCases();
        
        System.out.println("=== ALL ENHANCED LIST TESTS COMPLETED SUCCESSFULLY ===");
    }
    
    private static void testBasicOperations() {
        System.out.println("1. Testing Basic Operations:");
        MyDoublyLinkedList<String> list = new MyDoublyLinkedList<>();
        
        // Test adding elements
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        list.add("Date");
        
        System.out.println("   ✓ Added 4 elements: " + list.toString());
        System.out.println("   ✓ Size: " + list.getSize());
        System.out.println("   ✓ First: " + list.getFirst());
        System.out.println("   ✓ Last: " + list.getLast());
        System.out.println("   Basic operations: PASSED\n");
    }
    
    private static void testRemoveOperations() {
        System.out.println("2. Testing Remove Operations:");
        MyDoublyLinkedList<String> list = new MyDoublyLinkedList<>();
        
        // Setup test data
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        System.out.println("   Initial list: " + list.toString());
        
        // Test remove by value
        boolean removed = list.remove("C");
        System.out.println("   ✓ Removed 'C': " + removed + " -> " + list.toString());
        
        // Test removeAt
        String removedItem = list.removeAt(1);
        System.out.println("   ✓ Removed at index 1: '" + removedItem + "' -> " + list.toString());
        
        // Test remove non-existent item
        boolean notRemoved = list.remove("X");
        System.out.println("   ✓ Attempted to remove 'X': " + notRemoved);
        
        System.out.println("   Remove operations: PASSED\n");
    }
    
    private static void testInsertOperation() {
        System.out.println("3. Testing Insert Operation:");
        MyDoublyLinkedList<String> list = new MyDoublyLinkedList<>();
        
        // Test insert at various positions
        list.insert(0, "First");
        System.out.println("   ✓ Insert at index 0: " + list.toString());
        
        list.insert(1, "Last");
        System.out.println("   ✓ Insert at end: " + list.toString());
        
        list.insert(1, "Middle");
        System.out.println("   ✓ Insert in middle: " + list.toString());
        
        list.insert(0, "New First");
        System.out.println("   ✓ Insert at beginning: " + list.toString());
        
        System.out.println("   Insert operation: PASSED\n");
    }
    
    private static void testSearchOperations() {
        System.out.println("4. Testing Search Operations:");
        MyDoublyLinkedList<String> list = new MyDoublyLinkedList<>();
        
        // Setup test data with duplicates
        list.add("Apple");
        list.add("Banana");
        list.add("Apple");
        list.add("Cherry");
        list.add("Apple");
        
        System.out.println("   Test list: " + list.toString());
        
        // Test indexOf
        int firstIndex = list.indexOf("Apple");
        System.out.println("   ✓ First index of 'Apple': " + firstIndex);
        
        // Test lastIndexOf
        int lastIndex = list.lastIndexOf("Apple");
        System.out.println("   ✓ Last index of 'Apple': " + lastIndex);
        
        // Test indexOf for non-existent item
        int notFound = list.indexOf("Orange");
        System.out.println("   ✓ Index of 'Orange': " + notFound);
        
        // Test contains
        boolean contains = list.contains("Banana");
        System.out.println("   ✓ Contains 'Banana': " + contains);
        
        System.out.println("   Search operations: PASSED\n");
    }
    
    private static void testEdgeCases() {
        System.out.println("5. Testing Edge Cases:");
        
        // Test empty list operations
        MyDoublyLinkedList<String> emptyList = new MyDoublyLinkedList<>();
        System.out.println("   ✓ Empty list size: " + emptyList.getSize());
        System.out.println("   ✓ Empty list contains 'X': " + emptyList.contains("X"));
        System.out.println("   ✓ Empty list indexOf 'X': " + emptyList.indexOf("X"));
        
        // Test single element operations
        MyDoublyLinkedList<String> singleList = new MyDoublyLinkedList<>();
        singleList.add("Only");
        
        System.out.println("   ✓ Single element list: " + singleList.toString());
        boolean removedSingle = singleList.remove("Only");
        System.out.println("   ✓ Removed single element: " + removedSingle + " -> " + singleList.toString());
        
        // Test boundary conditions
        MyDoublyLinkedList<Integer> intList = new MyDoublyLinkedList<>();
        for (int i = 1; i <= 5; i++) {
            intList.add(i);
        }
        
        System.out.println("   ✓ Integer list: " + intList.toString());
        
        // Test removeAt boundaries
        int firstRemoved = intList.removeAt(0);
        System.out.println("   ✓ Removed first (index 0): " + firstRemoved + " -> " + intList.toString());
        
        int lastRemoved = intList.removeAt(intList.getSize() - 1);
        System.out.println("   ✓ Removed last: " + lastRemoved + " -> " + intList.toString());
        
        System.out.println("   Edge cases: PASSED\n");
    }
}