package com.example.school.controller;

import com.example.school.model.Course;
import com.example.school.model.User;
import com.example.school.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{courseCode}")
    public ResponseEntity<Course> getCourseByCourseCode(@PathVariable String courseCode) {
        Optional<Course> course = courseService.getCourseByCourseCode(courseCode);
        return course.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Course>> getCoursesByTeacherId(@PathVariable Long teacherId) {
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Course>> getCoursesByStudentId(@PathVariable Long studentId) {
        List<Course> courses = courseService.getCoursesByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchCoursesByName(@RequestParam String name) {
        List<Course> courses = courseService.searchCoursesByName(name);
        return ResponseEntity.ok(courses);
    }
    
    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @Valid @RequestBody Course courseDetails) {
        try {
            Course updatedCourse = courseService.updateCourse(id, courseDetails);
            return ResponseEntity.ok(updatedCourse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().body(Map.of("message", "Course deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<?> assignStudentToCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            Course updatedCourse = courseService.assignStudentToCourse(courseId, studentId);
            return ResponseEntity.ok(Map.of(
                "message", "Student assigned to course successfully",
                "course", updatedCourse
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<?> removeStudentFromCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            Course updatedCourse = courseService.removeStudentFromCourse(courseId, studentId);
            return ResponseEntity.ok(Map.of(
                "message", "Student removed from course successfully",
                "course", updatedCourse
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/exists/{courseCode}")
    public ResponseEntity<Map<String, Boolean>> checkCourseCodeExists(@PathVariable String courseCode) {
        boolean exists = courseService.existsByCourseCode(courseCode);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // Admin-only course assignment endpoints
    @PutMapping("/{courseId}/assign-teacher/{teacherId}/admin/{adminId}")
    public ResponseEntity<?> assignTeacherToCourse(@PathVariable Long courseId,
                                                  @PathVariable Long teacherId,
                                                  @PathVariable Long adminId) {
        try {
            Course updatedCourse = courseService.assignTeacherToCourse(courseId, teacherId, adminId);
            return ResponseEntity.ok(Map.of(
                "message", "Teacher assigned to course successfully",
                "course", updatedCourse
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{courseId}/remove-teacher/admin/{adminId}")
    public ResponseEntity<?> removeTeacherFromCourse(@PathVariable Long courseId, @PathVariable Long adminId) {
        try {
            Course updatedCourse = courseService.removeTeacherFromCourse(courseId, adminId);
            return ResponseEntity.ok(Map.of(
                "message", "Teacher removed from course successfully",
                "course", updatedCourse
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<Course>> getUnassignedCourses() {
        List<Course> courses = courseService.getUnassignedCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/available-teachers")
    public ResponseEntity<List<User>> getAvailableTeachers() {
        List<User> teachers = courseService.getAvailableTeachers();
        return ResponseEntity.ok(teachers);
    }

    @PutMapping("/assign-multiple/teacher/{teacherId}/admin/{adminId}")
    public ResponseEntity<?> assignMultipleCoursesToTeacher(@PathVariable Long teacherId,
                                                           @PathVariable Long adminId,
                                                           @RequestBody List<Long> courseIds) {
        try {
            List<Course> updatedCourses = courseService.assignMultipleCoursesToTeacher(courseIds, teacherId, adminId);
            return ResponseEntity.ok(Map.of(
                "message", "Courses assigned to teacher successfully",
                "courses", updatedCourses
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
