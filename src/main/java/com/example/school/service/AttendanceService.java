package com.example.school.service;

import com.example.school.model.*;
import com.example.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Basic CRUD operations
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
    
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }
    
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
    
    public List<Attendance> getAttendanceByCourse(Long courseId) {
        return attendanceRepository.findByCourseId(courseId);
    }
    
    public List<Attendance> getAttendanceByTeacher(Long teacherId) {
        return attendanceRepository.findByTeacherId(teacherId);
    }
    
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date);
    }
    
    // Mark attendance for a single student
    public Attendance markAttendance(Long studentId, Long courseId, LocalDate date, AttendanceStatus status, String remarks, Long teacherId) {
        // Validate teacher
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
        
        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("Only TEACHER users can mark attendance");
        }
        
        // Validate student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Validate course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        // Verify teacher teaches this course
        if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("Teacher can only mark attendance for their assigned courses");
        }
        
        // Check if attendance already exists for this student, course, and date
        Optional<Attendance> existingAttendance = attendanceRepository.findByStudentIdAndCourseIdAndAttendanceDate(studentId, courseId, date);
        
        if (existingAttendance.isPresent()) {
            // Update existing attendance
            Attendance attendance = existingAttendance.get();
            attendance.setStatus(status);
            attendance.setRemarks(remarks);
            attendance.setMarkedBy(teacher);
            return attendanceRepository.save(attendance);
        } else {
            // Create new attendance record
            Attendance attendance = new Attendance(student, course, teacher, date, status);
            attendance.setRemarks(remarks);
            return attendanceRepository.save(attendance);
        }
    }
    
    // Mark attendance for multiple students (bulk operation)
    public List<Attendance> markBulkAttendance(Long courseId, LocalDate date, Map<Long, AttendanceStatus> studentAttendance, Long teacherId) {
        // Validate teacher and course
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
        
        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("Only TEACHER users can mark attendance");
        }
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("Teacher can only mark attendance for their assigned courses");
        }
        
        List<Attendance> attendanceRecords = new ArrayList<>();
        
        for (Map.Entry<Long, AttendanceStatus> entry : studentAttendance.entrySet()) {
            Long studentId = entry.getKey();
            AttendanceStatus status = entry.getValue();
            
            try {
                Attendance attendance = markAttendance(studentId, courseId, date, status, null, teacherId);
                attendanceRecords.add(attendance);
            } catch (RuntimeException e) {
                // Log error but continue with other students
                System.err.println("Error marking attendance for student " + studentId + ": " + e.getMessage());
            }
        }
        
        return attendanceRecords;
    }
    
    // Get attendance statistics
    public Map<String, Object> getAttendanceStatistics(Long studentId, Long courseId) {
        Map<String, Object> stats = new HashMap<>();
        
        if (studentId != null && courseId != null) {
            // Student-course specific statistics
            Long presentCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.PRESENT);
            Long absentCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.ABSENT);
            Long lateCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.LATE);
            Long excusedCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.EXCUSED);
            
            Long totalClasses = presentCount + absentCount + lateCount + excusedCount;
            Double attendancePercentage = totalClasses > 0 ? (presentCount * 100.0 / totalClasses) : 0.0;
            
            stats.put("presentCount", presentCount);
            stats.put("absentCount", absentCount);
            stats.put("lateCount", lateCount);
            stats.put("excusedCount", excusedCount);
            stats.put("totalClasses", totalClasses);
            stats.put("attendancePercentage", Math.round(attendancePercentage * 100.0) / 100.0);
            
        } else if (studentId != null) {
            // Student overall statistics
            List<Object[]> summary = attendanceRepository.getAttendanceSummaryByStudent(studentId);
            for (Object[] row : summary) {
                AttendanceStatus status = (AttendanceStatus) row[0];
                Long count = (Long) row[1];
                stats.put(status.toString().toLowerCase() + "Count", count);
            }
            
        } else if (courseId != null) {
            // Course overall statistics
            List<Object[]> summary = attendanceRepository.getAttendanceSummaryByCourse(courseId);
            for (Object[] row : summary) {
                AttendanceStatus status = (AttendanceStatus) row[0];
                Long count = (Long) row[1];
                stats.put(status.toString().toLowerCase() + "Count", count);
            }
        }
        
        return stats;
    }
    
    // Get students with low attendance
    public List<Map<String, Object>> getStudentsWithLowAttendance(Long courseId, Double minPercentage) {
        List<Object[]> results = attendanceRepository.getStudentsWithLowAttendance(courseId, minPercentage);
        
        return results.stream().map(row -> {
            Map<String, Object> studentData = new HashMap<>();
            Long studentId = (Long) row[0];
            Double percentage = (Double) row[1];
            
            Optional<Student> student = studentRepository.findById(studentId);
            if (student.isPresent()) {
                studentData.put("studentId", studentId);
                studentData.put("studentName", student.get().getUser().getName());
                studentData.put("attendancePercentage", Math.round(percentage * 100.0) / 100.0);
            }
            
            return studentData;
        }).collect(Collectors.toList());
    }
    
    // Get attendance report for date range
    public List<Attendance> getAttendanceReport(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate) {
        if (studentId != null && courseId != null) {
            return attendanceRepository.findByStudentIdAndAttendanceDateBetween(studentId, startDate, endDate)
                    .stream()
                    .filter(a -> a.getCourse().getId().equals(courseId))
                    .collect(Collectors.toList());
        } else if (studentId != null) {
            return attendanceRepository.findByStudentIdAndAttendanceDateBetween(studentId, startDate, endDate);
        } else if (courseId != null) {
            return attendanceRepository.findByCourseIdAndAttendanceDateBetween(courseId, startDate, endDate);
        } else {
            return attendanceRepository.findByAttendanceDateBetween(startDate, endDate);
        }
    }
    
    // Delete attendance record
    public void deleteAttendance(Long id, Long teacherId) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found with id: " + id));
        
        // Verify teacher can delete this record
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
        
        if (teacher.getRole() != UserRole.TEACHER && teacher.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only TEACHER or ADMIN users can delete attendance records");
        }
        
        if (teacher.getRole() == UserRole.TEACHER) {
            if (!attendance.getCourse().getTeacher().getId().equals(teacherId)) {
                throw new RuntimeException("Teacher can only delete attendance records for their assigned courses");
            }
        }
        
        attendanceRepository.delete(attendance);
    }
}
