import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service class for schedule-related business logic
 * Provides methods for managing student schedules and course information
 */
public class ScheduleService {
    
    private static final String[] DAYS_OF_WEEK = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final String[] DAY_ABBREVIATIONS = {"M", "T", "W", "TH", "F"};
    private static final String[] TIME_SLOTS = {
        "7:00-8:00", "8:00-9:00", "9:00-10:00", "10:00-11:00", "11:00-12:00",
        "12:00-1:00", "1:00-2:00", "2:00-3:00", "3:00-4:00", "4:00-5:00", "5:00-6:00"
    };
    
    /**
     * Gets a student by ID with all course information loaded
     * @param studentId The student ID to look up
     * @return Optional containing the student with courses, or empty if not found
     */
    public static Optional<Student> getStudentById(String studentId) {
        Student student = CourseDataManager.getStudentWithCourses(studentId);
        return Optional.ofNullable(student);
    }
    
    /**
     * Gets a student with courses loaded (alternative method)
     * @param studentId The student ID to look up
     * @return Optional containing the student with courses, or empty if not found
     */
    public static Optional<Student> getStudentWithCourses(String studentId) {
        return getStudentById(studentId);
    }
    
    /**
     * Gets all courses for a specific student
     * @param studentId The student ID
     * @return List of courses the student is enrolled in
     */
    public static List<Course> getStudentCourses(String studentId) {
        return CourseDataManager.getCoursesByStudentId(studentId);
    }
    
    /**
     * Gets the total units for a student
     * @param studentId The student ID
     * @return Total units enrolled
     */
    public static int getTotalUnits(String studentId) {
        return CourseDataManager.getTotalUnitsByStudentId(studentId);
    }
    
    /**
     * Creates a schedule table data structure for GUI display
     * @param studentId The student ID
     * @return 2D array representing the schedule table
     */
    public static Object[][] createScheduleTableData(String studentId) {
        List<Course> courses = getStudentCourses(studentId);
        
        // Initialize the schedule grid
        Object[][] scheduleData = new Object[TIME_SLOTS.length][DAYS_OF_WEEK.length + 1]; // +1 for time column
        
        // Fill in time slots in the first column
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            scheduleData[i][0] = TIME_SLOTS[i];
            // Initialize other columns as empty
            for (int j = 1; j <= DAYS_OF_WEEK.length; j++) {
                scheduleData[i][j] = "";
            }
        }
        
        // Fill in courses
        for (Course course : courses) {
            if (course.getStartTime() != null && course.getEndTime() != null && course.getDays() != null) {
                fillCourseInSchedule(scheduleData, course);
            }
        }
        
