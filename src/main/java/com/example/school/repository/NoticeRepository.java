package com.example.school.repository;

import com.example.school.model.Notice;
import com.example.school.model.NoticePriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    List<Notice> findByIsActiveTrue();
    
    List<Notice> findByIsActiveTrueOrderByCreatedAtDesc();
    
    List<Notice> findByPriorityAndIsActiveTrue(NoticePriority priority);
    
    List<Notice> findByCreatedById(Long createdById);
    
    @Query("SELECT n FROM Notice n WHERE n.isActive = true ORDER BY n.priority DESC, n.createdAt DESC")
    List<Notice> findActiveNoticesOrderByPriorityAndDate();
}
