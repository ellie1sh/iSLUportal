import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Entity class representing a course in the student portal system
 * Aligned with the existing codebase structure and data management approach
 */
public class Course {
    private Long id;
    private String classCode;
    private String courseNumber;
    private String courseDescription;
    private Integer units;
    private LocalTime startTime;
    private LocalTime endTime;
    private String days;
    private String room;
    
    // Constructors
    public Course() {}
    
    public Course(String classCode, String courseNumber, String courseDescription, 
                  Integer units, LocalTime startTime, LocalTime endTime, String days, String room) {
        this.classCode = classCode;
        this.courseNumber = courseNumber;
        this.courseDescription = courseDescription;
        this.units = units;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
        this.room = room;
    }
    
    public Course(Long id, String classCode, String courseNumber, String courseDescription, 
                  Integer units, LocalTime startTime, LocalTime endTime, String days, String room) {
        this.id = id;
        this.classCode = classCode;
        this.courseNumber = courseNumber;
        this.courseDescription = courseDescription;
        this.units = units;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
        this.room = room;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }
    
    public String getCourseNumber() { return courseNumber; }
    public void setCourseNumber(String courseNumber) { this.courseNumber = courseNumber; }
    
    public String getCourseDescription() { return courseDescription; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }
    
    public Integer getUnits() { return units; }
    public void setUnits(Integer units) { this.units = units; }
    
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    
    public String getDays() { return days; }
    public void setDays(String days) { this.days = days; }
    
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    
    /**
     * Gets formatted schedule display string for GUI
     * @return Formatted time display (e.g., "0730 - 0830 AM")
     */
    public String getScheduleDisplay() {
        if (startTime == null || endTime == null) {
            return "";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        String startFormatted = startTime.format(formatter);
        String endFormatted = endTime.format(formatter);
        String amPm = startTime.getHour() < 12 ? "AM" : "PM";
        
        return String.format("%s - %s %s", startFormatted, endFormatted, amPm);
    }
    
    /**
     * Gets the time range as a formatted string for display
     * @return Time range string (e.g., "7:30-8:30")
     */
    public String getTimeRange() {
        if (startTime == null || endTime == null) {
            return "";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return startTime.format(formatter) + "-" + endTime.format(formatter);
    }
    
    /**
     * Gets the full course display name combining course number and description
     * @return Full course name for display
     */
    public String getFullCourseName() {
        if (courseNumber != null && courseDescription != null) {
            return courseNumber + " - " + courseDescription;
        } else if (courseNumber != null) {
            return courseNumber;
        } else if (courseDescription != null) {
            return courseDescription;
        }
        return "";
    }
    
    /**
     * Checks if this course is scheduled on the given day
     * @param day Day to check (e.g., "M", "T", "W", "TH", "F")
     * @return true if course is scheduled on the given day
     */
    public boolean isScheduledOnDay(String day) {
        if (days == null || day == null) {
            return false;
        }
        
        // Handle common day abbreviations
        String normalizedDays = days.toUpperCase();
        String normalizedDay = day.toUpperCase();
        
        // Special handling for Thursday (TH)
        if ("TH".equals(normalizedDay) || "THURSDAY".equals(normalizedDay)) {
            return normalizedDays.contains("TH");
        }
        
        // Handle other days
        switch (normalizedDay) {
            case "M":
            case "MONDAY":
                return normalizedDays.contains("M") && !normalizedDays.contains("TH");
            case "T":
            case "TUESDAY":
                return normalizedDays.contains("T") && !normalizedDays.contains("TH");
            case "W":
            case "WEDNESDAY":
                return normalizedDays.contains("W");
            case "F":
            case "FRIDAY":
                return normalizedDays.contains("F");
            default:
                return normalizedDays.contains(normalizedDay);
        }
    }
    
    /**
     * Converts course data to database format for persistence
     * Format: id|classCode|courseNumber|courseDescription|units|startTime|endTime|days|room
     * Using pipe separator to avoid issues with commas in descriptions
     * @return Database format string
     */
    public String toDatabaseFormat() {
        return String.join("|",
            id != null ? id.toString() : "",
            classCode != null ? classCode.replaceAll("\\|", "_") : "",
            courseNumber != null ? courseNumber.replaceAll("\\|", "_") : "",
            courseDescription != null ? courseDescription.replaceAll("\\|", "_") : "",
            units != null ? units.toString() : "",
            startTime != null ? startTime.toString() : "",
            endTime != null ? endTime.toString() : "",
            days != null ? days.replaceAll("\\|", "_") : "",
            room != null ? room.replaceAll("\\|", "_") : ""
        );
    }
    
    /**
     * Creates a Course object from database format string
     * @param databaseString Database format string
     * @return Course object or null if parsing fails
     */
    public static Course fromDatabaseFormat(String databaseString) {
        try {
            String[] parts = databaseString.split("\\|", -1); // -1 to include empty trailing strings
            if (parts.length < 9) {
                return null;
            }
            
            Long id = parts[0].isEmpty() ? null : Long.parseLong(parts[0]);
            String classCode = parts[1].isEmpty() ? null : parts[1].replaceAll("_", "|");
            String courseNumber = parts[2].isEmpty() ? null : parts[2].replaceAll("_", "|");
            String courseDescription = parts[3].isEmpty() ? null : parts[3].replaceAll("_", "|");
            Integer units = parts[4].isEmpty() ? null : Integer.parseInt(parts[4]);
            LocalTime startTime = parts[5].isEmpty() ? null : LocalTime.parse(parts[5]);
            LocalTime endTime = parts[6].isEmpty() ? null : LocalTime.parse(parts[6]);
            String days = parts[7].isEmpty() ? null : parts[7].replaceAll("_", "|");
            String room = parts[8].isEmpty() ? null : parts[8].replaceAll("_", "|");
            
            return new Course(id, classCode, courseNumber, courseDescription, units, startTime, endTime, days, room);
        } catch (Exception e) {
            System.err.println("Error parsing course from database format: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Course{id=%d, classCode='%s', courseNumber='%s', courseDescription='%s', units=%d, startTime=%s, endTime=%s, days='%s', room='%s'}",
            id, classCode, courseNumber, courseDescription, units, startTime, endTime, days, room);
    }
}