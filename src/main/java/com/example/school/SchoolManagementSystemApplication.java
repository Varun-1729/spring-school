package com.example.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchoolManagementSystemApplication {

    public static void main(String[] args) {
        // Set development profile if no profile is specified
        if (System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", "dev");
        }

        SpringApplication app = new SpringApplication(SchoolManagementSystemApplication.class);
        app.run(args);

        // Print startup message
        System.out.println("\n🎓 School Management System Started Successfully!");
        System.out.println("🌐 Access URL: http://localhost:8080/school-login.html");
        System.out.println("🔐 Demo Credentials:");
        System.out.println("   👨‍💼 Admin: admin@school.com / admin123");
        System.out.println("   👩‍🏫 Teacher: teacher@school.com / teacher123");
        System.out.println("   🎓 Student: student@school.com / student123");
        System.out.println("✅ Browser should open automatically!\n");
    }

}
