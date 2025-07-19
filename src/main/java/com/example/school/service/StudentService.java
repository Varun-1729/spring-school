package com.example.school.service;

import com.example.school.model.Course;
import com.example.school.model.Student;
import com.example.school.model.User;
import com.example.school.model.UserRole;
import com.example.school.repository.CourseRepository;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Optional<Student> getStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }
    
    public Optional<Student> getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }
    
    public List<Student> getStudentsByGradeLevel(String gradeLevel) {
        return studentRepository.findByGradeLevel(gradeLevel);
    }
    
    public List<Student> getStudentsByCourseId(Long courseId) {
        return studentRepository.findStudentsByCourseId(courseId);
    }
    
    public Student createStudent(Student student) {
        // Validate that the user exists and has STUDENT role
        User user = userRepository.findById(student.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + student.getUser().getId()));
        
        if (user.getRole() != UserRole.STUDENT) {
            throw new RuntimeException("User must have STUDENT role to create a student profile");
        }
        
        if (studentRepository.existsByStudentId(student.getStudentId())) {
            throw new RuntimeException("Student with ID " + student.getStudentId() + " already exists");
        }
        
        student.setUser(user);
        return studentRepository.save(student);
    }
    
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        
        student.setStudentId(studentDetails.getStudentId());
        student.setDateOfBirth(studentDetails.getDateOfBirth());
        student.setGradeLevel(studentDetails.getGradeLevel());
        student.setParentContact(studentDetails.getParentContact());
        student.setAddress(studentDetails.getAddress());
        
        return studentRepository.save(student);
    }
    
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        studentRepository.delete(student);
    }
    
    public boolean existsByStudentId(String studentId) {
        return studentRepository.existsByStudentId(studentId);
    }

    // Teacher-specific student management methods
    public List<Student> getStudentsByTeacherId(Long teacherId) {
        // Validate that the user is a teacher
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("User must have TEACHER role to access student data");
        }

        // Get all courses taught by this teacher
        List<Course> teacherCourses = courseRepository.findByTeacherId(teacherId);

        // Get all students enrolled in these courses
        return teacherCourses.stream()
                .flatMap(course -> course.getStudents().stream())
                .distinct()
                .toList();
    }

    public Student updateStudentByTeacher(Long studentId, Student studentDetails, Long teacherId) {
        // Validate that the user is a teacher
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("Only TEACHER users can update student information");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        // Verify that the teacher teaches this student (student is in one of teacher's courses)
        List<Course> teacherCourses = courseRepository.findByTeacherId(teacherId);
        boolean teachesStudent = teacherCourses.stream()
                .anyMatch(course -> course.getStudents().contains(student));

        if (!teachesStudent) {
            throw new RuntimeException("Teacher can only update students in their assigned courses");
        }

        // Update allowed fields (teachers can update academic info but not personal details)
        student.setGradeLevel(studentDetails.getGradeLevel());
        student.setParentContact(studentDetails.getParentContact());

        return studentRepository.save(student);
    }

    public List<Student> getStudentsInTeacherCourse(Long teacherId, Long courseId) {
        // Validate that the user is a teacher
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("User must have TEACHER role to access student data");
        }

        // Verify that the teacher teaches this course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        if (course.getTeacher() == null || !course.getTeacher().getId().equals(teacherId)) {
            throw new RuntimeException("Teacher can only access students from their assigned courses");
        }

        return studentRepository.findStudentsByCourseId(courseId);
    }

    // Admin-only student management methods
    public Student updateStudentByAdmin(Long studentId, Student studentDetails, Long adminId) {
        // Validate that the user is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can perform full student updates");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        // Admin can update all fields
        student.setStudentId(studentDetails.getStudentId());
        student.setDateOfBirth(studentDetails.getDateOfBirth());
        student.setGradeLevel(studentDetails.getGradeLevel());
        student.setParentContact(studentDetails.getParentContact());
        student.setAddress(studentDetails.getAddress());

        return studentRepository.save(student);
    }

    public void deleteStudentByAdmin(Long studentId, Long adminId) {
        // Validate that the user is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can delete students");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

        studentRepository.delete(student);
    }
}
