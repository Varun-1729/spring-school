package com.example.school.service;

import com.example.school.model.Assignment;
import com.example.school.model.Course;
import com.example.school.model.User;
import com.example.school.model.UserRole;
import com.example.school.repository.AssignmentRepository;
import com.example.school.repository.CourseRepository;
import com.example.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {
    
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
    
    public List<Assignment> getAllActiveAssignments() {
        return assignmentRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }
    
    public Optional<Assignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }
    
    public List<Assignment> getAssignmentsByCourseId(Long courseId) {
        return assignmentRepository.findByCourseId(courseId);
    }
    
    public List<Assignment> getActiveAssignmentsByCourseId(Long courseId) {
        return assignmentRepository.findActiveByCourseId(courseId);
    }
    
    public List<Assignment> getAssignmentsByCreator(Long createdById) {
        return assignmentRepository.findByCreatedById(createdById);
    }
    
    public List<Assignment> getAssignmentsByDueDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return assignmentRepository.findByDueDateBetween(startDate, endDate);
    }
    
    public Assignment createAssignment(Assignment assignment) {
        // Validate that the course exists
        Course course = courseRepository.findById(assignment.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + assignment.getCourse().getId()));
        
        // Validate that the creator exists and has TEACHER role
        User creator = userRepository.findById(assignment.getCreatedBy().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + assignment.getCreatedBy().getId()));
        
        if (creator.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("Only TEACHER users can create assignments");
        }
        
        assignment.setCourse(course);
        assignment.setCreatedBy(creator);
        return assignmentRepository.save(assignment);
    }
    
    public Assignment updateAssignment(Long id, Assignment assignmentDetails) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        
        assignment.setTitle(assignmentDetails.getTitle());
        assignment.setDescription(assignmentDetails.getDescription());
        assignment.setDueDate(assignmentDetails.getDueDate());
        assignment.setMaxMarks(assignmentDetails.getMaxMarks());
        assignment.setIsActive(assignmentDetails.getIsActive());
        
        // Update course if provided
        if (assignmentDetails.getCourse() != null) {
            Course course = courseRepository.findById(assignmentDetails.getCourse().getId())
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + assignmentDetails.getCourse().getId()));
            assignment.setCourse(course);
        }
        
        return assignmentRepository.save(assignment);
    }
    
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        assignmentRepository.delete(assignment);
    }
    
    public Assignment deactivateAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        
        assignment.setIsActive(false);
        return assignmentRepository.save(assignment);
    }
    
    public Assignment activateAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        
        assignment.setIsActive(true);
        return assignmentRepository.save(assignment);
    }
}
