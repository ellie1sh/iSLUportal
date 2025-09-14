import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Integration class that connects the Java Swing portal with Spring Boot backend
 * This class provides enhanced schedule functionality using real course data
 */
public class SpringScheduleIntegration {
    
    // Sample course data that matches the Spring Boot entities
    private static final List<CourseData> SAMPLE_COURSES = new ArrayList<>();
    
    static {
        // Initialize with the same data as in DataInitializer.java
        SAMPLE_COURSES.add(new CourseData("7024", "NSTP-CWTS 1", "FOUNDATIONS OF SERVICE", 3, 
                          "13:30", "14:30", "MWF", "D906"));
        SAMPLE_COURSES.add(new CourseData("9454", "GSTS", "SCIENCE, TECHNOLOGY, AND SOCIETY", 3,
                          "09:30", "10:30", "TTHS", "D504"));
        SAMPLE_COURSES.add(new CourseData("9455", "GENVI", "ENVIRONMENTAL SCIENCE", 3,
                          "09:30", "10:30", "MWF", "D503"));
        SAMPLE_COURSES.add(new CourseData("9456", "CFE 103", "CATHOLIC FOUNDATION OF MISSION", 3,
                          "13:30", "14:30", "TTHS", "D503"));
        SAMPLE_COURSES.add(new CourseData("9457", "IT 211", "REQUIREMENTS ANALYSIS AND MODELING", 3,
                          "10:30", "11:30", "MWF", "D511"));
        SAMPLE_COURSES.add(new CourseData("9458A", "IT 212", "DATA STRUCTURES (LEC)", 2,
                          "14:30", "15:30", "TF", "D513"));
        SAMPLE_COURSES.add(new CourseData("9458B", "IT 212L", "DATA STRUCTURES (LAB)", 1,
                          "16:00", "17:30", "TF", "D522"));
        SAMPLE_COURSES.add(new CourseData("9459A", "IT 213", "NETWORK FUNDAMENTALS (LEC)", 2,
                          "08:30", "09:30", "TF", "D513"));
        SAMPLE_COURSES.add(new CourseData("9459B", "IT 213L", "NETWORK FUNDAMENTALS (LAB)", 1,
                          "11:30", "13:00", "TF", "D528"));
        SAMPLE_COURSES.add(new CourseData("9547", "FIT OA", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (OUTDOOR AND ADVENTURE ACTIVITIES)", 2,
                          "15:30", "17:30", "TH", "D221"));
    }
    
    /**
     * Creates an enhanced schedule panel with detailed course information
     * @param studentId The student ID to fetch schedule for
     * @return JPanel containing the enhanced schedule
     */
    public static JPanel createEnhancedSchedulePanel(String studentId) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Header panel
        JPanel headerPanel = createScheduleHeader(studentId);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Schedule content panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(Color.WHITE);
        
        // Left panel - Weekly Schedule
        JPanel schedulePanel = createWeeklySchedulePanel();
        contentPanel.add(schedulePanel);
        
