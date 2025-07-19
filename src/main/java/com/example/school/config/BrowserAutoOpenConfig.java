package com.example.school.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

/**
 * Configuration class that automatically opens the browser when the application starts
 * Perfect for IntelliJ IDEA run button usage
 */
@Component
public class BrowserAutoOpenConfig {

    private final Environment environment;

    public BrowserAutoOpenConfig(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        // Only open browser in development mode (not in production)
        String[] activeProfiles = environment.getActiveProfiles();
        boolean isDevelopment = activeProfiles.length == 0 || 
                               java.util.Arrays.asList(activeProfiles).contains("dev") ||
                               java.util.Arrays.asList(activeProfiles).contains("local");

        if (isDevelopment) {
            CompletableFuture.runAsync(() -> {
                try {
                    // Wait a moment for the server to be fully ready
                    Thread.sleep(3000);
                    
                    String port = environment.getProperty("server.port", "8080");
                    String url = "http://localhost:" + port + "/school-login.html";
                    
                    System.out.println("🌐 Opening browser automatically...");
                    System.out.println("📱 URL: " + url);
                    System.out.println("🔐 Demo Credentials:");
                    System.out.println("   👨‍💼 Admin: admin@school.com / admin123");
                    System.out.println("   👩‍🏫 Teacher: teacher@school.com / teacher123");
                    System.out.println("   🎓 Student: student@school.com / student123");
                    
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            desktop.browse(new URI(url));
                            System.out.println("✅ Browser opened successfully!");
                        }
                    } else {
                        // Fallback for systems without Desktop support
                        String os = System.getProperty("os.name").toLowerCase();
                        Runtime runtime = Runtime.getRuntime();
                        
                        if (os.contains("win")) {
                            runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
                        } else if (os.contains("mac")) {
                            runtime.exec("open " + url);
                        } else if (os.contains("nix") || os.contains("nux")) {
                            runtime.exec("xdg-open " + url);
                        }
                        System.out.println("✅ Browser command executed!");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Could not open browser automatically: " + e.getMessage());
                    System.out.println("📱 Please manually open: http://localhost:8080/school-login.html");
                }
            });
        }
    }
}
