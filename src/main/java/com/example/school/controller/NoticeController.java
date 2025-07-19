package com.example.school.controller;

import com.example.school.model.Notice;
import com.example.school.model.NoticePriority;
import com.example.school.service.NoticeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notices")
@CrossOrigin(origins = "*")
public class NoticeController {
    
    @Autowired
    private NoticeService noticeService;
    
    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        List<Notice> notices = noticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Notice>> getAllActiveNotices() {
        List<Notice> notices = noticeService.getAllActiveNotices();
        return ResponseEntity.ok(notices);
    }
    
    @GetMapping("/active/priority")
    public ResponseEntity<List<Notice>> getActiveNoticesOrderedByPriority() {
        List<Notice> notices = noticeService.getActiveNoticesOrderedByPriority();
        return ResponseEntity.ok(notices);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Long id) {
        Optional<Notice> notice = noticeService.getNoticeById(id);
        return notice.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Notice>> getNoticesByPriority(@PathVariable NoticePriority priority) {
        List<Notice> notices = noticeService.getNoticesByPriority(priority);
        return ResponseEntity.ok(notices);
    }
    
    @GetMapping("/creator/{createdById}")
    public ResponseEntity<List<Notice>> getNoticesByCreator(@PathVariable Long createdById) {
        List<Notice> notices = noticeService.getNoticesByCreator(createdById);
        return ResponseEntity.ok(notices);
    }
    
    @PostMapping
    public ResponseEntity<?> createNotice(@Valid @RequestBody Notice notice) {
        try {
            Notice createdNotice = noticeService.createNotice(notice);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotice(@PathVariable Long id, @Valid @RequestBody Notice noticeDetails) {
        try {
            Notice updatedNotice = noticeService.updateNotice(id, noticeDetails);
            return ResponseEntity.ok(updatedNotice);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long id) {
        try {
            noticeService.deleteNotice(id);
            return ResponseEntity.ok().body(Map.of("message", "Notice deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateNotice(@PathVariable Long id) {
        try {
            Notice deactivatedNotice = noticeService.deactivateNotice(id);
            return ResponseEntity.ok(Map.of(
                "message", "Notice deactivated successfully",
                "notice", deactivatedNotice
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateNotice(@PathVariable Long id) {
        try {
            Notice activatedNotice = noticeService.activateNotice(id);
            return ResponseEntity.ok(Map.of(
                "message", "Notice activated successfully",
                "notice", activatedNotice
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
