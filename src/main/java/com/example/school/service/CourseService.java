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
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }
    
    public Optional<Course> getCourseByCourseCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }
    
    public List<Course> getCoursesByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }
    
    public List<Course> getCoursesByStudentId(Long studentId) {
        return courseRepository.findCoursesByStudentId(studentId);
    }
    
    public List<Course> searchCoursesByName(String courseName) {
        return courseRepository.findByCourseNameContainingIgnoreCase(courseName);
    }
    
    public Course createCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Course with code " + course.getCourseCode() + " already exists");
        }
        
        // Validate teacher if provided
        if (course.getTeacher() != null) {
            User teacher = userRepository.findById(course.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + course.getTeacher().getId()));
            
            if (teacher.getRole() != UserRole.TEACHER) {
                throw new RuntimeException("User must have TEACHER role to be assigned to a course");
            }
            course.setTeacher(teacher);
        }
        
        return courseRepository.save(course);
    }
    
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        
        course.setCourseCode(courseDetails.getCourseCode());
        course.setCourseName(courseDetails.getCourseName());
        course.setDescription(courseDetails.getDescription());
        course.setCredits(courseDetails.getCredits());
        
        // Update teacher if provided
        if (courseDetails.getTeacher() != null) {
            User teacher = userRepository.findById(courseDetails.getTeacher().getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + courseDetails.getTeacher().getId()));
            
            if (teacher.getRole() != UserRole.TEACHER) {
                throw new RuntimeException("User must have TEACHER role to be assigned to a course");
            }
            course.setTeacher(teacher);
        }
        
        return courseRepository.save(course);
    }
    
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        courseRepository.delete(course);
    }
    
    public Course assignStudentToCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        course.addStudent(student);
        return courseRepository.save(course);
    }
    
    public Course removeStudentFromCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        course.removeStudent(student);
        return courseRepository.save(course);
    }
    
    public boolean existsByCourseCode(String courseCode) {
        return courseRepository.existsByCourseCode(courseCode);
    }

    // Admin-only course assignment methods
    public Course assignTeacherToCourse(Long courseId, Long teacherId, Long adminId) {
        // Validate that the assigner is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can assign teachers to courses");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("User must have TEACHER role to be assigned to a course");
        }

        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public Course removeTeacherFromCourse(Long courseId, Long adminId) {
        // Validate that the remover is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can remove teachers from courses");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        course.setTeacher(null);
        return courseRepository.save(course);
    }

    public List<Course> getUnassignedCourses() {
        return courseRepository.findAll().stream()
                .filter(course -> course.getTeacher() == null)
                .toList();
    }

    public List<User> getAvailableTeachers() {
        return userRepository.findByRole(UserRole.TEACHER);
    }

    // Bulk operations for admin
    public List<Course> assignMultipleCoursesToTeacher(List<Long> courseIds, Long teacherId, Long adminId) {
        // Validate that the assigner is an admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can assign courses to teachers");
        }

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new RuntimeException("User must have TEACHER role to be assigned courses");
        }

        List<Course> updatedCourses = courseIds.stream()
                .map(courseId -> {
                    Course course = courseRepository.findById(courseId)
                            .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
                    course.setTeacher(teacher);
                    return courseRepository.save(course);
                })
                .toList();

        return updatedCourses;
    }
}
