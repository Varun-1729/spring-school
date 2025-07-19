package com.example.school.repository;

import com.example.school.model.ComingSoonFeature;
import com.example.school.model.FeaturePriority;
import com.example.school.model.FeatureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComingSoonFeatureRepository extends JpaRepository<ComingSoonFeature, Long> {
    
    List<ComingSoonFeature> findByIsActiveTrue();
    
    List<ComingSoonFeature> findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc();
    
    List<ComingSoonFeature> findByPriorityAndIsActiveTrue(FeaturePriority priority);
    
    List<ComingSoonFeature> findByStatusAndIsActiveTrue(FeatureStatus status);
    
    List<ComingSoonFeature> findByCreatedById(Long createdById);
    
    @Query("SELECT f FROM ComingSoonFeature f WHERE f.isActive = true ORDER BY f.priority DESC, f.displayOrder ASC, f.createdAt DESC")
    List<ComingSoonFeature> findActiveFeaturesOrderByPriorityAndDisplayOrder();
    
    @Query("SELECT f FROM ComingSoonFeature f WHERE f.title LIKE %:keyword% OR f.description LIKE %:keyword%")
    List<ComingSoonFeature> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT COUNT(f) FROM ComingSoonFeature f WHERE f.status = :status AND f.isActive = true")
    Long countByStatusAndIsActiveTrue(@Param("status") FeatureStatus status);
}
