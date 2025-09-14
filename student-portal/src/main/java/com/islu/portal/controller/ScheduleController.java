package com.islu.portal.controller;

import com.islu.portal.entity.Course;
import com.islu.portal.entity.Student;
import com.islu.portal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/students")
public class ScheduleController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/schedule/{studentId}")
    public String getStudentSchedule(@PathVariable String studentId, Model model) {
        Optional<Student> studentOpt = studentService.getStudentById(studentId);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            List<Course> courses = studentService.getStudentCourses(studentId);
            int totalUnits = studentService.getTotalUnits(studentId);

            model.addAttribute("student", student);
            model.addAttribute("courses", courses);
            model.addAttribute("totalUnits", totalUnits);

            return "schedule";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/api/schedule/{studentId}")
    @ResponseBody
    public ResponseEntity<ScheduleResponse> getScheduleApi(@PathVariable String studentId) {
        Optional<Student> studentOpt = studentService.getStudentById(studentId);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            List<Course> courses = studentService.getStudentCourses(studentId);
            int totalUnits = studentService.getTotalUnits(studentId);

            return ResponseEntity.ok(new ScheduleResponse(student, courses, totalUnits));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public static class ScheduleResponse {
        private Student student;
        private List<Course> courses;
        private int totalUnits;

        public ScheduleResponse(Student student, List<Course> courses, int totalUnits) {
            this.student = student;
            this.courses = courses;
            this.totalUnits = totalUnits;
        }

        public Student getStudent() { return student; }
        public void setStudent(Student student) { this.student = student; }

        public List<Course> getCourses() { return courses; }
        public void setCourses(List<Course> courses) { this.courses = courses; }

        public int getTotalUnits() { return totalUnits; }
        public void setTotalUnits(int totalUnits) { this.totalUnits = totalUnits; }
    }
}

