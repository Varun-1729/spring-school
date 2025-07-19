package com.example.school.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
public class Grade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;
    
    @NotNull(message = "Grade is required")
    @DecimalMin(value = "0.0", message = "Grade must be at least 0")
    @DecimalMax(value = "100.0", message = "Grade must not exceed 100")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal grade;
    
    @Column(name = "max_grade", precision = 5, scale = 2)
    private BigDecimal maxGrade = BigDecimal.valueOf(100);
    
    @Column(name = "comments", length = 500)
    private String comments;
    
    @ManyToOne
    @JoinColumn(name = "graded_by", nullable = false)
    private User gradedBy;
    
    @Column(name = "graded_at", nullable = false)
    private LocalDateTime gradedAt;
    
    // Constructors
    public Grade() {
        this.gradedAt = LocalDateTime.now();
    }
    
    public Grade(Student student, Course course, BigDecimal grade, User gradedBy) {
        this.student = student;
        this.course = course;
        this.grade = grade;
        this.gradedBy = gradedBy;
        this.gradedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Course getCourse() {
        return course;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public Assignment getAssignment() {
        return assignment;
    }
    
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
    
    public BigDecimal getGrade() {
        return grade;
    }
    
    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }
    
    public BigDecimal getMaxGrade() {
        return maxGrade;
    }
    
    public void setMaxGrade(BigDecimal maxGrade) {
        this.maxGrade = maxGrade;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public User getGradedBy() {
        return gradedBy;
    }
    
    public void setGradedBy(User gradedBy) {
        this.gradedBy = gradedBy;
    }
    
    public LocalDateTime getGradedAt() {
        return gradedAt;
    }
    
    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }
    
    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", grade=" + grade +
                ", maxGrade=" + maxGrade +
                ", comments='" + comments + '\'' +
                ", gradedAt=" + gradedAt +
                '}';
    }
}
