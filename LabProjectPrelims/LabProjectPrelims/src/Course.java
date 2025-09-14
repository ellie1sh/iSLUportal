import java.time.LocalTime;

/**
 * Simple POJO representing a course schedule entry.
 * This adapts the provided Spring entity to the current Swing app (no JPA).
 */
public class Course {
    private Long id;
    private String classCode;
    private String courseNumber;
    private String courseDescription;
    private int units;
    private LocalTime startTime;
    private LocalTime endTime;
    private String days; // e.g., MWF, TF, TH, TTHS
    private String room;

    public Course() {}

    public Course(String classCode, String courseNumber, String courseDescription,
                  int units, LocalTime startTime, LocalTime endTime, String days, String room) {
        this.classCode = classCode;
        this.courseNumber = courseNumber;
        this.courseDescription = courseDescription;
        this.units = units;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
        this.room = room;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public String getScheduleDisplay() {
        return String.format("%02d%02d - %02d%02d %s",
                startTime.getHour(), startTime.getMinute(),
                endTime.getHour(), endTime.getMinute(),
                startTime.getHour() < 12 ? "AM" : "PM");
    }

    public String getDisplayName() {
        // Short display for schedule grid
        return courseNumber + (room != null && !room.isEmpty() ? " (" + room + ")" : "");
    }
}

