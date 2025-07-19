package com.example.school.service;

import com.example.school.model.*;
import com.example.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GradeService {
    
    @Autowired
    private GradeRepository gradeRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }
    
    public Optional<Grade> getGradeById(Long id) {
        return gradeRepository.findById(id);
    }
    
    public List<Grade> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
    
    public List<Grade> getGradesByCourseId(Long courseId) {
        return gradeRepository.findByCourseId(courseId);
    }
    
    public List<Grade> getGradesByStudentAndCourse(Long studentId, Long courseId) {
        return gradeRepository.findByStudentIdAndCourseId(studentId, courseId);
    }
    
    public Optional<Grade> getGradeByStudentAndAssignment(Long studentId, Long assignmentId) {
        return gradeRepository.findByStudentIdAndAssignmentId(studentId, assignmentId);
    }
    
    public Optional<BigDecimal> getAverageGradeByStudent(Long studentId) {
        return gradeRepository.findAverageGradeByStudentId(studentId);
    }
    
    public Optional<BigDecimal> getAverageGradeByStudentAndCourse(Long studentId, Long courseId) {
        return gradeRepository.findAverageGradeByStudentIdAndCourseId(studentId, courseId);
    }
    
    public Grade assignGrade(Grade grade) {
        // Validate student exists
        Student student = studentRepository.findById(grade.getStudent().getId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + grade.getStudent().getId()));
        
        // Validate course exists
        Course course = courseRepository.findById(grade.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + grade.getCourse().getId()));
        
        // Validate grader exists and has TEACHER role
        User grader = userRepository.findById(grade.getGradedBy().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + grade.getGradedBy().getId()));
        
        if (grader.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("Only TEACHER users can assign grades");
        }
        
        // Validate assignment if provided
        if (grade.getAssignment() != null) {
            Assignment assignment = assignmentRepository.findById(grade.getAssignment().getId())
                    .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + grade.getAssignment().getId()));
            grade.setAssignment(assignment);
        }
        
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setGradedBy(grader);
        
        return gradeRepository.save(grade);
    }
    
    public Grade updateGrade(Long id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + id));
        
        grade.setGrade(gradeDetails.getGrade());
        grade.setMaxGrade(gradeDetails.getMaxGrade());
        grade.setComments(gradeDetails.getComments());
        
        return gradeRepository.save(grade);
    }
    
    public void deleteGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + id));
        gradeRepository.delete(grade);
    }
}
