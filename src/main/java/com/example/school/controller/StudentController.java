package com.example.school.controller;

import com.example.school.model.Student;
import com.example.school.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student-id/{studentId}")
    public ResponseEntity<Student> getStudentByStudentId(@PathVariable String studentId) {
        Optional<Student> student = studentService.getStudentByStudentId(studentId);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Student> getStudentByUserId(@PathVariable Long userId) {
        Optional<Student> student = studentService.getStudentByUserId(userId);
        return student.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/grade/{gradeLevel}")
    public ResponseEntity<List<Student>> getStudentsByGradeLevel(@PathVariable String gradeLevel) {
        List<Student> students = studentService.getStudentsByGradeLevel(gradeLevel);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Student>> getStudentsByCourseId(@PathVariable Long courseId) {
        List<Student> students = studentService.getStudentsByCourseId(courseId);
        return ResponseEntity.ok(students);
    }
    
    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {
        try {
            Student createdStudent = studentService.createStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        try {
            Student updatedStudent = studentService.updateStudent(id, studentDetails);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok().body(Map.of("message", "Student deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/exists/{studentId}")
    public ResponseEntity<Map<String, Boolean>> checkStudentIdExists(@PathVariable String studentId) {
        boolean exists = studentService.existsByStudentId(studentId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    // Teacher-specific endpoints
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getStudentsByTeacherId(@PathVariable Long teacherId) {
        try {
            List<Student> students = studentService.getStudentsByTeacherId(teacherId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/teacher/{teacherId}/course/{courseId}")
    public ResponseEntity<?> getStudentsInTeacherCourse(@PathVariable Long teacherId, @PathVariable Long courseId) {
        try {
            List<Student> students = studentService.getStudentsInTeacherCourse(teacherId, courseId);
            return ResponseEntity.ok(students);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{studentId}/teacher/{teacherId}")
    public ResponseEntity<?> updateStudentByTeacher(@PathVariable Long studentId,
                                                   @PathVariable Long teacherId,
                                                   @Valid @RequestBody Student studentDetails) {
        try {
            Student updatedStudent = studentService.updateStudentByTeacher(studentId, studentDetails, teacherId);
            return ResponseEntity.ok(Map.of(
                "message", "Student updated successfully by teacher",
                "student", updatedStudent
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Admin-specific endpoints
    @PutMapping("/{studentId}/admin/{adminId}")
    public ResponseEntity<?> updateStudentByAdmin(@PathVariable Long studentId,
                                                 @PathVariable Long adminId,
                                                 @Valid @RequestBody Student studentDetails) {
        try {
            Student updatedStudent = studentService.updateStudentByAdmin(studentId, studentDetails, adminId);
            return ResponseEntity.ok(Map.of(
                "message", "Student updated successfully by admin",
                "student", updatedStudent
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{studentId}/admin/{adminId}")
    public ResponseEntity<?> deleteStudentByAdmin(@PathVariable Long studentId, @PathVariable Long adminId) {
        try {
            studentService.deleteStudentByAdmin(studentId, adminId);
            return ResponseEntity.ok().body(Map.of("message", "Student deleted successfully by admin"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