        // Right panel - Course Details
        JPanel detailsPanel = createCourseDetailsPanel();
        contentPanel.add(detailsPanel);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer panel with summary
        JPanel footerPanel = createScheduleSummary();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private static JPanel createScheduleHeader(String studentId) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(10, 45, 90));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("📅 Class Schedule - FIRST SEMESTER, 2025-2026");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel studentLabel = new JLabel("Student: " + studentId);
        studentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        studentLabel.setForeground(Color.WHITE);
        headerPanel.add(studentLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private static JPanel createWeeklySchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Weekly Schedule",
            0, 0, new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);
        
        // Create schedule table
        String[] columnNames = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[][] scheduleData = generateScheduleMatrix();
        
        DefaultTableModel model = new DefaultTableModel(scheduleData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable scheduleTable = new JTable(model);
        scheduleTable.setRowHeight(35);
        scheduleTable.setFont(new Font("Arial", Font.PLAIN, 11));
        scheduleTable.getTableHeader().setBackground(new Color(240, 240, 240));
        scheduleTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set column widths
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        for (int i = 1; i < columnNames.length; i++) {
            scheduleTable.getColumnModel().getColumn(i).setPreferredWidth(120);
        }
        
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private static JPanel createCourseDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Course Details",
            0, 0, new Font("Arial", Font.BOLD, 14)
        ));
        panel.setBackground(Color.WHITE);
        
        // Create course details table
        String[] columnNames = {"Class Code", "Course", "Description", "Units", "Room", "Schedule"};
        Object[][] courseData = generateCourseDetailsMatrix();
        
        DefaultTableModel model = new DefaultTableModel(courseData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable courseTable = new JTable(model);
        courseTable.setRowHeight(25);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 10));
        courseTable.getTableHeader().setBackground(new Color(240, 240, 240));
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        
        // Set column widths
        courseTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        courseTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        courseTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        courseTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        courseTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(courseTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private static JPanel createScheduleSummary() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Calculate total units
        int totalUnits = SAMPLE_COURSES.stream().mapToInt(CourseData::getUnits).sum();
        
        JLabel summaryLabel = new JLabel("📊 Schedule Summary:");
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(summaryLabel);
        
        JLabel totalCoursesLabel = new JLabel("Total Courses: " + SAMPLE_COURSES.size());
        totalCoursesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(totalCoursesLabel);
        
        JLabel totalUnitsLabel = new JLabel("Total Units: " + totalUnits);
        totalUnitsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(totalUnitsLabel);
        
        JLabel statusLabel = new JLabel("Status: Currently Enrolled");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(new Color(0, 150, 0));
        panel.add(statusLabel);
        
        return panel;
    }
    
    private static String[][] generateScheduleMatrix() {
        // Time slots from 7:00 AM to 6:00 PM
        String[] timeSlots = {
            "07:00-08:00", "08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
            "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00"
        };
        
        String[][] matrix = new String[timeSlots.length][6];
        
        // Initialize with time slots and empty cells
        for (int i = 0; i < timeSlots.length; i++) {
            matrix[i][0] = timeSlots[i];
            for (int j = 1; j < 6; j++) {
                matrix[i][j] = "";
            }
        }
        
        // Map days to column indices
        Map<String, Integer> dayMap = new HashMap<>();
        dayMap.put("M", 1); dayMap.put("T", 2); dayMap.put("W", 3); dayMap.put("TH", 4); dayMap.put("F", 5);
        
        // Fill in courses
        for (CourseData course : SAMPLE_COURSES) {
            int startHour = Integer.parseInt(course.getStartTime().split(":")[0]);
            int timeSlotIndex = startHour - 7; // Adjust for 7 AM start
            
            if (timeSlotIndex >= 0 && timeSlotIndex < timeSlots.length) {
                String days = course.getDays();
                String courseDisplay = course.getCourseNumber() + " (" + course.getRoom() + ")";
                
                // Parse days and place course in appropriate columns
                if (days.contains("MWF")) {
                    matrix[timeSlotIndex][1] = courseDisplay; // Monday
                    matrix[timeSlotIndex][3] = courseDisplay; // Wednesday
                    matrix[timeSlotIndex][5] = courseDisplay; // Friday
                } else if (days.contains("TTHS")) {
                    matrix[timeSlotIndex][2] = courseDisplay; // Tuesday
                    matrix[timeSlotIndex][4] = courseDisplay; // Thursday
                } else if (days.contains("TF")) {
                    matrix[timeSlotIndex][2] = courseDisplay; // Tuesday
                    matrix[timeSlotIndex][5] = courseDisplay; // Friday
                } else if (days.contains("TH")) {
                    matrix[timeSlotIndex][4] = courseDisplay; // Thursday
                }
            }
        }
        
        return matrix;
    }
    
    private static Object[][] generateCourseDetailsMatrix() {
        Object[][] matrix = new Object[SAMPLE_COURSES.size()][6];
        
        for (int i = 0; i < SAMPLE_COURSES.size(); i++) {
            CourseData course = SAMPLE_COURSES.get(i);
            matrix[i][0] = course.getClassCode();
            matrix[i][1] = course.getCourseNumber();
            matrix[i][2] = course.getDescription().length() > 30 ? 
                          course.getDescription().substring(0, 30) + "..." : 
                          course.getDescription();
            matrix[i][3] = course.getUnits();
            matrix[i][4] = course.getRoom();
            matrix[i][5] = course.getStartTime() + "-" + course.getEndTime() + " " + course.getDays();
        }
        
        return matrix;
    }
    
    /**
     * Data class to represent course information
     */
    public static class CourseData {
        private String classCode;
        private String courseNumber;
        private String description;
        private int units;
        private String startTime;
        private String endTime;
        private String days;
        private String room;
        
        public CourseData(String classCode, String courseNumber, String description, 
                         int units, String startTime, String endTime, String days, String room) {
            this.classCode = classCode;
            this.courseNumber = courseNumber;
            this.description = description;
            this.units = units;
            this.startTime = startTime;
            this.endTime = endTime;
            this.days = days;
            this.room = room;
        }
        
        // Getters
        public String getClassCode() { return classCode; }
        public String getCourseNumber() { return courseNumber; }
        public String getDescription() { return description; }
        public int getUnits() { return units; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getDays() { return days; }
        public String getRoom() { return room; }
    }
    
    /**
     * Method to test the Spring Boot backend connection (if available)
     * @param studentId The student ID to fetch data for
     * @return true if connection successful, false otherwise
     */
    public static boolean testSpringBootConnection(String studentId) {
        try {
            URL url = new URL("http://localhost:8080/students/api/schedule/" + studentId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            System.out.println("Spring Boot backend not available, using local data: " + e.getMessage());
            return false;
        }
    }
}