package com.app6768688.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Journal {
    private Long id;
    private Integer weekNumber;
    private String title;
    private String excerpt;
    private String content;
    private JournalStatus status;
    private Boolean featured;
    private String tags;
    private Integer views;
    private LocalDateTime publishDate;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Journal() {}

    public Journal(Integer weekNumber, String title, String content, Long createdBy) {
        this.weekNumber = weekNumber;
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.status = JournalStatus.DRAFT;
        this.featured = false;
        this.views = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getWeekNumber() { return weekNumber; }
    public void setWeekNumber(Integer weekNumber) { this.weekNumber = weekNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public JournalStatus getStatus() { return status; }
    public void setStatus(JournalStatus status) { this.status = status; }

    public Boolean getFeatured() { return featured; }
    public void setFeatured(Boolean featured) { this.featured = featured; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }

    public LocalDateTime getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDateTime publishDate) { this.publishDate = publishDate; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public boolean isPublished() {
        return status == JournalStatus.PUBLISHED;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void incrementViews() {
        this.views++;
    }

    public int getReadingTime() {
        if (content == null) return 1;
        int wordCount = content.split("\\s+").length;
        int wordsPerMinute = 200;
        return Math.max(1, Math.round((float) wordCount / wordsPerMinute));
    }

    public String getExcerptOrGenerated() {
        if (excerpt != null && !excerpt.trim().isEmpty()) {
            return excerpt;
        }
        
        if (content == null) return "";
        
        String plainContent = content.replaceAll("<[^>]*>", "");
        return plainContent.length() > 150 ? 
            plainContent.substring(0, 150) + "..." : 
            plainContent;
    }

    public List<String> getTagsList() {
        if (tags == null || tags.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.asList(tags.split(","));
    }

    // Enums
    public enum JournalStatus {
        DRAFT, PUBLISHED, SCHEDULED
    }
}
