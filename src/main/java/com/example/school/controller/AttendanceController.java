package com.example.school.controller;

import com.example.school.model.Attendance;
import com.example.school.model.AttendanceStatus;
import com.example.school.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    // Get all attendance records
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        List<Attendance> attendance = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendance);
    }
    
    // Get attendance by ID
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        Optional<Attendance> attendance = attendanceService.getAttendanceById(id);
        return attendance.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    // Get attendance by student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<Attendance> attendance = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(attendance);
    }
    
    // Get attendance by course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Attendance>> getAttendanceByCourse(@PathVariable Long courseId) {
        List<Attendance> attendance = attendanceService.getAttendanceByCourse(courseId);
        return ResponseEntity.ok(attendance);
    }
    
    // Get attendance by teacher
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Attendance>> getAttendanceByTeacher(@PathVariable Long teacherId) {
        List<Attendance> attendance = attendanceService.getAttendanceByTeacher(teacherId);
        return ResponseEntity.ok(attendance);
    }
    
    // Get attendance by date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Attendance> attendance = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendance);
    }
    
    // Mark attendance for single student
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestBody Map<String, Object> attendanceData) {
        try {
            Long studentId = Long.valueOf(attendanceData.get("studentId").toString());
            Long courseId = Long.valueOf(attendanceData.get("courseId").toString());
            LocalDate date = LocalDate.parse(attendanceData.get("date").toString());
            AttendanceStatus status = AttendanceStatus.valueOf(attendanceData.get("status").toString());
            String remarks = attendanceData.get("remarks") != null ? attendanceData.get("remarks").toString() : null;
            Long teacherId = Long.valueOf(attendanceData.get("teacherId").toString());
            
            Attendance attendance = attendanceService.markAttendance(studentId, courseId, date, status, remarks, teacherId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Attendance marked successfully",
                "attendance", attendance
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Mark bulk attendance
    @PostMapping("/mark-bulk")
    public ResponseEntity<?> markBulkAttendance(@RequestBody Map<String, Object> bulkData) {
        try {
            Long courseId = Long.valueOf(bulkData.get("courseId").toString());
            LocalDate date = LocalDate.parse(bulkData.get("date").toString());
            Long teacherId = Long.valueOf(bulkData.get("teacherId").toString());
            
            @SuppressWarnings("unchecked")
            Map<String, String> studentAttendanceMap = (Map<String, String>) bulkData.get("studentAttendance");
            
            Map<Long, AttendanceStatus> studentAttendance = new HashMap<>();
            for (Map.Entry<String, String> entry : studentAttendanceMap.entrySet()) {
                Long studentId = Long.valueOf(entry.getKey());
                AttendanceStatus status = AttendanceStatus.valueOf(entry.getValue());
                studentAttendance.put(studentId, status);
            }
            
            List<Attendance> attendanceRecords = attendanceService.markBulkAttendance(courseId, date, studentAttendance, teacherId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Bulk attendance marked successfully",
                "attendanceRecords", attendanceRecords,
                "totalRecords", attendanceRecords.size()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get attendance statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getAttendanceStatistics(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId) {
        Map<String, Object> statistics = attendanceService.getAttendanceStatistics(studentId, courseId);
        return ResponseEntity.ok(statistics);
    }
    
    // Get students with low attendance
    @GetMapping("/low-attendance/course/{courseId}")
    public ResponseEntity<List<Map<String, Object>>> getStudentsWithLowAttendance(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "75.0") Double minPercentage) {
        List<Map<String, Object>> students = attendanceService.getStudentsWithLowAttendance(courseId, minPercentage);
        return ResponseEntity.ok(students);
    }
    
    // Get attendance report for date range
    @GetMapping("/report")
    public ResponseEntity<List<Attendance>> getAttendanceReport(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Attendance> report = attendanceService.getAttendanceReport(studentId, courseId, startDate, endDate);
        return ResponseEntity.ok(report);
    }
    
    // Delete attendance record
    @DeleteMapping("/{id}/teacher/{teacherId}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long id, @PathVariable Long teacherId) {
        try {
            attendanceService.deleteAttendance(id, teacherId);
            return ResponseEntity.ok().body(Map.of("message", "Attendance record deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Get attendance status enum values
    @GetMapping("/statuses")
    public ResponseEntity<AttendanceStatus[]> getAttendanceStatuses() {
        return ResponseEntity.ok(AttendanceStatus.values());
    }
    
    // Get attendance for specific student and course
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<Attendance>> getStudentCourseAttendance(
            @PathVariable Long studentId, 
            @PathVariable Long courseId) {
        List<Attendance> attendance = attendanceService.getAttendanceByStudent(studentId)
                .stream()
                .filter(a -> a.getCourse().getId().equals(courseId))
                .toList();
        return ResponseEntity.ok(attendance);
    }
    
    // Get today's attendance for a course
    @GetMapping("/today/course/{courseId}")
    public ResponseEntity<List<Attendance>> getTodayAttendanceForCourse(@PathVariable Long courseId) {
        List<Attendance> attendance = attendanceService.getAttendanceByDate(LocalDate.now())
                .stream()
                .filter(a -> a.getCourse().getId().equals(courseId))
                .toList();
        return ResponseEntity.ok(attendance);
    }
}
