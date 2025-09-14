package entity;

import java.time.LocalTime;

/**
 * Course entity class adapted for ISLU Student Portal
 * Contains course information including schedule details
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
     * Returns formatted schedule display string
     * @return Formatted time display (e.g., "1330 - 1430 PM")
     */
    public String getScheduleDisplay() {
        if (startTime == null || endTime == null) return "TBA";
        
        return String.format("%02d%02d - %02d%02d %s", 
            startTime.getHour(), startTime.getMinute(),
            endTime.getHour(), endTime.getMinute(),
            endTime.getHour() < 12 ? "AM" : "PM");
    }
    
    /**
     * Returns a formatted string representation of the course
     * @return Course details as a formatted string
     */
    @Override
    public String toString() {
        return String.format("%s - %s: %s (%d units) - %s %s Room %s", 
            classCode, courseNumber, courseDescription, units, 
            getScheduleDisplay(), days, room);
    }
    
    /**
     * Returns course data as an array for table display
     * @return Object array for JTable
     */
    public Object[] toTableRow() {
        return new Object[]{
            classCode,
            courseNumber,
            courseDescription,
            units,
            getScheduleDisplay(),
            days,
            room
        };
    }
}