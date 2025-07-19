package com.example.school.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "coming_soon_features")
public class ComingSoonFeature {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Feature title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    @Column(nullable = false)
    private String title;
    
    @NotBlank(message = "Feature description is required")
    @Column(nullable = false, length = 2000)
    private String description;
    
    @Column(name = "icon_class")
    private String iconClass; // CSS class for icon (e.g., "fas fa-calendar", "fas fa-book")
    
    @Column(name = "expected_release_date")
    private LocalDateTime expectedReleaseDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private FeaturePriority priority = FeaturePriority.MEDIUM;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FeatureStatus status = FeatureStatus.PLANNED;
    
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    // Constructors
    public ComingSoonFeature() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public ComingSoonFeature(String title, String description, User createdBy) {
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    public LocalDateTime getExpectedReleaseDate() {
        return expectedReleaseDate;
    }
    
    public void setExpectedReleaseDate(LocalDateTime expectedReleaseDate) {
        this.expectedReleaseDate = expectedReleaseDate;
    }
    
    public FeaturePriority getPriority() {
        return priority;
    }
    
    public void setPriority(FeaturePriority priority) {
        this.priority = priority;
    }
    
    public FeatureStatus getStatus() {
        return status;
    }
    
    public void setStatus(FeatureStatus status) {
        this.status = status;
    }
    
    public User getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "ComingSoonFeature{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", expectedReleaseDate=" + expectedReleaseDate +
                ", isActive=" + isActive +
                '}';
    }
}
