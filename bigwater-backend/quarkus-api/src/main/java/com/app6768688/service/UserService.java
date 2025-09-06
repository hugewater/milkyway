package com.app6768688.service;

import com.app6768688.model.User;
import com.app6768688.repository.UserRepository;
import com.app6768688.util.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int REFERRAL_CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public User createUser(String email, String passwordHash, String firstName, String lastName, String phone, String referralCode) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }

        // Generate unique referral code
        String generatedReferralCode = generateUniqueReferralCode();

        // Create user
        User user = new User(email, passwordHash, firstName, lastName);
        user.setPhone(phone);
        user.setReferralCode(generatedReferralCode);
        user.setReferredByCode(referralCode != null ? referralCode : "COMPANY001");
        user.setRole(User.UserRole.SUBSCRIBER);
        user.setStatus(User.UserStatus.ACTIVE);
        user.setLevel(User.UserLevel.CHIEF); // All new users start as CHIEF level
        user.setJoinDate(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.create(user);
    }

    @Transactional
    public User createAdminUser(String email, String passwordHash, String firstName, String lastName, String phone, String referralCode) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }

        // Create admin user
        User user = new User(email, passwordHash, firstName, lastName);
        user.setPhone(phone);
        user.setReferralCode(referralCode != null ? referralCode : "ADMIN001");
        user.setReferredByCode("COMPANY001");
        user.setRole(User.UserRole.ADMIN);
        user.setStatus(User.UserStatus.ACTIVE);
        user.setLevel(User.UserLevel.MINISTER);
        user.setJoinDate(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.create(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByReferralCode(String referralCode) {
        return userRepository.findByReferralCode(referralCode);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByQuery(UserRepository.UserQuery q) {
        return userRepository.findByQuery(q);
    }

    public long countByQuery(UserRepository.UserQuery q) {
        return userRepository.countByQuery(q);
    }

    public List<User> findByStatus(User.UserStatus status) {
        return userRepository.findByStatus(status);
    }

    public List<User> findByRole(User.UserRole role) {
        return userRepository.findByRole(role);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.update(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Transactional
    public User updateLastLogin(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            return userRepository.update(user);
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    @Transactional
    public User updateUserStatus(Long userId, User.UserStatus status) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(status);
            return userRepository.update(user);
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    @Transactional
    public User updateUserRole(Long userId, User.UserRole role) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setRole(role);
            return userRepository.update(user);
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    @Transactional
    public User verifyEmail(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmailVerifiedAt(LocalDateTime.now());
            return userRepository.update(user);
        }
        throw new RuntimeException("User not found with ID: " + userId);
    }

    public boolean isEmailVerified(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(user -> user.getEmailVerifiedAt() != null).orElse(false);
    }

    public boolean isUserActive(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::isActive).orElse(false);
    }

    public boolean isAdmin(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::isAdmin).orElse(false);
    }

    public boolean isSuperAdmin(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::isSuperAdmin).orElse(false);
    }

    private String generateUniqueReferralCode() {
        String referralCode;
        do {
            referralCode = generateReferralCode();
        } while (userRepository.existsByReferralCode(referralCode));
        return referralCode;
    }

    private String generateReferralCode() {
        StringBuilder sb = new StringBuilder(REFERRAL_CODE_LENGTH);
        for (int i = 0; i < REFERRAL_CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public List<User> getUsersByReferrer(String referrerCode) {
        // This would need to be implemented in the repository
        // For now, we'll return all users and filter in memory
        return userRepository.findAll().stream()
                .filter(user -> referrerCode.equals(user.getReferredByCode()))
                .toList();
    }

    public long getTotalUserCount() {
        return userRepository.findAll().size();
    }

    public long getActiveUserCount() {
        return userRepository.findByStatus(User.UserStatus.ACTIVE).size();
    }

    public long getSubscriberCount() {
        return userRepository.findByRole(User.UserRole.SUBSCRIBER).size();
    }
    
    // Authentication methods
    @Inject
    PasswordUtil passwordUtil;
    
    public User registerUser(String email, String password, String firstName, String lastName, String phone, String referralCode) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }
        
        // Hash password
        String hashedPassword = passwordUtil.hashPassword(password);
        
        // Generate unique referral code
        String generatedReferralCode = generateUniqueReferralCode();
        
        // Create user
        User user = new User(email, hashedPassword, firstName, lastName);
        user.setPhone(phone);
        user.setReferralCode(generatedReferralCode);
        user.setReferredByCode(referralCode != null ? referralCode : "COMPANY001");
        user.setRole(User.UserRole.SUBSCRIBER);
        user.setStatus(User.UserStatus.ACTIVE);
        
        return userRepository.create(user);
    }
    
    public User authenticateUser(String email, String password) {
        System.out.println("Authenticating user: " + email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found: " + user.getEmail());
            System.out.println("Stored hash: " + user.getPasswordHash());
            boolean isValid = passwordUtil.verifyPassword(password, user.getPasswordHash());
            System.out.println("Password valid: " + isValid);
            if (isValid) {
                // Update last login
                user.setLastLogin(LocalDateTime.now());
                userRepository.update(user);
                return user;
            }
        } else {
            System.out.println("User not found");
        }
        throw new RuntimeException("Invalid email or password");
    }
    
    public boolean validatePassword(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordUtil.verifyPassword(password, user.getPasswordHash());
        }
        return false;
    }

    public Map<String, Object> getUserNetwork(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        User user = userOpt.get();
        Map<String, Object> networkData = new HashMap<>();

        // Get uplines (referrers) - all the way to the top-most
        List<User> uplines = getUplinesToTop(user);
        networkData.put("uplines", uplines);

        // Get downlines (referrals) - up to 3 levels down
        List<User> downlines = getDownlines(user, 3);
        networkData.put("downlines", downlines);

        return networkData;
    }

    private List<User> getUplines(User user, int levels) {
        List<User> uplines = new ArrayList<>();
        User currentUser = user;
        
        for (int i = 0; i < levels && currentUser != null; i++) {
            String referrerCode = currentUser.getReferredByCode();
            if (referrerCode != null && !referrerCode.equals("COMPANY001")) {
                Optional<User> referrerOpt = userRepository.findByReferralCode(referrerCode);
                if (referrerOpt.isPresent()) {
                    uplines.add(referrerOpt.get());
                    currentUser = referrerOpt.get();
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        
        return uplines;
    }

    // Walk up the tree by referred_by_code until hitting COMPANY001 (top) or null
    private List<User> getUplinesToTop(User user) {
        List<User> uplines = new ArrayList<>();
        String referrerCode = user.getReferredByCode();

        while (referrerCode != null && !referrerCode.equals("COMPANY001")) {
            Optional<User> referrerOpt = userRepository.findByReferralCode(referrerCode);
            if (referrerOpt.isPresent()) {
                User referrer = referrerOpt.get();
                uplines.add(referrer);
                referrerCode = referrer.getReferredByCode();
            } else {
                break;
            }
        }

        return uplines;
    }

    public List<User> getDownlines(User user, int levels) {
        List<User> downlines = new ArrayList<>();
        
        // Get direct downlines (level 1)
        List<User> directDownlines = userRepository.findAll().stream()
                .filter(u -> user.getReferralCode().equals(u.getReferredByCode()))
                .toList();
        downlines.addAll(directDownlines);
        
        // Get indirect downlines (levels 2 and 3)
        if (levels > 1) {
            for (User directDownline : directDownlines) {
                List<User> level2Downlines = userRepository.findAll().stream()
                        .filter(u -> directDownline.getReferralCode().equals(u.getReferredByCode()))
                        .toList();
                downlines.addAll(level2Downlines);
                
                // Get level 3 downlines
                if (levels > 2) {
                    for (User level2Downline : level2Downlines) {
                        List<User> level3Downlines = userRepository.findAll().stream()
                                .filter(u -> level2Downline.getReferralCode().equals(u.getReferredByCode()))
                                .toList();
                        downlines.addAll(level3Downlines);
                    }
                }
            }
        }
        
        return downlines;
    }

    /**
     * Helper for API: get downlines by user id with configurable depth
     */
    public List<User> getDownlinesByUserId(Long userId, int levels) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        int depth = Math.max(1, Math.min(levels, 5)); // sanity limit
        return getDownlines(userOpt.get(), depth);
    }

    /**
     * Count ALL descendants (downlines) under a user, unlimited depth.
     */
    public int countAllDownlinesByUser(User user) {
        List<User> allUsers = userRepository.findAll();
        // Build adjacency by referredByCode -> children users
        Map<String, List<User>> byRef = new HashMap<>();
        for (User u : allUsers) {
            String parentCode = u.getReferredByCode();
            if (parentCode == null) parentCode = "";
            byRef.computeIfAbsent(parentCode, k -> new ArrayList<>()).add(u);
        }
        // Memoized DFS by referralCode
        Map<String, Integer> memo = new HashMap<>();
        return countDescendantsByReferralCode(user.getReferralCode(), byRef, memo);
    }

    private int countDescendantsByReferralCode(String referralCode, Map<String, List<User>> byRef, Map<String, Integer> memo) {
        if (referralCode == null) return 0;
        if (memo.containsKey(referralCode)) return memo.get(referralCode);
        List<User> children = byRef.getOrDefault(referralCode, List.of());
        int total = 0;
        for (User child : children) {
            total += 1 + countDescendantsByReferralCode(child.getReferralCode(), byRef, memo);
        }
        memo.put(referralCode, total);
        return total;
    }

    /**
     * Batch count ALL descendants for a list of user IDs.
     */
    public Map<Long, Integer> countAllDownlinesByUserIds(List<Long> userIds) {
        Map<Long, Integer> result = new HashMap<>();
        if (userIds == null || userIds.isEmpty()) return result;

        List<User> allUsers = userRepository.findAll();
        Map<Long, User> idToUser = new HashMap<>();
        for (User u : allUsers) idToUser.put(u.getId(), u);

        Map<String, List<User>> byRef = new HashMap<>();
        for (User u : allUsers) {
            String parentCode = u.getReferredByCode();
            if (parentCode == null) parentCode = "";
            byRef.computeIfAbsent(parentCode, k -> new ArrayList<>()).add(u);
        }
        Map<String, Integer> memo = new HashMap<>();

        for (Long id : userIds) {
            User user = idToUser.get(id);
            if (user == null) continue;
            int cnt = countDescendantsByReferralCode(user.getReferralCode(), byRef, memo);
            result.put(id, cnt);
        }
        return result;
    }
}
