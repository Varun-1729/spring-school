package com.example.school.repository;

import com.example.school.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    List<Assignment> findByIsActiveTrue();
    
    List<Assignment> findByCourseId(Long courseId);
    
    List<Assignment> findByCreatedById(Long createdById);
    
    List<Assignment> findByIsActiveTrueOrderByCreatedAtDesc();
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.isActive = true")
    List<Assignment> findActiveByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT a FROM Assignment a WHERE a.dueDate BETWEEN :startDate AND :endDate AND a.isActive = true")
    List<Assignment> findByDueDateBetween(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
}
