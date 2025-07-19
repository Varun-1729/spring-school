package com.example.school.service;

import com.example.school.model.Notice;
import com.example.school.model.NoticePriority;
import com.example.school.model.User;
import com.example.school.model.UserRole;
import com.example.school.repository.NoticeRepository;
import com.example.school.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {
    
    @Autowired
    private NoticeRepository noticeRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }
    
    public List<Notice> getAllActiveNotices() {
        return noticeRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }
    
    public List<Notice> getActiveNoticesOrderedByPriority() {
        return noticeRepository.findActiveNoticesOrderByPriorityAndDate();
    }
    
    public Optional<Notice> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }
    
    public List<Notice> getNoticesByPriority(NoticePriority priority) {
        return noticeRepository.findByPriorityAndIsActiveTrue(priority);
    }
    
    public List<Notice> getNoticesByCreator(Long createdById) {
        return noticeRepository.findByCreatedById(createdById);
    }
    
    public Notice createNotice(Notice notice) {
        // Validate that the creator exists and has ADMIN role
        User creator = userRepository.findById(notice.getCreatedBy().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + notice.getCreatedBy().getId()));
        
        if (creator.getRole() != UserRole.ADMIN) {
            throw new RuntimeException("Only ADMIN users can create notices");
        }
        
        notice.setCreatedBy(creator);
        return noticeRepository.save(notice);
    }
    
    public Notice updateNotice(Long id, Notice noticeDetails) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        
        notice.setTitle(noticeDetails.getTitle());
        notice.setContent(noticeDetails.getContent());
        notice.setPriority(noticeDetails.getPriority());
        notice.setIsActive(noticeDetails.getIsActive());
        
        return noticeRepository.save(notice);
    }
    
    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        noticeRepository.delete(notice);
    }
    
    public Notice deactivateNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        
        notice.setIsActive(false);
        return noticeRepository.save(notice);
    }
    
    public Notice activateNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found with id: " + id));
        
        notice.setIsActive(true);
        return noticeRepository.save(notice);
    }
}
