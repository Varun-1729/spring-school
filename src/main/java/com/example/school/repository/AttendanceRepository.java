package com.example.school.repository;

import com.example.school.model.Attendance;
import com.example.school.model.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    // Find attendance by student
    List<Attendance> findByStudentId(Long studentId);
    
    // Find attendance by course
    List<Attendance> findByCourseId(Long courseId);
    
    // Find attendance by teacher
    List<Attendance> findByTeacherId(Long teacherId);
    
    // Find attendance by date
    List<Attendance> findByAttendanceDate(LocalDate date);
    
    // Find attendance by student and course
    List<Attendance> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    // Find attendance by student and date
    List<Attendance> findByStudentIdAndAttendanceDate(Long studentId, LocalDate date);
    
    // Find attendance by course and date
    List<Attendance> findByCourseIdAndAttendanceDate(Long courseId, LocalDate date);
    
    // Find attendance by student, course and date (unique record)
    Optional<Attendance> findByStudentIdAndCourseIdAndAttendanceDate(Long studentId, Long courseId, LocalDate date);
    
    // Find attendance by date range
    List<Attendance> findByAttendanceDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find attendance by student and date range
    List<Attendance> findByStudentIdAndAttendanceDateBetween(Long studentId, LocalDate startDate, LocalDate endDate);
    
    // Find attendance by course and date range
    List<Attendance> findByCourseIdAndAttendanceDateBetween(Long courseId, LocalDate startDate, LocalDate endDate);
    
    // Find attendance by status
    List<Attendance> findByStatus(AttendanceStatus status);
    
    // Custom queries for statistics
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.status = :status")
    Long countByStudentIdAndStatus(@Param("studentId") Long studentId, @Param("status") AttendanceStatus status);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.course.id = :courseId AND a.status = :status")
    Long countByCourseIdAndStatus(@Param("courseId") Long courseId, @Param("status") AttendanceStatus status);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.course.id = :courseId AND a.status = :status")
    Long countByStudentIdAndCourseIdAndStatus(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("status") AttendanceStatus status);
    
    // Attendance percentage calculation
    @Query("SELECT (COUNT(a) * 100.0 / (SELECT COUNT(a2) FROM Attendance a2 WHERE a2.student.id = :studentId AND a2.course.id = :courseId)) " +
           "FROM Attendance a WHERE a.student.id = :studentId AND a.course.id = :courseId AND a.status = 'PRESENT'")
    Double calculateAttendancePercentage(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
    
    // Get attendance summary for a student
    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.student.id = :studentId GROUP BY a.status")
    List<Object[]> getAttendanceSummaryByStudent(@Param("studentId") Long studentId);
    
    // Get attendance summary for a course
    @Query("SELECT a.status, COUNT(a) FROM Attendance a WHERE a.course.id = :courseId GROUP BY a.status")
    List<Object[]> getAttendanceSummaryByCourse(@Param("courseId") Long courseId);
    
    // Get students with low attendance (less than specified percentage)
    @Query("SELECT a.student.id, (COUNT(CASE WHEN a.status = 'PRESENT' THEN 1 END) * 100.0 / COUNT(a)) as percentage " +
           "FROM Attendance a WHERE a.course.id = :courseId " +
           "GROUP BY a.student.id " +
           "HAVING (COUNT(CASE WHEN a.status = 'PRESENT' THEN 1 END) * 100.0 / COUNT(a)) < :minPercentage")
    List<Object[]> getStudentsWithLowAttendance(@Param("courseId") Long courseId, @Param("minPercentage") Double minPercentage);
}
