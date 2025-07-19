package com.example.school.service;

import com.example.school.model.ComingSoonFeature;
import com.example.school.model.FeaturePriority;
import com.example.school.model.FeatureStatus;
import com.example.school.model.User;
import com.example.school.model.UserRole;
import com.example.school.repository.ComingSoonFeatureRepository;
import com.example.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComingSoonFeatureService {
    
    @Autowired
    private ComingSoonFeatureRepository featureRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<ComingSoonFeature> getAllFeatures() {
        return featureRepository.findAll();
    }
    
    public List<ComingSoonFeature> getActiveFeatures() {
        return featureRepository.findActiveFeaturesOrderByPriorityAndDisplayOrder();
    }
    
    public Optional<ComingSoonFeature> getFeatureById(Long id) {
        return featureRepository.findById(id);
    }
    
    public List<ComingSoonFeature> getFeaturesByPriority(FeaturePriority priority) {
        return featureRepository.findByPriorityAndIsActiveTrue(priority);
    }
    
    public List<ComingSoonFeature> getFeaturesByStatus(FeatureStatus status) {
        return featureRepository.findByStatusAndIsActiveTrue(status);
    }
    
    public List<ComingSoonFeature> searchFeatures(String keyword) {
        return featureRepository.searchByKeyword(keyword);
    }
    
    public Long getFeatureCountByStatus(FeatureStatus status) {
        return featureRepository.countByStatusAndIsActiveTrue(status);
    }
    
    // Admin-only operations
    public ComingSoonFeature createFeature(ComingSoonFeature feature, Long adminId) {
        // Validate that the creator is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can create coming soon features");
        }
        
        feature.setCreatedBy(admin);
        return featureRepository.save(feature);
    }
    
    public ComingSoonFeature updateFeature(Long id, ComingSoonFeature featureDetails, Long adminId) {
        // Validate that the updater is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can update coming soon features");
        }
        
        ComingSoonFeature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id: " + id));
        
        feature.setTitle(featureDetails.getTitle());
        feature.setDescription(featureDetails.getDescription());
        feature.setIconClass(featureDetails.getIconClass());
        feature.setExpectedReleaseDate(featureDetails.getExpectedReleaseDate());
        feature.setPriority(featureDetails.getPriority());
        feature.setStatus(featureDetails.getStatus());
        feature.setDisplayOrder(featureDetails.getDisplayOrder());
        feature.setIsActive(featureDetails.getIsActive());
        
        return featureRepository.save(feature);
    }
    
    public void deleteFeature(Long id, Long adminId) {
        // Validate that the deleter is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can delete coming soon features");
        }
        
        ComingSoonFeature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id: " + id));
        
        featureRepository.delete(feature);
    }
    
    public ComingSoonFeature toggleFeatureStatus(Long id, Long adminId) {
        // Validate that the updater is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can toggle feature status");
        }
        
        ComingSoonFeature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id: " + id));
        
        feature.setIsActive(!feature.getIsActive());
        return featureRepository.save(feature);
    }
    
    public List<ComingSoonFeature> reorderFeatures(List<Long> featureIds, Long adminId) {
        // Validate that the updater is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can reorder features");
        }
        
        for (int i = 0; i < featureIds.size(); i++) {
            Long featureId = featureIds.get(i);
            ComingSoonFeature feature = featureRepository.findById(featureId)
                    .orElseThrow(() -> new RuntimeException("Feature not found with id: " + featureId));
            
            feature.setDisplayOrder(i);
            featureRepository.save(feature);
        }
        
        return getActiveFeatures();
    }
}
