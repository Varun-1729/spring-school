package com.example.school.controller;

import com.example.school.model.Grade;
import com.example.school.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/grades")
@CrossOrigin(origins = "*")
public class GradeController {
    
    @Autowired
    private GradeService gradeService;
    
    @GetMapping
    public ResponseEntity<List<Grade>> getAllGrades() {
        List<Grade> grades = gradeService.getAllGrades();
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        Optional<Grade> grade = gradeService.getGradeById(id);
        return grade.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Grade>> getGradesByStudentId(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesByStudentId(studentId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByCourseId(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByCourseId(courseId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<Grade>> getGradesByStudentAndCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesByStudentAndCourse(studentId, courseId);
        return ResponseEntity.ok(grades);
    }
    
    @GetMapping("/student/{studentId}/assignment/{assignmentId}")
    public ResponseEntity<Grade> getGradeByStudentAndAssignment(@PathVariable Long studentId, @PathVariable Long assignmentId) {
        Optional<Grade> grade = gradeService.getGradeByStudentAndAssignment(studentId, assignmentId);
        return grade.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/student/{studentId}/average")
    public ResponseEntity<Map<String, Object>> getAverageGradeByStudent(@PathVariable Long studentId) {
        Optional<BigDecimal> averageGrade = gradeService.getAverageGradeByStudent(studentId);
        if (averageGrade.isPresent()) {
            return ResponseEntity.ok(Map.of(
                "studentId", studentId,
                "averageGrade", averageGrade.get()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "studentId", studentId,
                "averageGrade", "No grades found"
            ));
        }
    }
    
    @GetMapping("/student/{studentId}/course/{courseId}/average")
    public ResponseEntity<Map<String, Object>> getAverageGradeByStudentAndCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        Optional<BigDecimal> averageGrade = gradeService.getAverageGradeByStudentAndCourse(studentId, courseId);
        if (averageGrade.isPresent()) {
            return ResponseEntity.ok(Map.of(
                "studentId", studentId,
                "courseId", courseId,
                "averageGrade", averageGrade.get()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "studentId", studentId,
                "courseId", courseId,
                "averageGrade", "No grades found"
            ));
        }
    }
    
    @PostMapping
    public ResponseEntity<?> assignGrade(@Valid @RequestBody Grade grade) {
        try {
            Grade assignedGrade = gradeService.assignGrade(grade);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable Long id, @Valid @RequestBody Grade gradeDetails) {
        try {
            Grade updatedGrade = gradeService.updateGrade(id, gradeDetails);
            return ResponseEntity.ok(updatedGrade);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        try {
            gradeService.deleteGrade(id);
            return ResponseEntity.ok().body(Map.of("message", "Grade deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
