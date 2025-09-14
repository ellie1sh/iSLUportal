import java.time.LocalTime;

/**
 * Plain model representing a course scheduled for a student.
 * Aligned to the original JPA entity fields but adapted for this Swing app.
 */
public class Course {
    private String classCode;
    private String courseNumber;
    private String courseDescription;
    private int units;
    private LocalTime startTime;
    private LocalTime endTime;
    private String days; // e.g., "MWF", "TTHS", "TF"
    private String room;

    public Course() {}

    public Course(String classCode,
                  String courseNumber,
                  String courseDescription,
                  int units,
                  LocalTime startTime,
                  LocalTime endTime,
                  String days,
                  String room) {
        this.classCode = classCode;
        this.courseNumber = courseNumber;
        this.courseDescription = courseDescription;
        this.units = units;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
        this.room = room;
    }

    public String getClassCode() { return classCode; }
    public void setClassCode(String classCode) { this.classCode = classCode; }

    public String getCourseNumber() { return courseNumber; }
    public void setCourseNumber(String courseNumber) { this.courseNumber = courseNumber; }

    public String getCourseDescription() { return courseDescription; }
    public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getDays() { return days; }
    public void setDays(String days) { this.days = days; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    /**
     * Returns a human-readable time range like "1:30 PM - 2:30 PM".
     */
    public String getScheduleDisplay() {
        return formatTime(startTime) + " - " + formatTime(endTime);
    }

    private static String formatTime(LocalTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        String ampm = hour < 12 ? "AM" : "PM";
        int hour12 = hour % 12;
        if (hour12 == 0) hour12 = 12;
        return hour12 + ":" + String.format("%02d", minute) + " " + ampm;
    }
}

