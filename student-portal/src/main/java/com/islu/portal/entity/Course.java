package com.islu.portal.entity;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "course_number")
    private String courseNumber;

    @Column(name = "course_description")
    private String courseDescription;

    private Integer units;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private String days;
    private String room;

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

    public String getScheduleDisplay() {
        if (startTime == null || endTime == null) {
            return "";
        }
        int startHour = startTime.getHour();
        int endHour = endTime.getHour();
        String startAmPm = startHour < 12 ? "AM" : "PM";
        String endAmPm = endHour < 12 ? "AM" : "PM";
        return String.format("%02d%02d %s - %02d%02d %s",
            startTime.getHour() % 12 == 0 ? 12 : startTime.getHour() % 12, startTime.getMinute(), startAmPm,
            endTime.getHour() % 12 == 0 ? 12 : endTime.getHour() % 12, endTime.getMinute(), endAmPm);
    }
}

