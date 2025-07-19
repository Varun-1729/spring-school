package com.example.school.controller;

import com.example.school.model.Assignment;
import com.example.school.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*")
public class AssignmentController {
    
    @Autowired
    private AssignmentService assignmentService;
    
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Assignment>> getAllActiveAssignments() {
        List<Assignment> assignments = assignmentService.getAllActiveAssignments();
        return ResponseEntity.ok(assignments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        Optional<Assignment> assignment = assignmentService.getAssignmentById(id);
        return assignment.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourseId(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        return ResponseEntity.ok(assignments);
    }
    
    @GetMapping("/course/{courseId}/active")
    public ResponseEntity<List<Assignment>> getActiveAssignmentsByCourseId(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getActiveAssignmentsByCourseId(courseId);
        return ResponseEntity.ok(assignments);
    }
    
    @GetMapping("/creator/{createdById}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCreator(@PathVariable Long createdById) {
        List<Assignment> assignments = assignmentService.getAssignmentsByCreator(createdById);
        return ResponseEntity.ok(assignments);
    }
    
    @GetMapping("/due-date")
    public ResponseEntity<List<Assignment>> getAssignmentsByDueDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Assignment> assignments = assignmentService.getAssignmentsByDueDateRange(startDate, endDate);
        return ResponseEntity.ok(assignments);
    }
    
    @PostMapping
    public ResponseEntity<?> createAssignment(@Valid @RequestBody Assignment assignment) {
        try {
            Assignment createdAssignment = assignmentService.createAssignment(assignment);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAssignment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long id, @Valid @RequestBody Assignment assignmentDetails) {
        try {
            Assignment updatedAssignment = assignmentService.updateAssignment(id, assignmentDetails);
            return ResponseEntity.ok(updatedAssignment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteAssignment(id);
            return ResponseEntity.ok().body(Map.of("message", "Assignment deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateAssignment(@PathVariable Long id) {
        try {
            Assignment deactivatedAssignment = assignmentService.deactivateAssignment(id);
            return ResponseEntity.ok(Map.of(
                "message", "Assignment deactivated successfully",
                "assignment", deactivatedAssignment
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateAssignment(@PathVariable Long id) {
        try {
            Assignment activatedAssignment = assignmentService.activateAssignment(id);
            return ResponseEntity.ok(Map.of(
                "message", "Assignment activated successfully",
                "assignment", activatedAssignment
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