        return scheduleData;
    }
    
    /**
     * Fills a course into the schedule grid
     * @param scheduleData The schedule grid to fill
     * @param course The course to place in the schedule
     */
    private static void fillCourseInSchedule(Object[][] scheduleData, Course course) {
        // Determine which days this course is scheduled
        for (int dayIndex = 0; dayIndex < DAY_ABBREVIATIONS.length; dayIndex++) {
            if (course.isScheduledOnDay(DAY_ABBREVIATIONS[dayIndex])) {
                // Find the appropriate time slot
                int timeSlotIndex = findTimeSlotIndex(course.getStartTime(), course.getEndTime());
                if (timeSlotIndex >= 0 && timeSlotIndex < scheduleData.length) {
                    // Place the course in the schedule (dayIndex + 1 because first column is time)
                    String courseDisplay = formatCourseForSchedule(course);
                    scheduleData[timeSlotIndex][dayIndex + 1] = courseDisplay;
                }
            }
        }
    }
    
    /**
     * Finds the appropriate time slot index for a course
     * @param startTime Course start time
     * @param endTime Course end time
     * @return Time slot index, or -1 if not found
     */
    private static int findTimeSlotIndex(LocalTime startTime, LocalTime endTime) {
        String timeRange = formatTimeRange(startTime, endTime);
        
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            if (TIME_SLOTS[i].equals(timeRange) || isTimeSlotMatch(TIME_SLOTS[i], startTime, endTime)) {
                return i;
            }
        }
        
        // If exact match not found, find the closest slot
        for (int i = 0; i < TIME_SLOTS.length; i++) {
            LocalTime slotStart = parseTimeFromSlot(TIME_SLOTS[i], true);
            if (slotStart != null && !startTime.isBefore(slotStart) && startTime.isBefore(slotStart.plusHours(1))) {
                return i;
            }
        }
        
        return -1; // Not found
    }
    
    /**
     * Formats a time range for comparison with time slots
     * @param startTime Start time
     * @param endTime End time
     * @return Formatted time range string
     */
    private static String formatTimeRange(LocalTime startTime, LocalTime endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        return startTime.format(formatter) + "-" + endTime.format(formatter);
    }
    
    /**
     * Checks if a time slot matches the given start and end times
     * @param timeSlot Time slot string (e.g., "8:00-9:00")
     * @param startTime Course start time
     * @param endTime Course end time
     * @return true if the time slot matches
     */
    private static boolean isTimeSlotMatch(String timeSlot, LocalTime startTime, LocalTime endTime) {
        try {
            String[] parts = timeSlot.split("-");
            if (parts.length == 2) {
                LocalTime slotStart = LocalTime.parse(parts[0].trim() + ":00");
                LocalTime slotEnd = LocalTime.parse(parts[1].trim() + ":00");
                
                // Check if course time overlaps with this slot
                return !startTime.isAfter(slotEnd) && !endTime.isBefore(slotStart);
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return false;
    }
    
    /**
     * Parses time from a time slot string
     * @param timeSlot Time slot string (e.g., "8:00-9:00")
     * @param getStart If true, returns start time; if false, returns end time
     * @return LocalTime object or null if parsing fails
     */
    private static LocalTime parseTimeFromSlot(String timeSlot, boolean getStart) {
        try {
            String[] parts = timeSlot.split("-");
            if (parts.length == 2) {
                String timeStr = getStart ? parts[0].trim() : parts[1].trim();
                if (!timeStr.contains(":")) {
                    timeStr += ":00";
                }
                return LocalTime.parse(timeStr);
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return null;
    }
    
    /**
     * Formats a course for display in the schedule
     * @param course The course to format
     * @return Formatted course string
     */
    private static String formatCourseForSchedule(Course course) {
        StringBuilder display = new StringBuilder();
        
        if (course.getCourseNumber() != null) {
            display.append(course.getCourseNumber());
        } else if (course.getClassCode() != null) {
            display.append(course.getClassCode());
        }
        
        if (course.getRoom() != null) {
            if (display.length() > 0) {
                display.append("\n");
            }
            display.append("Room: ").append(course.getRoom());
        }
        
        return display.toString();
    }
    
    /**
     * Gets courses scheduled for a specific day
     * @param studentId The student ID
     * @param day Day abbreviation (M, T, W, TH, F)
     * @return List of courses scheduled for the day
     */
    public static List<Course> getCoursesForDay(String studentId, String day) {
        List<Course> allCourses = getStudentCourses(studentId);
        List<Course> daysCourses = new ArrayList<>();
        
        for (Course course : allCourses) {
            if (course.isScheduledOnDay(day)) {
                daysCourses.add(course);
            }
        }
        
        // Sort by start time
        daysCourses.sort((c1, c2) -> {
            if (c1.getStartTime() == null && c2.getStartTime() == null) return 0;
            if (c1.getStartTime() == null) return 1;
            if (c2.getStartTime() == null) return -1;
            return c1.getStartTime().compareTo(c2.getStartTime());
        });
        
        return daysCourses;
    }
    
    /**
     * Gets a summary of the student's schedule
     * @param studentId The student ID
     * @return ScheduleSummary object containing schedule statistics
     */
    public static ScheduleSummary getScheduleSummary(String studentId) {
        List<Course> courses = getStudentCourses(studentId);
        int totalUnits = getTotalUnits(studentId);
        
        Map<String, Integer> coursesPerDay = new HashMap<>();
        for (String day : DAY_ABBREVIATIONS) {
            coursesPerDay.put(day, getCoursesForDay(studentId, day).size());
        }
        
        return new ScheduleSummary(courses.size(), totalUnits, coursesPerDay);
    }
    
    /**
     * Creates column names for the schedule table
     * @return Array of column names
     */
    public static String[] getScheduleColumnNames() {
        String[] columns = new String[DAYS_OF_WEEK.length + 1];
        columns[0] = "Time";
        System.arraycopy(DAYS_OF_WEEK, 0, columns, 1, DAYS_OF_WEEK.length);
        return columns;
    }
    
    /**
     * Validates if a course schedule conflicts with existing courses
     * @param studentId The student ID
     * @param newCourse The new course to check
     * @return List of conflicting courses, empty if no conflicts
     */
    public static List<Course> checkScheduleConflicts(String studentId, Course newCourse) {
        List<Course> conflicts = new ArrayList<>();
        List<Course> existingCourses = getStudentCourses(studentId);
        
        for (Course existing : existingCourses) {
            if (hasTimeConflict(existing, newCourse)) {
                conflicts.add(existing);
            }
        }
        
        return conflicts;
    }
    
    /**
     * Checks if two courses have a time conflict
     * @param course1 First course
     * @param course2 Second course
     * @return true if there's a conflict
     */
    private static boolean hasTimeConflict(Course course1, Course course2) {
        // Check if they share any common days
        if (course1.getDays() == null || course2.getDays() == null ||
            course1.getStartTime() == null || course1.getEndTime() == null ||
            course2.getStartTime() == null || course2.getEndTime() == null) {
            return false;
        }
        
        // Check for day overlap
        boolean hasCommonDay = false;
        for (String day : DAY_ABBREVIATIONS) {
            if (course1.isScheduledOnDay(day) && course2.isScheduledOnDay(day)) {
                hasCommonDay = true;
                break;
            }
        }
        
        if (!hasCommonDay) {
            return false;
        }
        
        // Check for time overlap
        return !course1.getEndTime().isBefore(course2.getStartTime()) && 
               !course2.getEndTime().isBefore(course1.getStartTime());
    }
    
    /**
     * Inner class to represent schedule summary information
     */
    public static class ScheduleSummary {
        private final int totalCourses;
        private final int totalUnits;
        private final Map<String, Integer> coursesPerDay;
        
        public ScheduleSummary(int totalCourses, int totalUnits, Map<String, Integer> coursesPerDay) {
            this.totalCourses = totalCourses;
            this.totalUnits = totalUnits;
            this.coursesPerDay = new HashMap<>(coursesPerDay);
        }
        
        public int getTotalCourses() { return totalCourses; }
        public int getTotalUnits() { return totalUnits; }
        public Map<String, Integer> getCoursesPerDay() { return coursesPerDay; }
        
        public int getCoursesForDay(String day) {
            return coursesPerDay.getOrDefault(day, 0);
        }
        
        @Override
        public String toString() {
            return String.format("ScheduleSummary{totalCourses=%d, totalUnits=%d, coursesPerDay=%s}",
                totalCourses, totalUnits, coursesPerDay);
        }
    }
}