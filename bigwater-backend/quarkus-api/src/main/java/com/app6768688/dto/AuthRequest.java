package com.app6768688.dto;

public class AuthRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String referralCode;
    
    // Default constructor
    public AuthRequest() {}
    
    // Constructor for login
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    // Constructor for registration
    public AuthRequest(String email, String password, String firstName, String lastName, String phone, String referralCode) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.referralCode = referralCode;
    }
    
    // Getters and Setters
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getReferralCode() {
        return referralCode;
    }
    
    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }
}
