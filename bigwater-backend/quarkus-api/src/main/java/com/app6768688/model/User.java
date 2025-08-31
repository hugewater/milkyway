package com.app6768688.model;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private UserStatus status;
    private UserLevel level;
    private String referralCode;
    private String referredByCode;
    private LocalDateTime joinDate;
    private LocalDateTime lastLogin;
    private LocalDateTime emailVerifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public User() {}

    public User(String email, String passwordHash, String firstName, String lastName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = UserRole.SUBSCRIBER;
        this.status = UserStatus.ACTIVE;
        this.level = UserLevel.CHIEF;
        this.referredByCode = "COMPANY001";
        this.joinDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public UserLevel getLevel() { return level; }
    public void setLevel(UserLevel level) { this.level = level; }

    public String getReferralCode() { return referralCode; }
    public void setReferralCode(String referralCode) { this.referralCode = referralCode; }

    public String getReferredByCode() { return referredByCode; }
    public void setReferredByCode(String referredByCode) { this.referredByCode = referredByCode; }

    public LocalDateTime getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getEmailVerifiedAt() { return emailVerifiedAt; }
    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) { this.emailVerifiedAt = emailVerifiedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return email;
        }
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "").trim();
    }

    public String getInitials() {
        String first = firstName != null && !firstName.isEmpty() ? firstName.substring(0, 1) : "";
        String last = lastName != null && !lastName.isEmpty() ? lastName.substring(0, 1) : "";
        return (first + last).toUpperCase();
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN || role == UserRole.SUPER_ADMIN;
    }

    public boolean isSuperAdmin() {
        return role == UserRole.SUPER_ADMIN;
    }

    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    // Enums
    public enum UserRole {
        SUBSCRIBER, ADMIN, SUPER_ADMIN
    }

    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }

    public enum UserLevel {
        CHIEF("Chief", 1),
        MAYOR("Mayor", 2),
        GOVERNOR("Governor", 3),
        MINISTER("Minister", 4),
        PRESIDENT("President", 5);

        private final String displayName;
        private final int rank;

        UserLevel(String displayName, int rank) {
            this.displayName = displayName;
            this.rank = rank;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getRank() {
            return rank;
        }

        public static UserLevel fromString(String level) {
            if (level == null) return CHIEF;
            
            for (UserLevel userLevel : values()) {
                if (userLevel.name().equalsIgnoreCase(level) || 
                    userLevel.displayName.equalsIgnoreCase(level)) {
                    return userLevel;
                }
            }
            return CHIEF;
        }
    }
}
