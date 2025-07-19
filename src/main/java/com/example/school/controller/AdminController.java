package com.example.school.controller;

import com.example.school.model.*;
import com.example.school.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private ComingSoonFeatureService featureService;
    
    @Autowired
    private NoticeService noticeService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private GradeService gradeService;
    
    // Dashboard overview endpoint
    @GetMapping("/dashboard/{adminId}")
    public ResponseEntity<?> getAdminDashboard(@PathVariable Long adminId) {
        try {
            // Validate admin
            User admin = userService.getUserById(adminId)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            
            if (admin.getRole() != UserRole.ADMIN) {
                throw new RuntimeException("Access denied: Admin role required");
            }
            
            // Gather dashboard statistics
            Map<String, Object> dashboard = new HashMap<>();
            
            // User statistics
            List<User> allUsers = userService.getAllUsers();
            dashboard.put("totalUsers", allUsers.size());
            dashboard.put("totalStudents", userService.getAllStudents().size());
            dashboard.put("totalTeachers", userService.getAllTeachers().size());
            dashboard.put("totalAdmins", userService.getAllAdmins().size());
            
            // Course statistics
            List<Course> allCourses = courseService.getAllCourses();
            dashboard.put("totalCourses", allCourses.size());
            dashboard.put("assignedCourses", allCourses.stream().filter(c -> c.getTeacher() != null).count());
            dashboard.put("unassignedCourses", courseService.getUnassignedCourses().size());
            
            // Coming Soon Features statistics
            dashboard.put("totalFeatures", featureService.getAllFeatures().size());
            dashboard.put("activeFeatures", featureService.getActiveFeatures().size());
            dashboard.put("plannedFeatures", featureService.getFeatureCountByStatus(FeatureStatus.PLANNED));
            dashboard.put("inDevelopmentFeatures", featureService.getFeatureCountByStatus(FeatureStatus.IN_DEVELOPMENT));
            
            // Recent data
            dashboard.put("recentUsers", allUsers.stream().limit(5).toList());
            dashboard.put("recentCourses", allCourses.stream().limit(5).toList());
            dashboard.put("recentFeatures", featureService.getActiveFeatures().stream().limit(5).toList());
            
            return ResponseEntity.ok(dashboard);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // User management endpoints
    @GetMapping("/users/{adminId}")
    public ResponseEntity<?> getAllUsersForAdmin(@PathVariable Long adminId) {
        try {
            validateAdmin(adminId);
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/users/role/{role}/{adminId}")
    public ResponseEntity<?> getUsersByRole(@PathVariable UserRole role, @PathVariable Long adminId) {
        try {
            validateAdmin(adminId);
            List<User> users = userService.getUsersByRole(role);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Course management endpoints
    @GetMapping("/courses/{adminId}")
    public ResponseEntity<?> getAllCoursesForAdmin(@PathVariable Long adminId) {
        try {
            validateAdmin(adminId);
            List<Course> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/courses/unassigned/{adminId}")
    public ResponseEntity<?> getUnassignedCoursesForAdmin(@PathVariable Long adminId) {
        try {
            validateAdmin(adminId);
            List<Course> courses = courseService.getUnassignedCourses();
            return ResponseEntity.ok(courses);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/teachers/available/{adminId}")
    public ResponseEntity<?> getAvailableTeachersForAdmin(@PathVariable Long adminId) {
        try {
            validateAdmin(adminId);
            List<User> teachers = courseService.getAvailableTeachers();
            return ResponseEntity.ok(teachers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Course assignment management
    @PostMapping("/assign-courses/{adminId}")
    public ResponseEntity<?> bulkAssignCourses(@PathVariable Long adminId, 
                                              @RequestBody Map<String, Object> assignmentData) {
        try {
            validateAdmin(adminId);
            
            @SuppressWarnings("unchecked")
            List<Long> courseIds = (List<Long>) assignmentData.get("courseIds");
            Long teacherId = Long.valueOf(assignmentData.get("teacherId").toString());
            
            List<Course> updatedCourses = courseService.assignMultipleCoursesToTeacher(courseIds, teacherId, adminId);
            
            return ResponseEntity.ok(Map.of(
                "message", "Courses assigned successfully",
                "assignedCourses", updatedCourses
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // System statistics endpoint
    @GetMapping("/statistics/{adminId}")
    public ResponseEntity<?> getSystemStatistics(@PathVariable Long adminId) {
        try {
            validateAdmin(adminId);
            
            Map<String, Object> stats = new HashMap<>();
            
            // User statistics by role
            stats.put("usersByRole", Map.of(
                "ADMIN", userService.getAllAdmins().size(),
                "TEACHER", userService.getAllTeachers().size(),
                "STUDENT", userService.getAllStudents().size()
            ));
            
            // Course statistics
            List<Course> allCourses = courseService.getAllCourses();
            stats.put("courseStatistics", Map.of(
                "total", allCourses.size(),
                "assigned", allCourses.stream().filter(c -> c.getTeacher() != null).count(),
                "unassigned", courseService.getUnassignedCourses().size()
            ));
            
            // Feature statistics
            stats.put("featureStatistics", Map.of(
                "total", featureService.getAllFeatures().size(),
                "active", featureService.getActiveFeatures().size(),
                "planned", featureService.getFeatureCountByStatus(FeatureStatus.PLANNED),
                "inDevelopment", featureService.getFeatureCountByStatus(FeatureStatus.IN_DEVELOPMENT),
                "testing", featureService.getFeatureCountByStatus(FeatureStatus.TESTING),
                "ready", featureService.getFeatureCountByStatus(FeatureStatus.READY_FOR_RELEASE)
            ));
            
            return ResponseEntity.ok(stats);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Helper method to validate admin access
    private void validateAdmin(Long adminId) {
        User admin = userService.getUserById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Access denied: Admin role required");
        }
    }
}
