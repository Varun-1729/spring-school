package com.example.school.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions")
public class AssignmentSubmission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;
    
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @Column(name = "submission_text", length = 5000)
    private String submissionText;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "file_name")
    private String fileName;
    
    @NotNull(message = "Submission date is required")
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;
    
    @Column(name = "is_late")
    private Boolean isLate = false;
    
    @Column(name = "teacher_feedback", length = 2000)
    private String teacherFeedback;
    
    @Column(name = "grade")
    private Double grade;
    
    @Column(name = "max_grade")
    private Double maxGrade;
    
    @ManyToOne
    @JoinColumn(name = "graded_by")
    private User gradedBy;
    
    @Column(name = "graded_at")
    private LocalDateTime gradedAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public AssignmentSubmission() {
        this.submittedAt = LocalDateTime.now();
    }
    
    public AssignmentSubmission(Assignment assignment, Student student, String submissionText) {
        this.assignment = assignment;
        this.student = student;
        this.submissionText = submissionText;
        this.submittedAt = LocalDateTime.now();
        
        // Check if submission is late
        if (assignment.getDueDate() != null && LocalDateTime.now().isAfter(assignment.getDueDate())) {
            this.isLate = true;
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Assignment getAssignment() {
        return assignment;
    }
    
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public String getSubmissionText() {
        return submissionText;
    }
    
    public void setSubmissionText(String submissionText) {
        this.submissionText = submissionText;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public SubmissionStatus getStatus() {
        return status;
    }
    
    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
    
    public Boolean getIsLate() {
        return isLate;
    }
    
    public void setIsLate(Boolean isLate) {
        this.isLate = isLate;
    }
    
    public String getTeacherFeedback() {
        return teacherFeedback;
    }
    
    public void setTeacherFeedback(String teacherFeedback) {
        this.teacherFeedback = teacherFeedback;
    }
    
    public Double getGrade() {
        return grade;
    }
    
    public void setGrade(Double grade) {
        this.grade = grade;
    }
    
    public Double getMaxGrade() {
        return maxGrade;
    }
    
    public void setMaxGrade(Double maxGrade) {
        this.maxGrade = maxGrade;
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
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public boolean isGraded() {
        return grade != null && gradedBy != null;
    }
    
    public double getGradePercentage() {
        if (grade != null && maxGrade != null && maxGrade > 0) {
            return (grade / maxGrade) * 100.0;
        }
        return 0.0;
    }
    
    @Override
    public String toString() {
        return "AssignmentSubmission{" +
                "id=" + id +
                ", assignment=" + (assignment != null ? assignment.getTitle() : null) +
                ", student=" + (student != null ? student.getStudentId() : null) +
                ", submittedAt=" + submittedAt +
                ", status=" + status +
                ", isLate=" + isLate +
                ", grade=" + grade +
                '}';
    }
}
