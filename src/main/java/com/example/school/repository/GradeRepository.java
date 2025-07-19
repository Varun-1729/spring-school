package com.example.school.repository;

import com.example.school.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    
    List<Grade> findByStudentId(Long studentId);
    
    List<Grade> findByCourseId(Long courseId);
    
    List<Grade> findByStudentIdAndCourseId(Long studentId, Long courseId);
    
    Optional<Grade> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);
    
    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.student.id = :studentId")
    Optional<BigDecimal> findAverageGradeByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT AVG(g.grade) FROM Grade g WHERE g.student.id = :studentId AND g.course.id = :courseId")
    Optional<BigDecimal> findAverageGradeByStudentIdAndCourseId(@Param("studentId") Long studentId, 
                                                               @Param("courseId") Long courseId);
}
