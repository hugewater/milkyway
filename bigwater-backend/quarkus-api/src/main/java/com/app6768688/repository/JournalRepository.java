package com.app6768688.repository;

import com.app6768688.model.Journal;
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
public class JournalRepository {

    @Inject
    AgroalDataSource dataSource;

    @Transactional
    public Journal create(Journal journal) {
        String sql = """
            INSERT INTO journals (week_number, title, excerpt, content, status, featured, 
                                tags, views, publish_date, created_by, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, journal.getWeekNumber());
            stmt.setString(2, journal.getTitle());
            stmt.setString(3, journal.getExcerpt());
            stmt.setString(4, journal.getContent());
            stmt.setString(5, journal.getStatus().name());
            stmt.setBoolean(6, journal.getFeatured());
            stmt.setString(7, journal.getTags());
            stmt.setInt(8, journal.getViews());
            stmt.setTimestamp(9, journal.getPublishDate() != null ? Timestamp.valueOf(journal.getPublishDate()) : null);
            stmt.setLong(10, journal.getCreatedBy());
            stmt.setTimestamp(11, Timestamp.valueOf(journal.getCreatedAt()));
            stmt.setTimestamp(12, Timestamp.valueOf(journal.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating journal failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    journal.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating journal failed, no ID obtained.");
                }
            }
            
            return journal;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating journal", e);
        }
    }

    public Optional<Journal> findById(Long id) {
        String sql = "SELECT * FROM journals WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToJournal(rs));
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding journal by ID", e);
        }
    }

    public Optional<Journal> findByWeekNumber(Integer weekNumber) {
        String sql = "SELECT * FROM journals WHERE week_number = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, weekNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToJournal(rs));
            }
            
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding journal by week number", e);
        }
    }

    public List<Journal> findAll() {
        String sql = "SELECT * FROM journals ORDER BY week_number DESC";
        List<Journal> journals = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                journals.add(mapResultSetToJournal(rs));
            }
            
            return journals;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all journals", e);
        }
    }

    public List<Journal> findByStatus(Journal.JournalStatus status) {
        String sql = "SELECT * FROM journals WHERE status = ? ORDER BY week_number DESC";
        List<Journal> journals = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                journals.add(mapResultSetToJournal(rs));
            }
            
            return journals;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding journals by status", e);
        }
    }

    public List<Journal> findPublished() {
        return findByStatus(Journal.JournalStatus.PUBLISHED);
    }

    public List<Journal> findFeatured() {
        String sql = "SELECT * FROM journals WHERE featured = true ORDER BY week_number DESC";
        List<Journal> journals = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                journals.add(mapResultSetToJournal(rs));
            }
            
            return journals;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding featured journals", e);
        }
    }

    public List<Journal> findByCreator(Long createdBy) {
        String sql = "SELECT * FROM journals WHERE created_by = ? ORDER BY week_number DESC";
        List<Journal> journals = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, createdBy);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                journals.add(mapResultSetToJournal(rs));
            }
            
            return journals;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding journals by creator", e);
        }
    }

    @Transactional
    public Journal update(Journal journal) {
        String sql = """
            UPDATE journals SET week_number = ?, title = ?, excerpt = ?, content = ?, 
                              status = ?, featured = ?, tags = ?, views = ?, publish_date = ?, 
                              updated_at = ?
            WHERE id = ?
            """;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, journal.getWeekNumber());
            stmt.setString(2, journal.getTitle());
            stmt.setString(3, journal.getExcerpt());
            stmt.setString(4, journal.getContent());
            stmt.setString(5, journal.getStatus().name());
            stmt.setBoolean(6, journal.getFeatured());
            stmt.setString(7, journal.getTags());
            stmt.setInt(8, journal.getViews());
            stmt.setTimestamp(9, journal.getPublishDate() != null ? Timestamp.valueOf(journal.getPublishDate()) : null);
            stmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(11, journal.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating journal failed, no rows affected.");
            }
            
            journal.setUpdatedAt(LocalDateTime.now());
            return journal;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating journal", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        String sql = "DELETE FROM journals WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting journal failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting journal", e);
        }
    }

    @Transactional
    public void incrementViews(Long id) {
        String sql = "UPDATE journals SET views = views + 1, updated_at = ? WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(2, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Incrementing journal views failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error incrementing journal views", e);
        }
    }

    @Transactional
    public Journal publishJournal(Long id) {
        Optional<Journal> journalOpt = findById(id);
        if (journalOpt.isPresent()) {
            Journal journal = journalOpt.get();
            journal.setStatus(Journal.JournalStatus.PUBLISHED);
            journal.setPublishDate(LocalDateTime.now());
            return update(journal);
        }
        throw new RuntimeException("Journal not found with ID: " + id);
    }

    public boolean existsByWeekNumber(Integer weekNumber) {
        String sql = "SELECT COUNT(*) FROM journals WHERE week_number = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, weekNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if journal exists by week number", e);
        }
    }

    public long getTotalJournalCount() {
        String sql = "SELECT COUNT(*) FROM journals";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total journal count", e);
        }
    }

    public long getPublishedJournalCount() {
        return findByStatus(Journal.JournalStatus.PUBLISHED).size();
    }

    public long getTotalViews() {
        String sql = "SELECT SUM(views) FROM journals";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total views", e);
        }
    }

    public Integer getNextWeekNumber() {
        String sql = "SELECT MAX(week_number) FROM journals";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                Integer maxWeek = rs.getObject(1, Integer.class);
                return maxWeek != null ? maxWeek + 1 : 1;
            }
            
            return 1;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting next week number", e);
        }
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
        journal.setCreatedBy(rs.getLong("created_by"));
        
        Timestamp publishDate = rs.getTimestamp("publish_date");
        if (publishDate != null) {
            journal.setPublishDate(publishDate.toLocalDateTime());
        }
        
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
