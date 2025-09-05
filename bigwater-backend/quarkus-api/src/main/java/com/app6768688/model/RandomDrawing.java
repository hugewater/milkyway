package com.app6768688.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RandomDrawing {
    private Long id;
    private String drawingName;
    private DrawingType drawingType;
    private Integer weekNumber;
    private LocalDateTime drawingDate;
    private BigDecimal prizePool;
    private String winningNumbers;
    private DrawingStatus status;
    private Integer totalParticipants;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public RandomDrawing() {}

    public RandomDrawing(String drawingName, DrawingType drawingType, Integer weekNumber, 
                        LocalDateTime drawingDate, BigDecimal prizePool, Long createdBy) {
        this.drawingName = drawingName;
        this.drawingType = drawingType;
        this.weekNumber = weekNumber;
        this.drawingDate = drawingDate;
        this.prizePool = prizePool;
        this.createdBy = createdBy;
        this.status = DrawingStatus.PENDING;
        this.totalParticipants = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDrawingName() { return drawingName; }
    public void setDrawingName(String drawingName) { this.drawingName = drawingName; }

    public DrawingType getDrawingType() { return drawingType; }
    public void setDrawingType(DrawingType drawingType) { this.drawingType = drawingType; }

    public Integer getWeekNumber() { return weekNumber; }
    public void setWeekNumber(Integer weekNumber) { this.weekNumber = weekNumber; }

    public LocalDateTime getDrawingDate() { return drawingDate; }
    public void setDrawingDate(LocalDateTime drawingDate) { this.drawingDate = drawingDate; }

    public BigDecimal getPrizePool() { return prizePool; }
    public void setPrizePool(BigDecimal prizePool) { this.prizePool = prizePool; }

    public String getWinningNumbers() { return winningNumbers; }
    public void setWinningNumbers(String winningNumbers) { this.winningNumbers = winningNumbers; }

    public DrawingStatus getStatus() { return status; }
    public void setStatus(DrawingStatus status) { this.status = status; }

    public Integer getTotalParticipants() { return totalParticipants; }
    public void setTotalParticipants(Integer totalParticipants) { this.totalParticipants = totalParticipants; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public boolean isPending() {
        return status == DrawingStatus.PENDING;
    }

    public boolean isCompleted() {
        return status == DrawingStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return status == DrawingStatus.CANCELLED;
    }

    public boolean isOpenForParticipation() {
        return isPending() && LocalDateTime.now().isBefore(drawingDate);
    }

    public void incrementParticipants() {
        this.totalParticipants++;
    }

    public String getFormattedPrizePool() {
        return prizePool != null ? prizePool.toString() + " USDT" : "0 USDT";
    }

    public String getFormattedDrawingDate() {
        return drawingDate != null ? drawingDate.toString() : "Not set";
    }

    public List<String> getWinningNumbersList() {
        if (winningNumbers == null || winningNumbers.trim().isEmpty()) {
            return List.of();
        }
        return List.of(winningNumbers.split(","));
    }

    public boolean hasWinningNumbers() {
        return winningNumbers != null && !winningNumbers.trim().isEmpty();
    }

    // Enums
    public enum DrawingType {
        SWEEPSTAKES, RAFFLE, CONTEST
    }

    public enum DrawingStatus {
        PENDING, COMPLETED, CANCELLED
    }
}
