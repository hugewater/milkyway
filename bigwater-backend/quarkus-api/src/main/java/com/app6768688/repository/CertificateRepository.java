package com.app6768688.repository;

import com.app6768688.model.Certificate;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CertificateRepository {

    @Inject
    AgroalDataSource dataSource;

    @Transactional
    public Certificate create(Certificate certificate) {
        String sql = """
            INSERT INTO certificates (certificate_name, description, price_usdt, 
                                    duration_days, benefits, is_active, max_supply, current_supply, 
                                    created_by, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, certificate.getCertificateName());
            stmt.setString(2, certificate.getDescription());
            stmt.setBigDecimal(3, certificate.getPriceUsdt());
            stmt.setInt(4, certificate.getDurationDays());
            stmt.setString(5, certificate.getBenefits());
            stmt.setBoolean(6, certificate.getIsActive());
            stmt.setObject(7, certificate.getMaxSupply());
            stmt.setInt(8, certificate.getCurrentSupply());
            stmt.setLong(9, certificate.getCreatedBy());
            stmt.setTimestamp(10, Timestamp.valueOf(certificate.getCreatedAt()));
            stmt.setTimestamp(11, Timestamp.valueOf(certificate.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating certificate failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    certificate.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating certificate failed, no ID obtained.");
                }
            }
            
            return certificate;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating certificate", e);
        }
    }

    public Optional<Certificate> findById(Long id) {
        String sql = "SELECT * FROM certificates WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToCertificate(rs));
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding certificate by ID", e);
        }
    }

    public List<Certificate> findAll() {
        String sql = "SELECT * FROM certificates ORDER BY created_at DESC";
        List<Certificate> certificates = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                certificates.add(mapResultSetToCertificate(rs));
            }
            
            return certificates;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all certificates", e);
        }
    }



    public List<Certificate> findActive() {
        String sql = "SELECT * FROM certificates WHERE is_active = true ORDER BY price_usdt ASC";
        List<Certificate> certificates = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                certificates.add(mapResultSetToCertificate(rs));
            }
            
            return certificates;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding active certificates", e);
        }
    }

    public List<Certificate> findAvailable() {
        String sql = """
            SELECT * FROM certificates 
            WHERE is_active = true 
            AND (max_supply IS NULL OR current_supply < max_supply)
            ORDER BY price_usdt ASC
            """;
        List<Certificate> certificates = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                certificates.add(mapResultSetToCertificate(rs));
            }
            
            return certificates;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding available certificates", e);
        }
    }

    @Transactional
    public Certificate update(Certificate certificate) {
        String sql = """
            UPDATE certificates SET certificate_name = ?, description = ?,
                                  price_usdt = ?, duration_days = ?, benefits = ?, is_active = ?,
                                  max_supply = ?, current_supply = ?, updated_at = ?
            WHERE id = ?
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, certificate.getCertificateName());
            stmt.setString(2, certificate.getDescription());
            stmt.setBigDecimal(3, certificate.getPriceUsdt());
            stmt.setInt(4, certificate.getDurationDays());
            stmt.setString(5, certificate.getBenefits());
            stmt.setBoolean(6, certificate.getIsActive());
            stmt.setObject(7, certificate.getMaxSupply());
            stmt.setInt(8, certificate.getCurrentSupply());
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(10, certificate.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating certificate failed, no rows affected.");
            }
            
            certificate.setUpdatedAt(LocalDateTime.now());
            return certificate;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating certificate", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        String sql = "DELETE FROM certificates WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting certificate failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting certificate", e);
        }
    }

    @Transactional
    public void incrementSupply(Long id) {
        String sql = "UPDATE certificates SET current_supply = current_supply + 1, updated_at = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(2, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Incrementing certificate supply failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error incrementing certificate supply", e);
        }
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM certificates WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if certificate exists", e);
        }
    }

    private Certificate mapResultSetToCertificate(ResultSet rs) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(rs.getLong("id"));
        certificate.setCertificateName(rs.getString("certificate_name"));
        certificate.setDescription(rs.getString("description"));
        certificate.setPriceUsdt(rs.getBigDecimal("price_usdt"));
        certificate.setDurationDays(rs.getInt("duration_days"));
        certificate.setBenefits(rs.getString("benefits"));
        certificate.setIsActive(rs.getBoolean("is_active"));
        certificate.setMaxSupply(rs.getObject("max_supply", Integer.class));
        certificate.setCurrentSupply(rs.getInt("current_supply"));
        certificate.setCreatedBy(rs.getLong("created_by"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            certificate.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            certificate.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return certificate;
    }
}
