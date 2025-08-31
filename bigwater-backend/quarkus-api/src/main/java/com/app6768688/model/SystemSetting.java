package com.app6768688.model;

import java.time.LocalDateTime;

public class SystemSetting {
    private Long id;
    private String settingKey;
    private String settingValue;
    private SettingType settingType;
    private String description;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public SystemSetting() {}

    public SystemSetting(String settingKey, String settingValue, SettingType settingType, 
                        String description, Boolean isPublic) {
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.settingType = settingType;
        this.description = description;
        this.isPublic = isPublic;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSettingKey() { return settingKey; }
    public void setSettingKey(String settingKey) { this.settingKey = settingKey; }

    public String getSettingValue() { return settingValue; }
    public void setSettingValue(String settingValue) { this.settingValue = settingValue; }

    public SettingType getSettingType() { return settingType; }
    public void setSettingType(SettingType settingType) { this.settingType = settingType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods
    public boolean isPublic() {
        return isPublic != null && isPublic;
    }

    public boolean isPrivate() {
        return !isPublic();
    }

    public boolean isStringType() {
        return settingType == SettingType.STRING;
    }

    public boolean isNumberType() {
        return settingType == SettingType.NUMBER;
    }

    public boolean isBooleanType() {
        return settingType == SettingType.BOOLEAN;
    }

    public boolean isJsonType() {
        return settingType == SettingType.JSON;
    }

    public String getFormattedValue() {
        if (settingValue == null) return "null";
        
        switch (settingType) {
            case BOOLEAN:
                return Boolean.parseBoolean(settingValue) ? "true" : "false";
            case NUMBER:
                try {
                    double num = Double.parseDouble(settingValue);
                    return String.format("%.2f", num);
                } catch (NumberFormatException e) {
                    return settingValue;
                }
            case JSON:
                return settingValue;
            case STRING:
            default:
                return settingValue;
        }
    }

    public boolean getBooleanValue() {
        if (settingType != SettingType.BOOLEAN) {
            throw new IllegalStateException("Setting is not a boolean type");
        }
        return Boolean.parseBoolean(settingValue);
    }

    public double getNumberValue() {
        if (settingType != SettingType.NUMBER) {
            throw new IllegalStateException("Setting is not a number type");
        }
        try {
            return Double.parseDouble(settingValue);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid number value: " + settingValue);
        }
    }

    public int getIntValue() {
        if (settingType != SettingType.NUMBER) {
            throw new IllegalStateException("Setting is not a number type");
        }
        try {
            return Integer.parseInt(settingValue);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid integer value: " + settingValue);
        }
    }

    public String getFormattedType() {
        return settingType != null ? settingType.name() : "UNKNOWN";
    }

    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    // Enums
    public enum SettingType {
        STRING, NUMBER, BOOLEAN, JSON
    }
}
