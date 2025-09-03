package com.app6768688;

import com.app6768688.model.User;
import com.app6768688.model.Certificate;
import com.app6768688.model.UsdtWallet;
import com.app6768688.model.Journal;
import com.app6768688.model.RandomDrawing;
import com.app6768688.service.UserService;
import com.app6768688.service.CertificateService;
import com.app6768688.service.WalletService;
import com.app6768688.service.AuthService;
import com.app6768688.service.JournalService;
import com.app6768688.dto.AuthRequest;
import com.app6768688.dto.AuthResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.math.BigDecimal;
import com.app6768688.service.TransactionService;
import com.app6768688.model.Transaction;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import com.app6768688.util.PasswordUtil;

@Path("/bw-api")
public class BigWaterResource {

    @Inject
    UserService userService;

    @Inject
    CertificateService certificateService;

    @Inject
    WalletService walletService;
    
    @Inject
    TransactionService transactionService;

    @Inject
    AuthService authService;

    @Inject
    JournalService journalService;

    @Inject
    DataSource dataSource;

    @Inject
    PasswordUtil passwordUtil;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "BigWater API is running");
        response.put("version", "1.0.0");
        response.put("status", "active");
        response.put("database", "JDBC + Agroal");
        
        return Response.ok(response).build();
    }

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        response.put("database", "Connected");
        
        return Response.ok(response).build();
    }

    @GET
    @Path("/version")
    @Produces(MediaType.APPLICATION_JSON)
    public Response version() {
        Map<String, Object> response = new HashMap<>();
        response.put("version", "1.0.0");
        response.put("framework", "Quarkus");
        response.put("java", System.getProperty("java.version"));
        response.put("database", "MySQL + JDBC");
        
        return Response.ok(response).build();
    }

    @GET
    @Path("/init")
    @Produces(MediaType.APPLICATION_JSON)
    public Response initializeDatabase() {
        try {
            // Create admin user
            User adminUser = userService.createAdminUser(
                "admin@bigwater.com",
                passwordUtil.hashPassword("admin123"),
                "Admin",
                "User",
                "",
                "ADMIN001"
            );
            
            // Create test user
            User testUser = userService.createUser(
                "test@test.com",
                passwordUtil.hashPassword("admin123"),
                "Test",
                "User",
                "",
                "ADMIN001"
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Database initialized with test users");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    // User endpoints
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(
        @QueryParam("offset") Integer offset,
        @QueryParam("limit") Integer limit,
        @QueryParam("sort") String sort,
        @QueryParam("order") String order,
        @QueryParam("role") String role,
        @QueryParam("status") String status,
        @QueryParam("level") String level,
        @QueryParam("q") String q
    ) {
        try {
            com.app6768688.repository.UserRepository.UserQuery query = new com.app6768688.repository.UserRepository.UserQuery();
            query.offset = offset;
            query.limit = limit;
            query.sortBy = sort;
            query.order = order;
            query.role = role;
            query.status = status;
            query.level = level;
            query.q = q;

            long total = userService.countByQuery(query);
            List<User> items = userService.findByQuery(query);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", items);
            response.put("total", total);
            response.put("count", items.size());
            response.put("offset", query.offset != null ? query.offset : 0);
            response.put("limit", query.limit != null ? query.limit : 50);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        try {
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", userOpt.get());
                
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "User not found");
                
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(Map<String, Object> userData) {
        try {
            String email = (String) userData.get("email");
            String password = (String) userData.get("password");
            String firstName = (String) userData.get("firstName");
            String lastName = (String) userData.get("lastName");
            String phone = (String) userData.get("phone");
            String referralCode = (String) userData.get("referralCode");
            String level = (String) userData.get("level");
            String referredByCode = (String) userData.get("referredByCode");

            // Hash the password
            String passwordHash = passwordUtil.hashPassword(password);
            
            // Create user with basic info
            User user = userService.createUser(email, passwordHash, firstName, lastName, phone, referralCode);
            
            // Set additional fields if provided
            if (level != null) {
                user.setLevel(User.UserLevel.fromString(level));
            }
            if (referredByCode != null) {
                user.setReferredByCode(referredByCode);
            }
            
            // Update the user with additional fields
            user = userService.updateUser(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", user);
            response.put("message", "User created successfully");
            
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            String base = e.getMessage() != null ? e.getMessage() : "Update failed";
            String causeMsg = e.getCause() != null && e.getCause().getMessage() != null ? (": " + e.getCause().getMessage()) : "";
            response.put("error", base + causeMsg);
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @PUT
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, Map<String, Object> userData) {
        try {
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Update user fields
                if (userData.containsKey("firstName")) {
                    user.setFirstName((String) userData.get("firstName"));
                }
                if (userData.containsKey("lastName")) {
                    user.setLastName((String) userData.get("lastName"));
                }
                if (userData.containsKey("email")) {
                    user.setEmail((String) userData.get("email"));
                }
                if (userData.containsKey("phone")) {
                    user.setPhone((String) userData.get("phone"));
                }
                if (userData.containsKey("role")) {
                    user.setRole(User.UserRole.valueOf((String) userData.get("role")));
                }
                if (userData.containsKey("status")) {
                    user.setStatus(User.UserStatus.valueOf((String) userData.get("status")));
                }
                if (userData.containsKey("level")) {
                    user.setLevel(User.UserLevel.fromString((String) userData.get("level")));
                }
                if (userData.containsKey("referredByCode")) {
                    user.setReferredByCode((String) userData.get("referredByCode"));
                }
                if (userData.containsKey("password")) {
                    String password = (String) userData.get("password");
                    if (password != null && !password.trim().isEmpty()) {
                        String passwordHash = passwordUtil.hashPassword(password);
                        user.setPasswordHash(passwordHash);
                    }
                }               
                User updatedUser = userService.updateUser(user);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", updatedUser);
                response.put("message", "User updated successfully");
                
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "User not found");
                
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @PUT
    @Path("/users/{id}/level")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserLevel(@PathParam("id") Long id, Map<String, String> request) {
        try {
            String newLevel = request.get("level");
            if (newLevel == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Level is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                // Only allow updating SUBSCRIBER users' levels
                if (user.getRole() != User.UserRole.SUBSCRIBER) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Can only update levels for SUBSCRIBER users");
                    return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
                }
                
                // Validate the level
                try {
                    User.UserLevel level = User.UserLevel.fromString(newLevel);
                    user.setLevel(level);
                    user.setUpdatedAt(LocalDateTime.now());
                    
                    User updatedUser = userService.updateUser(user);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("message", "User level updated successfully");
                    response.put("data", updatedUser);
                    
                    return Response.ok(response).build();
                } catch (IllegalArgumentException e) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Invalid level: " + newLevel + ". Valid levels are: CHIEF, MAYOR, GOVERNOR, MINISTER, PRESIDENT");
                    return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
                }
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "User not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }

    @DELETE
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            Optional<User> userOpt = userService.findById(id);
            if (userOpt.isPresent()) {
                userService.deleteUser(id);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "User deleted successfully");
                
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "User not found");
                
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/users/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", userService.getTotalUserCount());
            stats.put("activeUsers", userService.getActiveUserCount());
            stats.put("subscribers", userService.getSubscriberCount());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    // Certificate endpoints
    @GET
    @Path("/certificates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCertificates() {
        try {
            List<Certificate> certificates = certificateService.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", certificates);
            response.put("count", certificates.size());
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/certificates/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCertificateById(@PathParam("id") Long id) {
        try {
            Optional<Certificate> certOpt = certificateService.findById(id);
            if (certOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", certOpt.get());
                
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Certificate not found");
                
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/certificates")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCertificate(Map<String, Object> certData) {
        try {
            String certificateName = (String) certData.get("certificateName");
            String description = (String) certData.get("description");
            String priceUsdtStr = (String) certData.get("priceUsdt");
            String durationDaysStr = (String) certData.get("durationDays");
            String benefits = (String) certData.get("benefits");
            String maxSupplyStr = (String) certData.get("maxSupply");
            String createdByStr = (String) certData.get("createdBy");
            Boolean isActive = (Boolean) certData.get("isActive");

            BigDecimal priceUsdt = new BigDecimal(priceUsdtStr);
            Integer durationDays = Integer.valueOf(durationDaysStr);
            Integer maxSupply = maxSupplyStr != null ? Integer.valueOf(maxSupplyStr) : null;
            Long createdBy = Long.valueOf(createdByStr);

            Certificate certificate = certificateService.createCertificate(
                certificateName, description, priceUsdt, 
                durationDays, benefits, maxSupply, createdBy, isActive
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", certificate);
            response.put("message", "Certificate created successfully");
            
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/certificates/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCertificateStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCertificates", certificateService.getTotalCertificateCount());
            stats.put("activeCertificates", certificateService.getActiveCertificateCount());
            stats.put("availableCertificates", certificateService.getAvailableCertificateCount());
            stats.put("totalValue", certificateService.getTotalValueOfAllCertificates());
            stats.put("averagePrice", certificateService.getAverageCertificatePrice());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @PUT
    @Path("/certificates/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCertificate(@PathParam("id") Long id, Map<String, Object> certData) {
        try {
            Optional<Certificate> certOpt = certificateService.findById(id);
            if (!certOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Certificate not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }

            Certificate certificate = certOpt.get();
            
            // Update fields if provided
            if (certData.containsKey("certificateName")) {
                certificate.setCertificateName((String) certData.get("certificateName"));
            }
            if (certData.containsKey("description")) {
                certificate.setDescription((String) certData.get("description"));
            }
            if (certData.containsKey("priceUsdt")) {
                certificate.setPriceUsdt(new BigDecimal(certData.get("priceUsdt").toString()));
            }
            if (certData.containsKey("durationDays")) {
                certificate.setDurationDays(Integer.valueOf(certData.get("durationDays").toString()));
            }
            if (certData.containsKey("benefits")) {
                certificate.setBenefits((String) certData.get("benefits"));
            }
            if (certData.containsKey("maxSupply")) {
                String maxSupplyStr = (String) certData.get("maxSupply");
                if (maxSupplyStr != null && !maxSupplyStr.trim().isEmpty()) {
                    certificate.setMaxSupply(Integer.valueOf(maxSupplyStr));
                } else {
                    certificate.setMaxSupply(null);
                }
            }
            if (certData.containsKey("isActive")) {
                certificate.setIsActive((Boolean) certData.get("isActive"));
            }
            
            certificate.setUpdatedAt(LocalDateTime.now());
            Certificate updatedCertificate = certificateService.updateCertificate(certificate);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedCertificate);
            response.put("message", "Certificate updated successfully");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @DELETE
    @Path("/certificates/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCertificate(@PathParam("id") Long id) {
        try {
            Optional<Certificate> certOpt = certificateService.findById(id);
            if (!certOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Certificate not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }

            certificateService.deleteCertificate(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Certificate deleted successfully");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/certificates/{id}/toggle-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toggleCertificateStatus(@PathParam("id") Long id) {
        try {
            Optional<Certificate> certOpt = certificateService.findById(id);
            if (!certOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Certificate not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }

            Certificate certificate = certOpt.get();
            boolean newStatus = !certificate.getIsActive();
            Certificate updatedCertificate = certificateService.updateCertificateStatus(id, newStatus);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedCertificate);
            response.put("message", "Certificate status updated successfully");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    // Wallet endpoints
    @GET
    @Path("/wallets")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWallets(
        @QueryParam("offset") Integer offset,
        @QueryParam("limit") Integer limit,
        @QueryParam("sort") String sort,
        @QueryParam("order") String order,
        @QueryParam("type") String type,
        @QueryParam("active") Boolean active,
        @QueryParam("q") String q
    ) {
        try {
            com.app6768688.repository.WalletRepository.WalletQuery query = new com.app6768688.repository.WalletRepository.WalletQuery();
            query.offset = offset;
            query.limit = limit;
            query.sortBy = sort;
            query.order = order;
            query.type = type;
            query.active = active;
            query.q = q;

            long total = walletService.countByQuery(query);
            List<UsdtWallet> items = walletService.findByQuery(query);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", items);
            response.put("total", total);
            response.put("count", items.size());
            response.put("offset", query.offset != null ? query.offset : 0);
            response.put("limit", query.limit != null ? query.limit : 50);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/wallets/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWalletsByUserId(@PathParam("userId") Long userId) {
        try {
            List<UsdtWallet> wallets = walletService.findByUserId(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", wallets);
            response.put("count", wallets.size());
            response.put("totalBalance", walletService.getTotalBalanceByUserId(userId));
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/wallets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createWallet(Map<String, Object> walletData) {
        try {
            String userIdStr = (String) walletData.get("userId");
            String walletAddress = (String) walletData.get("walletAddress");
            String walletName = (String) walletData.get("walletName");
            String walletTypeStr = (String) walletData.get("walletType");
            Object balanceObj = walletData.get("balance");

            Long userId = Long.valueOf(userIdStr);
            
            // Handle nullable walletType
            UsdtWallet.WalletType walletType = null;
            if (walletTypeStr != null && !walletTypeStr.trim().isEmpty()) {
                walletType = UsdtWallet.WalletType.valueOf(walletTypeStr);
            }
            
            BigDecimal initialBalance = BigDecimal.ZERO;
            if (balanceObj != null) {
                initialBalance = new BigDecimal(balanceObj.toString());
            }

            UsdtWallet wallet = walletService.createWallet(userId, walletAddress, walletName, walletType);
            
            // Set initial balance if provided
            if (initialBalance.compareTo(BigDecimal.ZERO) > 0) {
                walletService.updateBalance(wallet.getId(), initialBalance);
                // Refresh wallet data to get updated balance
                wallet = walletService.findById(wallet.getId()).orElse(wallet);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", wallet);
            response.put("message", "Wallet created successfully");
            
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/wallets/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWalletStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalWallets", walletService.getTotalWalletCount());
            stats.put("verifiedWallets", walletService.getVerifiedWalletCount());
            stats.put("totalSystemBalance", walletService.getTotalSystemBalance());
            stats.put("averageWalletBalance", walletService.getAverageWalletBalance());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/wallets/add-balance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBalanceToWallet(Map<String, Object> payload) {
        try {
            Object walletIdObj = payload.get("walletId");
            Object amountObj = payload.get("amount");

            if (walletIdObj == null || amountObj == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "walletId and amount are required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            Long walletId = Long.valueOf(walletIdObj.toString());
            java.math.BigDecimal amount = new java.math.BigDecimal(amountObj.toString());

            walletService.addBalance(walletId, amount);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Balance added successfully");
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @PUT
    @Path("/wallets/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateWallet(@PathParam("id") Long walletId, Map<String, Object> walletData) {
        try {
            String walletAddress = (String) walletData.get("walletAddress");
            String walletName = (String) walletData.get("walletName");
            String walletTypeStr = (String) walletData.get("walletType");
            Object balanceObj = walletData.get("balance");

            // Get existing wallet
            Optional<UsdtWallet> existingWalletOpt = walletService.findById(walletId);
            if (existingWalletOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Wallet not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }

            UsdtWallet existingWallet = existingWalletOpt.get();

            // Update wallet fields only if provided
            if (walletAddress != null && !walletAddress.trim().isEmpty()) {
                existingWallet.setWalletAddress(walletAddress);
            }
            if (walletName != null && !walletName.trim().isEmpty()) {
                existingWallet.setWalletName(walletName);
            }
            if (walletTypeStr != null && !walletTypeStr.trim().isEmpty()) {
                try {
                    UsdtWallet.WalletType walletType = UsdtWallet.WalletType.valueOf(walletTypeStr.toUpperCase());
                    existingWallet.setWalletType(walletType);
                } catch (IllegalArgumentException iae) {
                    // ignore invalid type and keep original
                }
            }

            UsdtWallet updatedWallet = walletService.updateWallet(existingWallet);
            
            // Update balance if provided
            if (balanceObj != null) {
                BigDecimal newBalance = new BigDecimal(balanceObj.toString());
                walletService.updateBalance(walletId, newBalance);
                // Refresh wallet data to get updated balance
                updatedWallet = walletService.findById(walletId).orElse(updatedWallet);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedWallet);
            response.put("message", "Wallet updated successfully");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/wallets/{id}/toggle-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toggleWalletStatus(@PathParam("id") Long walletId) {
        try {
            // Get existing wallet
            Optional<UsdtWallet> existingWalletOpt = walletService.findById(walletId);
            if (existingWalletOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Wallet not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }

            UsdtWallet existingWallet = existingWalletOpt.get();
            
            if (existingWallet.getIsActive()) {
                walletService.deactivateWallet(walletId);
            } else {
                // Reactivate wallet by setting isActive to true
                existingWallet.setIsActive(true);
                walletService.updateWallet(existingWallet);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Wallet status toggled successfully");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @DELETE
    @Path("/wallets/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteWallet(@PathParam("id") Long walletId) {
        try {
            Optional<UsdtWallet> existingWalletOpt = walletService.findById(walletId);
            if (existingWalletOpt.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Wallet not found");
                return Response.status(Response.Status.NOT_FOUND).entity(response).build();
            }
            walletService.deleteWallet(walletId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Wallet deleted successfully");
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/wallets/{id}/pay")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response payToWallet(@PathParam("id") Long walletId, Map<String, Object> payload) {
        try {
            Object amountObj = payload.get("amount");
            String description = (String) payload.get("description");

            if (amountObj == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Amount is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            BigDecimal amount = new BigDecimal(amountObj.toString());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Amount must be positive");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            // Add balance to wallet
            walletService.addBalance(walletId, amount);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment successful");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/wallets/{id}/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdrawFromWallet(@PathParam("id") Long walletId, Map<String, Object> payload) {
        try {
            Object amountObj = payload.get("amount");
            String description = (String) payload.get("description");
            String toAddress = (String) payload.get("toAddress");

            if (amountObj == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Amount is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            if (toAddress == null || toAddress.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Destination address is required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            BigDecimal amount = new BigDecimal(amountObj.toString());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Amount must be positive");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            // Subtract balance from wallet (this will check for sufficient balance)
            walletService.subtractBalance(walletId, amount);

            // Here you would typically integrate with a blockchain service to actually send the funds
            // For now, we'll just log the withdrawal request
            System.out.println("Withdrawal request: " + amount + " USDT from wallet " + walletId + " to address " + toAddress);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Withdrawal request submitted successfully");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/wallets/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferBetweenWallets(Map<String, Object> payload) {
        try {
            Object fromIdObj = payload.get("fromWalletId");
            Object toIdObj = payload.get("toWalletId");
            Object amountObj = payload.get("amount");

            if (fromIdObj == null || toIdObj == null || amountObj == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "fromWalletId, toWalletId and amount are required");
                return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
            }

            Long fromWalletId = Long.valueOf(fromIdObj.toString());
            Long toWalletId = Long.valueOf(toIdObj.toString());
            java.math.BigDecimal amount = new java.math.BigDecimal(amountObj.toString());

            boolean ok = walletService.transferBetweenWallets(fromWalletId, toWalletId, amount);

            Map<String, Object> response = new HashMap<>();
            response.put("success", ok);
            response.put("message", ok ? "Transfer completed" : "Transfer failed");
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    // Authentication endpoints
    @POST
    @Path("/auth/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(AuthRequest request) {
        try {
            AuthResponse response = authService.register(request);
            
            if (response.isSuccess()) {
                return Response.status(Response.Status.CREATED)
                        .entity(response)
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(false, "Registration failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/auth/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(AuthRequest request) {
        try {
            AuthResponse response = authService.login(request);
            
            if (response.isSuccess()) {
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(false, "Login failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/auth/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(Map<String, String> tokenRequest) {
        try {
            String token = tokenRequest.get("token");
            AuthResponse response = authService.validateToken(token);
            
            if (response.isSuccess()) {
                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(false, "Token validation failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    // Journal endpoints
    @GET
    @Path("/journals")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllJournals() {
        try {
            List<Journal> journals = journalService.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", journals);
            response.put("count", journals.size());
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/journals/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJournalById(@PathParam("id") Long id) {
        try {
            Optional<Journal> journal = journalService.findById(id);
            
            if (journal.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", journal.get());
                
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Journal not found");
                
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/journals")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createJournal(Journal journal) {
        try {
            Journal createdJournal = journalService.create(journal);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", createdJournal);
            response.put("message", "Journal created successfully");
            
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @PUT
    @Path("/journals/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJournal(@PathParam("id") Long id, Journal journal) {
        try {
            Journal updatedJournal = journalService.update(id, journal);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updatedJournal);
            response.put("message", "Journal updated successfully");
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @DELETE
    @Path("/journals/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteJournal(@PathParam("id") Long id) {
        try {
            boolean deleted = journalService.delete(id);
            
            if (deleted) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Journal deleted successfully");
                
                return Response.ok(response).build();
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "Journal not found");
                
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(response)
                        .build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/journals/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJournalStats() {
        try {
            Map<String, Object> stats = journalService.getStats();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", stats);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/transactions/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionsByUserId(@PathParam("userId") Long userId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", transactions);
            response.put("count", transactions.size());
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/transactions/wallet/{walletId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionsByWalletId(@PathParam("walletId") Long walletId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByWalletId(walletId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", transactions);
            response.put("count", transactions.size());
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/users/{userId}/network")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserNetwork(@PathParam("userId") Long userId) {
        try {
            Map<String, Object> networkData = userService.getUserNetwork(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", networkData);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/test-db")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testDatabase() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Database connection test");
            response.put("timestamp", System.currentTimeMillis());
            
            // Try to get user count
            try {
                List<User> users = userService.findAll();
                response.put("userCount", users.size());
                response.put("users", users);
            } catch (Exception e) {
                response.put("userError", e.getMessage());
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/test-sql")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testSql() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "SQL test");
            
            // Test if users table exists
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SHOW TABLES LIKE 'users'");
                 ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    response.put("tableExists", true);
                    response.put("tableName", rs.getString(1));
                } else {
                    response.put("tableExists", false);
                }
            }
            
            // Test user count
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users");
                 ResultSet rs = stmt.executeQuery()) {
                
                if (rs.next()) {
                    response.put("userCount", rs.getInt(1));
                }
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/test-password")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testPassword() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Test password");
            
            // Test password hashing
            String password = "admin123";
            String expectedHash = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
            
            // Convert hex to base64
            byte[] hashBytes = hexStringToByteArray(expectedHash);
            String base64Hash = Base64.getEncoder().encodeToString(hashBytes);
            
            response.put("password", password);
            response.put("expectedHash", expectedHash);
            response.put("base64Hash", base64Hash);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }
    
    @GET
    @Path("/test-hash")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testHash() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Test hash");
            
            // Test password hashing for company@bigwater.com
            String password = "admin123";
            String companyHash = "VOSMvXWtjC0yN7NaFRyokd6OvU7G0qlYpREh4HTBulE=";
            String testHash = "jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=";
            
            String actualHash = passwordUtil.hashPassword(password);
            boolean companyValid = passwordUtil.verifyPassword(password, companyHash);
            boolean testValid = passwordUtil.verifyPassword(password, testHash);
            
            response.put("password", password);
            response.put("companyHash", companyHash);
            response.put("testHash", testHash);
            response.put("actualHash", actualHash);
            response.put("companyValid", companyValid);
            response.put("testValid", testValid);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/test-company-password")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testCompanyPassword() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Test company password");
            
            String companyHash = "VOSMvXWtjC0yN7NaFRyokd6OvU7G0qlYpREh4HTBulE=";
            
            // Test different passwords
            String[] passwords = {"admin123", "password", "123456", "admin", "company", "bigwater", "company123"};
            
            Map<String, Boolean> results = new HashMap<>();
            for (String password : passwords) {
                boolean isValid = passwordUtil.verifyPassword(password, companyHash);
                results.put(password, isValid);
            }
            
            response.put("companyHash", companyHash);
            response.put("results", results);
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/view-users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewUsers() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "View users");
            
            // Get all users from database
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT id, email, first_name, last_name, role, status, level FROM users");
                 ResultSet rs = stmt.executeQuery()) {
                
                List<Map<String, Object>> users = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("id", rs.getLong("id"));
                    user.put("email", rs.getString("email"));
                    user.put("firstName", rs.getString("first_name"));
                    user.put("lastName", rs.getString("last_name"));
                    user.put("role", rs.getString("role"));
                    user.put("status", rs.getString("status"));
                    user.put("level", rs.getString("level"));
                    users.add(user);
                }
                response.put("users", users);
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/view-passwords")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewPasswords() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "View passwords");
            
            // Get all users from database
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT id, email, password_hash FROM users");
                 ResultSet rs = stmt.executeQuery()) {
                
                List<Map<String, Object>> users = new ArrayList<>();
                while (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("id", rs.getLong("id"));
                    user.put("email", rs.getString("email"));
                    user.put("passwordHash", rs.getString("password_hash"));
                    users.add(user);
                }
                response.put("users", users);
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/fix-passwords")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fixPasswords() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Fix passwords");
            
            // Update test@test.com password hash
            String newHash = "jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=";
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("UPDATE users SET password_hash = ? WHERE email = ?")) {
                
                stmt.setString(1, newHash);
                stmt.setString(2, "test@test.com");
                int affectedRows = stmt.executeUpdate();
                response.put("updatedRows", affectedRows);
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }
    
    @GET
    @Path("/update-user-levels")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserLevels() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Update user levels");
            
            // Update existing users to have appropriate levels
            try (Connection conn = dataSource.getConnection()) {
                // All SUBSCRIBER users are regular users and can be any of the 5 levels
                // For now, set all SUBSCRIBER users to CHIEF level (default)
                try (PreparedStatement stmt = conn.prepareStatement("UPDATE users SET level = ? WHERE role = ?")) {
                    stmt.setString(1, "CHIEF");
                    stmt.setString(2, "SUBSCRIBER");
                    int affectedRows = stmt.executeUpdate();
                    response.put("subscriberUpdated", affectedRows);
                }
                
                // SUPER_ADMIN and ADMIN users keep their special levels for system management
                // SUPER_ADMIN → PRESIDENT (system administrator)
                try (PreparedStatement stmt = conn.prepareStatement("UPDATE users SET level = ? WHERE role = ?")) {
                    stmt.setString(1, "PRESIDENT");
                    stmt.setString(2, "SUPER_ADMIN");
                    int affectedRows = stmt.executeUpdate();
                    response.put("superAdminUpdated", affectedRows);
                }
                
                // ADMIN → MINISTER (system manager)
                try (PreparedStatement stmt = conn.prepareStatement("UPDATE users SET level = ? WHERE role = ?")) {
                    stmt.setString(1, "MINISTER");
                    stmt.setString(2, "ADMIN");
                    int affectedRows = stmt.executeUpdate();
                    response.put("adminUpdated", affectedRows);
                }
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/check-table-structure")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkTableStructure() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Check table structure");
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DESCRIBE users")) {
                
                ResultSet rs = stmt.executeQuery();
                List<Map<String, Object>> columns = new ArrayList<>();
                
                while (rs.next()) {
                    Map<String, Object> column = new HashMap<>();
                    column.put("field", rs.getString("Field"));
                    column.put("type", rs.getString("Type"));
                    column.put("null", rs.getString("Null"));
                    column.put("key", rs.getString("Key"));
                    column.put("default", rs.getString("Default"));
                    columns.add(column);
                }
                
                response.put("columns", columns);
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/add-level-column")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLevelColumn() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Add level column");
            
            try (Connection conn = dataSource.getConnection()) {
                // Add level column
                try (PreparedStatement stmt = conn.prepareStatement("ALTER TABLE users ADD COLUMN level VARCHAR(20) DEFAULT 'CHIEF'")) {
                    int affectedRows = stmt.executeUpdate();
                    response.put("levelColumnAdded", true);
                    response.put("affectedRows", affectedRows);
                } catch (Exception e) {
                    response.put("levelColumnAdded", false);
                    response.put("error", e.getMessage());
                }
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/check-user-level/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkUserLevel(@PathParam("id") Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Check user level");
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT id, email, first_name, last_name, role, status, level FROM users WHERE id = ?")) {
                
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("id", rs.getLong("id"));
                    user.put("email", rs.getString("email"));
                    user.put("firstName", rs.getString("first_name"));
                    user.put("lastName", rs.getString("last_name"));
                    user.put("role", rs.getString("role"));
                    user.put("status", rs.getString("status"));
                    user.put("level", rs.getString("level"));
                    response.put("user", user);
                } else {
                    response.put("user", null);
                    response.put("message", "User not found");
                }
            }
            
            return Response.ok(response).build();
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // Drawing endpoints
    @POST
    @Path("/drawings")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDrawing(Map<String, Object> drawingData) {
        try {
            String name = (String) drawingData.get("name");
            BigDecimal prizePool = new BigDecimal(drawingData.get("prizePool").toString());
            String drawDateStr = (String) drawingData.get("drawDate");
            String winningNumbers = (String) drawingData.get("winningNumbers");
            Integer powerBall = (Integer) drawingData.get("powerBall");
            
            // Parse draw date
            LocalDateTime drawDate = LocalDateTime.parse(drawDateStr);
            
            // Create drawing
            RandomDrawing drawing = new RandomDrawing();
            drawing.setDrawingName(name);
            drawing.setDrawingType(RandomDrawing.DrawingType.LOTTERY); // Default type
            drawing.setPrizePool(prizePool);
            drawing.setDrawingDate(drawDate);
            // Store winning numbers as JSON array
            String[] numbers = winningNumbers.split(",");
            String jsonNumbers = "[" + winningNumbers + "," + powerBall + "]";
            drawing.setWinningNumbers(jsonNumbers);
            drawing.setStatus(RandomDrawing.DrawingStatus.PENDING);
            drawing.setCreatedBy(1L); // TODO: Get from JWT token
            drawing.setCreatedAt(LocalDateTime.now());
            drawing.setUpdatedAt(LocalDateTime.now());
            
            // Save to database
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO random_drawings (drawing_name, drawing_type, week_number, drawing_date, prize_pool, winning_numbers, status, created_by, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
                
                stmt.setString(1, drawing.getDrawingName());
                stmt.setString(2, drawing.getDrawingType().name());
                stmt.setInt(3, 1); // Default week number
                stmt.setTimestamp(4, Timestamp.valueOf(drawing.getDrawingDate()));
                stmt.setBigDecimal(5, drawing.getPrizePool());
                stmt.setString(6, drawing.getWinningNumbers());
                stmt.setString(7, drawing.getStatus().name());
                stmt.setLong(8, drawing.getCreatedBy());
                stmt.setTimestamp(9, Timestamp.valueOf(drawing.getCreatedAt()));
                stmt.setTimestamp(10, Timestamp.valueOf(drawing.getUpdatedAt()));
                
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows > 0) {
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        drawing.setId(rs.getLong(1));
                    }
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", drawing);
            response.put("message", "Drawing created successfully");
            
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
                    
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @GET
    @Path("/drawings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDrawings() {
        try {
            List<RandomDrawing> drawings = new ArrayList<>();
            
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, drawing_name, drawing_type, drawing_date, prize_pool, winning_numbers, status, total_participants, created_by, created_at, updated_at " +
                     "FROM random_drawings ORDER BY created_at DESC")) {
                
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    RandomDrawing drawing = new RandomDrawing();
                    drawing.setId(rs.getLong("id"));
                    drawing.setDrawingName(rs.getString("drawing_name"));
                    drawing.setDrawingType(RandomDrawing.DrawingType.valueOf(rs.getString("drawing_type")));
                    drawing.setDrawingDate(rs.getTimestamp("drawing_date").toLocalDateTime());
                    drawing.setPrizePool(rs.getBigDecimal("prize_pool"));
                    drawing.setWinningNumbers(rs.getString("winning_numbers"));
                    drawing.setStatus(RandomDrawing.DrawingStatus.valueOf(rs.getString("status")));
                    drawing.setTotalParticipants(rs.getInt("total_participants"));
                    drawing.setCreatedBy(rs.getLong("created_by"));
                    drawing.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    drawing.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                    
                    drawings.add(drawing);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", drawings);
            response.put("count", drawings.size());
            
            return Response.ok(response).build();
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    @PUT
    @Path("/drawings/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDrawing(@PathParam("id") Long id, Map<String, Object> drawingData) {
        try {
            String name = (String) drawingData.get("name");
            BigDecimal prizePool = new BigDecimal(drawingData.get("prizePool").toString());
            String drawDateStr = (String) drawingData.get("drawDate");
            String winningNumbers = (String) drawingData.get("winningNumbers");
            Integer powerBall = (Integer) drawingData.get("powerBall");
            String status = (String) drawingData.get("status");
            
            // Parse draw date
            LocalDateTime drawDate = LocalDateTime.parse(drawDateStr);
            
            // Update drawing in database
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE random_drawings SET drawing_name = ?, prize_pool = ?, drawing_date = ?, winning_numbers = ?, status = ?, updated_at = ? WHERE id = ?")) {
                
                stmt.setString(1, name);
                stmt.setBigDecimal(2, prizePool);
                stmt.setTimestamp(3, Timestamp.valueOf(drawDate));
                stmt.setString(4, "[" + winningNumbers + "," + powerBall + "]");
                stmt.setString(5, status);
                stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setLong(7, id);
                
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows > 0) {
                    // Get updated drawing
                    try (PreparedStatement selectStmt = conn.prepareStatement(
                        "SELECT id, drawing_name, drawing_type, drawing_date, prize_pool, winning_numbers, status, total_participants, created_by, created_at, updated_at " +
                        "FROM random_drawings WHERE id = ?")) {
                        
                        selectStmt.setLong(1, id);
                        ResultSet rs = selectStmt.executeQuery();
                        
                        if (rs.next()) {
                            RandomDrawing drawing = new RandomDrawing();
                            drawing.setId(rs.getLong("id"));
                            drawing.setDrawingName(rs.getString("drawing_name"));
                            drawing.setDrawingType(RandomDrawing.DrawingType.valueOf(rs.getString("drawing_type")));
                            drawing.setDrawingDate(rs.getTimestamp("drawing_date").toLocalDateTime());
                            drawing.setPrizePool(rs.getBigDecimal("prize_pool"));
                            drawing.setWinningNumbers(rs.getString("winning_numbers"));
                            drawing.setStatus(RandomDrawing.DrawingStatus.valueOf(rs.getString("status")));
                            drawing.setTotalParticipants(rs.getInt("total_participants"));
                            drawing.setCreatedBy(rs.getLong("created_by"));
                            drawing.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                            drawing.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                            
                            Map<String, Object> response = new HashMap<>();
                            response.put("success", true);
                            response.put("data", drawing);
                            response.put("message", "Drawing updated successfully");
                            
                            return Response.ok(response).build();
                        }
                    }
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Drawing not found");
            
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(response)
                    .build();
                    
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
    }

    @POST
    @Path("/users/{userId}/add-downline")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDownline(@PathParam("userId") Long userId, Map<String, Object> downlineData) {
        try {
            String email = (String) downlineData.get("email");
            String firstName = (String) downlineData.get("firstName");
            String lastName = (String) downlineData.get("lastName");
            String phone = (String) downlineData.get("phone");
            String password = (String) downlineData.get("password");
            String referralCode = (String) downlineData.get("referralCode");
            
            if (email == null || email.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("success", false, "error", "Email is required"))
                        .build();
            }
            
            if (password == null || password.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("success", false, "error", "Password is required"))
                        .build();
            }
            
            // Check if email already exists
            if (userService.findByEmail(email).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(Map.of("success", false, "error", "User with this email already exists"))
                        .build();
            }
            
            // Validate referralCode from frontend
            if (referralCode == null || referralCode.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("success", false, "error", "Referral code is required"))
                        .build();
            }
            
            // Use the referralCode directly from frontend to boost performance
            // No need to query database for referrer user
            String referredByCode = referralCode;
            
            // Use the provided password
            String passwordHash = passwordUtil.hashPassword(password);
            
            // Create the new downline user
            User newDownline = userService.createUser(email, passwordHash, firstName, lastName, phone, referredByCode);
            
            // Set default values for new downline
            newDownline.setRole(User.UserRole.SUBSCRIBER);
            // Make new member ACTIVE by default
            newDownline.setStatus(User.UserStatus.ACTIVE);
            // Default new member to lowest displayed title until conditions met
            newDownline.setLevel(User.UserLevel.CHIEF); // stored enum unchanged; frontend maps weeks<24 to "Customer"
            
            // Update the user with all fields
            newDownline = userService.updateUser(newDownline);
            
            // TODO: Send invitation email
            // emailService.sendInvitation(email, firstName, referrer.getFullName());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", newDownline);
            response.put("message", "Downline added successfully");
            
            return Response.status(Response.Status.CREATED)
                    .entity(response)
                    .build();
                    
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .build();
        }
    }

    /**
     * Generate a temporary password for new users
     */
    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        // Generate 8-character password
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
}
