package com.example.school.controller;

import com.example.school.model.ComingSoonFeature;
import com.example.school.model.FeaturePriority;
import com.example.school.model.FeatureStatus;
import com.example.school.service.ComingSoonFeatureService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/coming-soon")
@CrossOrigin(origins = "*")
public class ComingSoonFeatureController {
    
    @Autowired
    private ComingSoonFeatureService featureService;
    
    // Public endpoints (accessible by all users)
    @GetMapping
    public ResponseEntity<List<ComingSoonFeature>> getAllFeatures() {
        List<ComingSoonFeature> features = featureService.getAllFeatures();
        return ResponseEntity.ok(features);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ComingSoonFeature>> getActiveFeatures() {
        List<ComingSoonFeature> features = featureService.getActiveFeatures();
        return ResponseEntity.ok(features);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ComingSoonFeature> getFeatureById(@PathVariable Long id) {
        Optional<ComingSoonFeature> feature = featureService.getFeatureById(id);
        return feature.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<ComingSoonFeature>> getFeaturesByPriority(@PathVariable FeaturePriority priority) {
        List<ComingSoonFeature> features = featureService.getFeaturesByPriority(priority);
        return ResponseEntity.ok(features);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ComingSoonFeature>> getFeaturesByStatus(@PathVariable FeatureStatus status) {
        List<ComingSoonFeature> features = featureService.getFeaturesByStatus(status);
        return ResponseEntity.ok(features);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ComingSoonFeature>> searchFeatures(@RequestParam String keyword) {
        List<ComingSoonFeature> features = featureService.searchFeatures(keyword);
        return ResponseEntity.ok(features);
    }
    
    @GetMapping("/count/{status}")
    public ResponseEntity<Map<String, Long>> getFeatureCountByStatus(@PathVariable FeatureStatus status) {
        Long count = featureService.getFeatureCountByStatus(status);
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    // Admin-only endpoints
    @PostMapping("/admin/{adminId}")
    public ResponseEntity<?> createFeature(@PathVariable Long adminId, @Valid @RequestBody ComingSoonFeature feature) {
        try {
            ComingSoonFeature createdFeature = featureService.createFeature(feature, adminId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFeature);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/admin/{adminId}")
    public ResponseEntity<?> updateFeature(@PathVariable Long id, @PathVariable Long adminId, 
                                         @Valid @RequestBody ComingSoonFeature featureDetails) {
        try {
            ComingSoonFeature updatedFeature = featureService.updateFeature(id, featureDetails, adminId);
            return ResponseEntity.ok(updatedFeature);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}/admin/{adminId}")
    public ResponseEntity<?> deleteFeature(@PathVariable Long id, @PathVariable Long adminId) {
        try {
            featureService.deleteFeature(id, adminId);
            return ResponseEntity.ok().body(Map.of("message", "Feature deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/toggle/admin/{adminId}")
    public ResponseEntity<?> toggleFeatureStatus(@PathVariable Long id, @PathVariable Long adminId) {
        try {
            ComingSoonFeature updatedFeature = featureService.toggleFeatureStatus(id, adminId);
            return ResponseEntity.ok(Map.of(
                "message", "Feature status toggled successfully",
                "feature", updatedFeature
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/reorder/admin/{adminId}")
    public ResponseEntity<?> reorderFeatures(@PathVariable Long adminId, @RequestBody List<Long> featureIds) {
        try {
            List<ComingSoonFeature> reorderedFeatures = featureService.reorderFeatures(featureIds, adminId);
            return ResponseEntity.ok(Map.of(
                "message", "Features reordered successfully",
                "features", reorderedFeatures
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Utility endpoints
    @GetMapping("/enums/priorities")
    public ResponseEntity<FeaturePriority[]> getFeaturePriorities() {
        return ResponseEntity.ok(FeaturePriority.values());
    }
    
    @GetMapping("/enums/statuses")
    public ResponseEntity<FeatureStatus[]> getFeatureStatuses() {
        return ResponseEntity.ok(FeatureStatus.values());
    }
}
