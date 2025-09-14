import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service/helper for building a schedule grid for the Swing UI.
 */
public class ScheduleService {

    public static List<Course> getSampleCourses() {
        // Sample courses adapted from the provided Spring Boot DataInitializer
        return Arrays.asList(
            new Course("7024", "NSTP-CWTS 1", "FOUNDATIONS OF SERVICE", 3,
                LocalTime.of(13, 30), LocalTime.of(14, 30), "MWF", "D906"),
            new Course("9454", "GSTS", "SCIENCE, TECHNOLOGY, AND SOCIETY", 3,
                LocalTime.of(9, 30), LocalTime.of(10, 30), "TTHS", "D504"),
            new Course("9455", "GENVI", "ENVIRONMENTAL SCIENCE", 3,
                LocalTime.of(9, 30), LocalTime.of(10, 30), "MWF", "D503"),
            new Course("9456", "CFE 103", "CATHOLIC FOUNDATION OF MISSION", 3,
                LocalTime.of(13, 30), LocalTime.of(14, 30), "TTHS", "D503"),
            new Course("9457", "IT 211", "REQUIREMENTS ANALYSIS AND MODELING", 3,
                LocalTime.of(10, 30), LocalTime.of(11, 30), "MWF", "D511"),
            new Course("9458A", "IT 212", "DATA STRUCTURES (LEC)", 2,
                LocalTime.of(14, 30), LocalTime.of(15, 30), "TF", "D513"),
            new Course("9458B", "IT 212L", "DATA STRUCTURES (LAB)", 1,
                LocalTime.of(16, 0), LocalTime.of(17, 30), "TF", "D522"),
            new Course("9459A", "IT 213", "NETWORK FUNDAMENTALS (LEC)", 2,
                LocalTime.of(8, 30), LocalTime.of(9, 30), "TF", "D513"),
            new Course("9459B", "IT 213L", "NETWORK FUNDAMENTALS (LAB)", 1,
                LocalTime.of(11, 30), LocalTime.of(13, 0), "TF", "D528"),
            new Course("9547", "FIT OA", "PHYSICAL ACTIVITY TOWARDS HEALTH AND FITNESS (OUTDOOR AND ADVENTURE ACTIVITIES)", 2,
                LocalTime.of(15, 30), LocalTime.of(17, 30), "TH", "D221")
        );
    }

    public static Object[][] buildScheduleData(List<Course> courses, String[] columnNames) {
        if (courses == null || courses.isEmpty()) {
            return new Object[0][columnNames.length];
        }

        LocalTime earliest = null;
        LocalTime latest = null;
        for (Course c : courses) {
            LocalTime s = c.getStartTime();
            LocalTime e = c.getEndTime();
            if (earliest == null || s.isBefore(earliest)) earliest = s;
            if (latest == null || e.isAfter(latest)) latest = e;
        }

        earliest = floorToHalfHour(earliest);
        latest = ceilToHalfHour(latest);

        // Build half-hour time slots
        List<LocalTime> slots = new ArrayList<>();
        LocalTime cursor = earliest;
        while (!cursor.isAfter(latest)) {
            slots.add(cursor);
            cursor = cursor.plusMinutes(30);
        }
        if (slots.size() < 2) {
            return new Object[0][columnNames.length];
        }

        int rows = slots.size() - 1; // intervals
        int cols = columnNames.length;
        Object[][] data = new Object[rows][cols];

        for (int r = 0; r < rows; r++) {
            LocalTime start = slots.get(r);
            LocalTime end = slots.get(r + 1);
            data[r][0] = formatTimeRange(start, end);
            for (int c = 1; c < cols; c++) {
                data[r][c] = "";
            }
        }

        // Fill courses into cells
        for (Course course : courses) {
            Set<Integer> dayColumns = parseDayColumns(course.getDays());
            for (int r = 0; r < rows; r++) {
                LocalTime slotStart = slots.get(r);
                LocalTime slotEnd = slots.get(r + 1);
                if (overlaps(slotStart, slotEnd, course.getStartTime(), course.getEndTime())) {
                    for (int col : dayColumns) {
                        if (col <= 0 || col >= cols) continue; // guard
                        String existing = data[r][col] == null ? "" : data[r][col].toString();
                        String add = course.getCourseNumber();
                        if (course.getRoom() != null && !course.getRoom().isEmpty()) {
                            add += " (" + course.getRoom() + ")";
                        }
                        if (existing.isEmpty()) {
                            data[r][col] = add;
                        } else if (!existing.contains(add)) {
                            data[r][col] = existing + " | " + add;
                        }
                    }
                }
            }
        }

        return data;
    }

    private static boolean overlaps(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }

    private static LocalTime floorToHalfHour(LocalTime t) {
        int minute = t.getMinute();
        if (minute < 30) return t.withMinute(0).withSecond(0).withNano(0);
        return t.withMinute(30).withSecond(0).withNano(0);
    }

    private static LocalTime ceilToHalfHour(LocalTime t) {
        int minute = t.getMinute();
        if (minute == 0) return t.withSecond(0).withNano(0);
        if (minute <= 30) return t.withMinute(30).withSecond(0).withNano(0);
        return t.plusHours(1).withMinute(0).withSecond(0).withNano(0);
    }

    private static String formatTimeRange(LocalTime start, LocalTime end) {
        return String.format("%02d:%02d-%02d:%02d", start.getHour(), start.getMinute(), end.getHour(), end.getMinute());
    }

    /**
     * Parses day codes like "MWF", "TF", "TH", "TTHS" into column indices.
     * Columns: 0=Time, 1=Mon, 2=Tue, 3=Wed, 4=Thu, 5=Fri (Saturday ignored).
     */
    private static Set<Integer> parseDayColumns(String code) {
        Set<Integer> cols = new LinkedHashSet<>();
        if (code == null) return cols;
        String s = code.toUpperCase().replaceAll("\\s+", "");
        int i = 0;
        while (i < s.length()) {
            if (i + 1 < s.length() && s.substring(i, i + 2).equals("TH")) {
                cols.add(4); // Thursday
                i += 2;
                continue;
            }
            char c = s.charAt(i);
            switch (c) {
                case 'M': cols.add(1); break; // Monday
                case 'T': cols.add(2); break; // Tuesday
                case 'W': cols.add(3); break; // Wednesday
                case 'F': cols.add(5); break; // Friday
                case 'S': /* Saturday - not displayed */ break;
                default: break;
            }
            i++;
        }
        return cols;
    }
}

