package com.app6768688.service;

import com.app6768688.model.Journal;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@ApplicationScoped
public class JournalService {

    @Inject
    DataSource dataSource;

    @Transactional
    public List<Journal> findAll() {
        List<Journal> journals = new ArrayList<>();
        String sql = "SELECT * FROM journals ORDER BY week_number DESC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                journals.add(mapResultSetToJournal(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching journals", e);
        }
        
        return journals;
    }

    @Transactional
    public Optional<Journal> findById(Long id) {
        String sql = "SELECT * FROM journals WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToJournal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching journal by id: " + id, e);
        }
        
        return Optional.empty();
    }

    @Transactional
    public List<Journal> findByStatus(Journal.JournalStatus status) {
        List<Journal> journals = new ArrayList<>();
        String sql = "SELECT * FROM journals WHERE status = ? ORDER BY week_number DESC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    journals.add(mapResultSetToJournal(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching journals by status", e);
        }
        
        return journals;
    }

    @Transactional
    public Journal create(Journal journal) {
        String sql = "INSERT INTO journals (week_number, title, excerpt, content, status, featured, tags, views, publish_date, created_by, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            journal.setCreatedAt(LocalDateTime.now());
            journal.setUpdatedAt(LocalDateTime.now());
            if (journal.getStatus() == null) {
                journal.setStatus(Journal.JournalStatus.DRAFT);
            }
            if (journal.getFeatured() == null) {
                journal.setFeatured(false);
            }
            if (journal.getViews() == null) {
                journal.setViews(0);
            }
            
            stmt.setInt(1, journal.getWeekNumber());
            stmt.setString(2, journal.getTitle());
            stmt.setString(3, journal.getExcerpt());
            stmt.setString(4, journal.getContent());
            stmt.setString(5, journal.getStatus().name());
            stmt.setBoolean(6, journal.getFeatured());
            stmt.setString(7, journal.getTags());
            stmt.setInt(8, journal.getViews());
            stmt.setTimestamp(9, journal.getPublishDate() != null ? Timestamp.valueOf(journal.getPublishDate()) : null);
            stmt.setLong(10, journal.getCreatedBy() != null ? journal.getCreatedBy() : 1L);
            stmt.setTimestamp(11, Timestamp.valueOf(journal.getCreatedAt()));
            stmt.setTimestamp(12, Timestamp.valueOf(journal.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating journal failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    journal.setId(generatedKeys.getLong(1));
                } else {
                    throw new RuntimeException("Creating journal failed, no ID obtained.");
                }
            }
            
            return journal;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating journal", e);
        }
    }

    @Transactional
    public Journal update(Long id, Journal journalUpdate) {
        String sql = "UPDATE journals SET week_number = ?, title = ?, excerpt = ?, content = ?, status = ?, featured = ?, tags = ?, publish_date = ?, updated_at = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, journalUpdate.getWeekNumber());
            stmt.setString(2, journalUpdate.getTitle());
            stmt.setString(3, journalUpdate.getExcerpt());
            stmt.setString(4, journalUpdate.getContent());
            stmt.setString(5, journalUpdate.getStatus().name());
            stmt.setBoolean(6, journalUpdate.getFeatured());
            stmt.setString(7, journalUpdate.getTags());
            stmt.setTimestamp(8, journalUpdate.getPublishDate() != null ? Timestamp.valueOf(journalUpdate.getPublishDate()) : null);
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(10, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Journal not found with id: " + id);
            }
            
            return findById(id).orElseThrow(() -> new RuntimeException("Journal not found after update"));
        } catch (SQLException e) {
            throw new RuntimeException("Error updating journal", e);
        }
    }

    @Transactional
    public boolean delete(Long id) {
        String sql = "DELETE FROM journals WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting journal", e);
        }
    }

    @Transactional
    public void incrementViews(Long id) {
        String sql = "UPDATE journals SET views = views + 1 WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error incrementing views", e);
        }
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        try (Connection conn = dataSource.getConnection()) {
            // Total journals
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM journals");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalJournals", rs.getLong(1));
                }
            }
            
            // Published journals
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM journals WHERE status = ?")) {
                stmt.setString(1, Journal.JournalStatus.PUBLISHED.name());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.put("publishedJournals", rs.getLong(1));
                    }
                }
            }
            
            // Draft journals
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM journals WHERE status = ?")) {
                stmt.setString(1, Journal.JournalStatus.DRAFT.name());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        stats.put("draftJournals", rs.getLong(1));
                    }
                }
            }
            
            // Total views
            try (PreparedStatement stmt = conn.prepareStatement("SELECT SUM(views) FROM journals");
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long totalViews = rs.getLong(1);
                    stats.put("totalViews", totalViews != 0 ? totalViews : 0);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting journal stats", e);
        }
        
        return stats;
    }

    private Journal mapResultSetToJournal(ResultSet rs) throws SQLException {
        Journal journal = new Journal();
        journal.setId(rs.getLong("id"));
        journal.setWeekNumber(rs.getInt("week_number"));
        journal.setTitle(rs.getString("title"));
        journal.setExcerpt(rs.getString("excerpt"));
        journal.setContent(rs.getString("content"));
        journal.setStatus(Journal.JournalStatus.valueOf(rs.getString("status")));
        journal.setFeatured(rs.getBoolean("featured"));
        journal.setTags(rs.getString("tags"));
        journal.setViews(rs.getInt("views"));
        
        Timestamp publishDate = rs.getTimestamp("publish_date");
        if (publishDate != null) {
            journal.setPublishDate(publishDate.toLocalDateTime());
        }
        
        journal.setCreatedBy(rs.getLong("created_by"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            journal.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            journal.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return journal;
    }
}
