package com.app6768688.repository;

import com.app6768688.model.UsdtWallet;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class WalletRepository {

    @Inject
    AgroalDataSource dataSource;

    @Transactional
    public UsdtWallet create(UsdtWallet wallet) {
        String sql = """
            INSERT INTO usdt_wallets (user_id, wallet_address, wallet_name, wallet_type, 
                                    balance, is_active, is_verified, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, wallet.getUserId());
            stmt.setString(2, wallet.getWalletAddress());
            
            // Handle nullable fields
            String walletName = wallet.getWalletName();
            if (walletName != null) {
                stmt.setString(3, walletName);
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            
            UsdtWallet.WalletType walletType = wallet.getWalletType();
            if (walletType != null) {
                stmt.setString(4, walletType.name());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            BigDecimal balance = wallet.getBalance();
            if (balance != null) {
                stmt.setBigDecimal(5, balance);
            } else {
                stmt.setNull(5, Types.DECIMAL);
            }
            
            Boolean isActive = wallet.getIsActive();
            if (isActive != null) {
                stmt.setBoolean(6, isActive);
            } else {
                stmt.setNull(6, Types.BOOLEAN);
            }
            
            Boolean isVerified = wallet.getIsVerified();
            if (isVerified != null) {
                stmt.setBoolean(7, isVerified);
            } else {
                stmt.setNull(7, Types.BOOLEAN);
            }
            
            stmt.setTimestamp(8, Timestamp.valueOf(wallet.getCreatedAt()));
            stmt.setTimestamp(9, Timestamp.valueOf(wallet.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating wallet failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    wallet.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating wallet failed, no ID obtained.");
                }
            }
            
            return wallet;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating wallet", e);
        }
    }

    public Optional<UsdtWallet> findById(Long id) {
        String sql = "SELECT * FROM usdt_wallets WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToWallet(rs));
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding wallet by ID", e);
        }
    }

    public List<UsdtWallet> findByUserId(Long userId) {
        String sql = "SELECT * FROM usdt_wallets WHERE user_id = ? ORDER BY created_at DESC";
        List<UsdtWallet> wallets = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                wallets.add(mapResultSetToWallet(rs));
            }
            
            return wallets;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding wallets by user ID", e);
        }
    }

    public Optional<UsdtWallet> findByAddress(String walletAddress) {
        String sql = "SELECT * FROM usdt_wallets WHERE wallet_address = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, walletAddress);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToWallet(rs));
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding wallet by address", e);
        }
    }

    public List<UsdtWallet> findByType(UsdtWallet.WalletType walletType) {
        String sql = "SELECT * FROM usdt_wallets WHERE wallet_type = ? ORDER BY created_at DESC";
        List<UsdtWallet> wallets = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, walletType.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                wallets.add(mapResultSetToWallet(rs));
            }
            
            return wallets;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding wallets by type", e);
        }
    }

    public List<UsdtWallet> findActive() {
        String sql = "SELECT * FROM usdt_wallets WHERE is_active = true ORDER BY created_at DESC";
        List<UsdtWallet> wallets = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                wallets.add(mapResultSetToWallet(rs));
            }
            
            return wallets;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding active wallets", e);
        }
    }

    public List<UsdtWallet> findVerified() {
        String sql = "SELECT * FROM usdt_wallets WHERE is_verified = true ORDER BY created_at DESC";
        List<UsdtWallet> wallets = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                wallets.add(mapResultSetToWallet(rs));
            }
            
            return wallets;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding verified wallets", e);
        }
    }

    public List<UsdtWallet> findAll() {
        String sql = "SELECT * FROM usdt_wallets ORDER BY created_at DESC";
        List<UsdtWallet> wallets = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                wallets.add(mapResultSetToWallet(rs));
            }
            return wallets;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all wallets", e);
        }
    }

    public static class WalletQuery {
        public Integer offset;
        public Integer limit;
        public String sortBy; // id, created_at, balance
        public String order;  // asc/desc
        public String type;   // wallet_type
        public Boolean active; // is_active
        public String q;      // wallet_name/address like
    }

    public long countByQuery(WalletQuery q) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM usdt_wallets WHERE 1=1");
        List<Object> params = new ArrayList<>();
        buildWhere(sql, params, q);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = prepare(conn, sql.toString(), params);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error counting wallets", e);
        }
    }

    public List<UsdtWallet> findByQuery(WalletQuery q) {
        StringBuilder sql = new StringBuilder("SELECT * FROM usdt_wallets WHERE 1=1");
        List<Object> params = new ArrayList<>();
        buildWhere(sql, params, q);
        // sorting
        String sortCol = switch (q.sortBy != null ? q.sortBy : "created_at") {
            case "id" -> "id";
            case "balance" -> "balance";
            case "created_at" -> "created_at";
            default -> "created_at";
        };
        String order = (q.order != null && q.order.equalsIgnoreCase("asc")) ? "ASC" : "DESC";
        sql.append(" ORDER BY ").append(sortCol).append(" ").append(order);
        // paging
        int limit = q.limit != null ? Math.max(1, Math.min(q.limit, 200)) : 50;
        int offset = q.offset != null ? Math.max(0, q.offset) : 0;
        sql.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        List<UsdtWallet> wallets = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = prepare(conn, sql.toString(), params);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) wallets.add(mapResultSetToWallet(rs));
            return wallets;
        } catch (SQLException e) {
            throw new RuntimeException("Error querying wallets", e);
        }
    }

    private void buildWhere(StringBuilder sql, List<Object> params, WalletQuery q) {
        if (q == null) return;
        if (q.type != null && !q.type.isBlank()) {
            sql.append(" AND wallet_type = ?");
            params.add(q.type);
        }
        if (q.active != null) {
            sql.append(" AND is_active = ?");
            params.add(q.active);
        }
        if (q.q != null && !q.q.isBlank()) {
            sql.append(" AND (wallet_name LIKE ? OR wallet_address LIKE ?)");
            String like = "%" + q.q.trim() + "%";
            params.add(like);
            params.add(like);
        }
    }

    private PreparedStatement prepare(Connection conn, String sql, List<Object> params) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.size(); i++) {
            Object p = params.get(i);
            if (p instanceof String s) stmt.setString(i + 1, s);
            else if (p instanceof Integer n) stmt.setInt(i + 1, n);
            else if (p instanceof Long l) stmt.setLong(i + 1, l);
            else if (p instanceof Boolean b) stmt.setBoolean(i + 1, b);
            else stmt.setObject(i + 1, p);
        }
        return stmt;
    }

    @Transactional
    public UsdtWallet update(UsdtWallet wallet) {
        String sql = """
            UPDATE usdt_wallets SET user_id = ?, wallet_address = ?, wallet_name = ?, 
                                  wallet_type = ?, balance = ?, is_active = ?, is_verified = ?, 
                                  updated_at = ?
            WHERE id = ?
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, wallet.getUserId());
            stmt.setString(2, wallet.getWalletAddress());
            // wallet_name (nullable)
            if (wallet.getWalletName() != null) {
                stmt.setString(3, wallet.getWalletName());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            // wallet_type (nullable)
            if (wallet.getWalletType() != null) {
                stmt.setString(4, wallet.getWalletType().name());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            // balance (nullable)
            if (wallet.getBalance() != null) {
                stmt.setBigDecimal(5, wallet.getBalance());
            } else {
                stmt.setNull(5, Types.DECIMAL);
            }
            // is_active (nullable)
            if (wallet.getIsActive() != null) {
                stmt.setBoolean(6, wallet.getIsActive());
            } else {
                stmt.setNull(6, Types.BOOLEAN);
            }
            // is_verified (nullable)
            if (wallet.getIsVerified() != null) {
                stmt.setBoolean(7, wallet.getIsVerified());
            } else {
                stmt.setNull(7, Types.BOOLEAN);
            }
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(9, wallet.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating wallet failed, no rows affected.");
            }
            
            wallet.setUpdatedAt(LocalDateTime.now());
            return wallet;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating wallet", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        String sql = "DELETE FROM usdt_wallets WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting wallet failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting wallet", e);
        }
    }

    @Transactional
    public void updateBalance(Long walletId, BigDecimal newBalance) {
        String sql = "UPDATE usdt_wallets SET balance = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, newBalance);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(3, walletId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating wallet balance failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating wallet balance", e);
        }
    }

    @Transactional
    public void addBalance(Long walletId, BigDecimal amount) {
        String sql = "UPDATE usdt_wallets SET balance = balance + ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, amount);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(3, walletId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding wallet balance failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding wallet balance", e);
        }
    }

    @Transactional
    public void subtractBalance(Long walletId, BigDecimal amount) {
        String sql = "UPDATE usdt_wallets SET balance = balance - ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, amount);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(3, walletId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Subtracting wallet balance failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error subtracting wallet balance", e);
        }
    }

    @Transactional
    public void verifyWallet(Long walletId) {
        String sql = "UPDATE usdt_wallets SET is_verified = true, updated_at = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(2, walletId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Verifying wallet failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verifying wallet", e);
        }
    }

    public boolean existsByAddress(String walletAddress) {
        String sql = "SELECT COUNT(*) FROM usdt_wallets WHERE wallet_address = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, walletAddress);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if wallet exists by address", e);
        }
    }

    public long countByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM usdt_wallets WHERE user_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error counting wallets by user ID", e);
        }
    }

    public BigDecimal getTotalBalanceByUserId(Long userId) {
        String sql = "SELECT SUM(balance) FROM usdt_wallets WHERE user_id = ? AND is_active = true";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                return total != null ? total : BigDecimal.ZERO;
            }
            
            return BigDecimal.ZERO;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total balance by user ID", e);
        }
    }

    private UsdtWallet mapResultSetToWallet(ResultSet rs) throws SQLException {
        UsdtWallet wallet = new UsdtWallet();
        wallet.setId(rs.getLong("id"));
        wallet.setUserId(rs.getLong("user_id"));
        wallet.setWalletAddress(rs.getString("wallet_address"));
        
        // Handle nullable fields
        String walletName = rs.getString("wallet_name");
        wallet.setWalletName(walletName);
        
        String walletTypeStr = rs.getString("wallet_type");
        if (walletTypeStr != null) {
            wallet.setWalletType(UsdtWallet.WalletType.valueOf(walletTypeStr));
        } else {
            wallet.setWalletType(UsdtWallet.WalletType.MAIN); // Default value
        }
        
        BigDecimal balance = rs.getBigDecimal("balance");
        wallet.setBalance(balance != null ? balance : BigDecimal.ZERO);
        
        Boolean isActive = rs.getBoolean("is_active");
        if (rs.wasNull()) {
            isActive = true; // Default value
        }
        wallet.setIsActive(isActive);
        
        Boolean isVerified = rs.getBoolean("is_verified");
        if (rs.wasNull()) {
            isVerified = false; // Default value
        }
        wallet.setIsVerified(isVerified);
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            wallet.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            wallet.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return wallet;
    }
}
