package com.example.school.controller;

import com.example.school.model.*;
import com.example.school.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin(origins = "*")
public class TeacherController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private GradeService gradeService;
    
    // Teacher dashboard overview
    @GetMapping("/dashboard/{teacherId}")
    public ResponseEntity<?> getTeacherDashboard(@PathVariable Long teacherId) {
        try {
            // Validate teacher
            User teacher = userService.getUserById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            
            if (teacher.getRole() != UserRole.TEACHER) {
                throw new RuntimeException("Access denied: Teacher role required");
            }
            
            // Gather dashboard data
            Map<String, Object> dashboard = new HashMap<>();
            
            // Teacher's courses
            List<Course> teacherCourses = courseService.getCoursesByTeacherId(teacherId);
            dashboard.put("totalCourses", teacherCourses.size());
            dashboard.put("courses", teacherCourses);
            
            // Students in teacher's courses
            List<Student> teacherStudents = studentService.getStudentsByTeacherId(teacherId);
            dashboard.put("totalStudents", teacherStudents.size());
            dashboard.put("students", teacherStudents.stream().limit(10).toList()); // Limit for dashboard
            
            // Assignments created by teacher
            List<Assignment> teacherAssignments = assignmentService.getAssignmentsByCreator(teacherId);
            dashboard.put("totalAssignments", teacherAssignments.size());
            dashboard.put("recentAssignments", teacherAssignments.stream().limit(5).toList());
            
            // Course-wise student count
            Map<String, Integer> courseStudentCount = new HashMap<>();
            for (Course course : teacherCourses) {
                courseStudentCount.put(course.getCourseName(), course.getStudents().size());
            }
            dashboard.put("courseStudentCount", courseStudentCount);
            
            return ResponseEntity.ok(dashboard);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get teacher's courses
    @GetMapping("/courses/{teacherId}")
    public ResponseEntity<?> getTeacherCourses(@PathVariable Long teacherId) {
        try {
            validateTeacher(teacherId);
            List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
            return ResponseEntity.ok(courses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get all students taught by teacher
    @GetMapping("/students/{teacherId}")
    public ResponseEntity<?> getTeacherStudents(@PathVariable Long teacherId) {
        try {
            validateTeacher(teacherId);
            List<Student> students = studentService.getStudentsByTeacherId(teacherId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get students in a specific course
    @GetMapping("/course/{courseId}/students/{teacherId}")
    public ResponseEntity<?> getStudentsInCourse(@PathVariable Long courseId, @PathVariable Long teacherId) {
        try {
            validateTeacher(teacherId);
            List<Student> students = studentService.getStudentsInTeacherCourse(teacherId, courseId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get teacher's assignments
    @GetMapping("/assignments/{teacherId}")
    public ResponseEntity<?> getTeacherAssignments(@PathVariable Long teacherId) {
        try {
            validateTeacher(teacherId);
            List<Assignment> assignments = assignmentService.getAssignmentsByCreator(teacherId);
            return ResponseEntity.ok(assignments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get assignments for a specific course
    @GetMapping("/course/{courseId}/assignments/{teacherId}")
    public ResponseEntity<?> getCourseAssignments(@PathVariable Long courseId, @PathVariable Long teacherId) {
        try {
            validateTeacher(teacherId);
            
            // Verify teacher teaches this course
            Course course = courseService.getCourseById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            
            if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
                throw new RuntimeException("Teacher can only access assignments from their assigned courses");
            }
            
            List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
            return ResponseEntity.ok(assignments);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get grades for teacher's courses
    @GetMapping("/grades/{teacherId}")
    public ResponseEntity<?> getTeacherGrades(@PathVariable Long teacherId) {
        try {
            validateTeacher(teacherId);
            
            // Get all courses taught by teacher
            List<Course> teacherCourses = courseService.getCoursesByTeacherId(teacherId);
            
            // Get grades for all these courses
            Map<String, List<Grade>> gradesByCourse = new HashMap<>();
            for (Course course : teacherCourses) {
                List<Grade> courseGrades = gradeService.getGradesByCourseId(course.getId());
                gradesByCourse.put(course.getCourseName(), courseGrades);
            }
            
            return ResponseEntity.ok(gradesByCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get grades for a specific course
    @GetMapping("/course/{courseId}/grades/{teacherId}")
    public ResponseEntity<?> getCourseGrades(@PathVariable Long courseId, @PathVariable Long teacherId) {
        try {
            validateTeacher(teacherId);
            
            // Verify teacher teaches this course
            Course course = courseService.getCourseById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            
            if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
                throw new RuntimeException("Teacher can only access grades from their assigned courses");
            }
            
            List<Grade> grades = gradeService.getGradesByCourseId(courseId);
            return ResponseEntity.ok(grades);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Teacher statistics
    @GetMapping("/statistics/{teacherId}")
    public ResponseEntity<?> getTeacherStatistics(@PathVariable Long teacherId) {
        try {
            validateTeacher(teacherId);
            
            Map<String, Object> stats = new HashMap<>();
            
            // Course statistics
            List<Course> teacherCourses = courseService.getCoursesByTeacherId(teacherId);
            stats.put("totalCourses", teacherCourses.size());
            
            // Student statistics
            List<Student> teacherStudents = studentService.getStudentsByTeacherId(teacherId);
            stats.put("totalStudents", teacherStudents.size());
            
            // Assignment statistics
            List<Assignment> teacherAssignments = assignmentService.getAssignmentsByCreator(teacherId);
            stats.put("totalAssignments", teacherAssignments.size());
            stats.put("activeAssignments", teacherAssignments.stream().filter(Assignment::getIsActive).count());
            
            // Course-wise breakdown
            Map<String, Map<String, Object>> courseBreakdown = new HashMap<>();
            for (Course course : teacherCourses) {
                Map<String, Object> courseStats = new HashMap<>();
                courseStats.put("studentCount", course.getStudents().size());
                courseStats.put("assignmentCount", assignmentService.getAssignmentsByCourseId(course.getId()).size());
                courseStats.put("gradeCount", gradeService.getGradesByCourseId(course.getId()).size());
                courseBreakdown.put(course.getCourseName(), courseStats);
            }
            stats.put("courseBreakdown", courseBreakdown);
            
            return ResponseEntity.ok(stats);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Helper method to validate teacher access
    private void validateTeacher(Long teacherId) {
        User teacher = userService.getUserById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        
        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("Access denied: Teacher role required");
        }
    }
}
