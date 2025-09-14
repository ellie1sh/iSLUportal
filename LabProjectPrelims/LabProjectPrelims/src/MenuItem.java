import java.util.LinkedList;

// Custom class to hold menu data
public class MenuItem {
    private String name;
    private LinkedList<String> subItems;

    public MenuItem(String name) {
        this.name = name;
        this.subItems = null; // No sublist by default
    }

    public MenuItem(String name, LinkedList<String> subItems) {
        this.name = name;
        this.subItems = subItems;
    }

    public String getName() {
        return name;
    }

    public LinkedList<String> getSubItems() {
        return subItems;
    }

    public boolean hasSubItems() {
        return subItems != null && !subItems.isEmpty();
    }
}