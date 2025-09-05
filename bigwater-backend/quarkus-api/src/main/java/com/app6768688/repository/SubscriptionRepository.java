package com.app6768688.repository;

import com.app6768688.model.Subscription;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SubscriptionRepository {
    
    @Inject
    EntityManager entityManager;
    
    @Transactional
    public Subscription create(Subscription subscription) {
        entityManager.persist(subscription);
        entityManager.flush();
        return subscription;
    }
    
    @Transactional
    public Subscription update(Subscription subscription) {
        subscription.setUpdatedAt(java.time.LocalDateTime.now());
        return entityManager.merge(subscription);
    }
    
    @Transactional
    public void delete(Long id) {
        Subscription subscription = entityManager.find(Subscription.class, id);
        if (subscription != null) {
            entityManager.remove(subscription);
        }
    }
    
    public Optional<Subscription> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Subscription.class, id));
    }
    
    public List<Subscription> findByUserId(Long userId) {
        TypedQuery<Subscription> query = entityManager.createQuery(
            "SELECT s FROM Subscription s WHERE s.userId = :userId ORDER BY s.createdAt DESC",
            Subscription.class
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    public Optional<Subscription> findActiveByUserId(Long userId) {
        TypedQuery<Subscription> query = entityManager.createQuery(
            "SELECT s FROM Subscription s WHERE s.userId = :userId AND s.status = 'ACTIVE' " +
            "AND s.fromDate <= :today AND s.toDate >= :today ORDER BY s.createdAt DESC",
            Subscription.class
        );
        query.setParameter("userId", userId);
        query.setParameter("today", LocalDate.now());
        
        List<Subscription> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
    
    public List<Subscription> findExpiredSubscriptions() {
        TypedQuery<Subscription> query = entityManager.createQuery(
            "SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' AND s.toDate < :today",
            Subscription.class
        );
        query.setParameter("today", LocalDate.now());
        return query.getResultList();
    }
    
    public List<Subscription> findExpiringSoon(int days) {
        LocalDate futureDate = LocalDate.now().plusDays(days);
        TypedQuery<Subscription> query = entityManager.createQuery(
            "SELECT s FROM Subscription s WHERE s.status = 'ACTIVE' " +
            "AND s.toDate BETWEEN :today AND :futureDate ORDER BY s.toDate ASC",
            Subscription.class
        );
        query.setParameter("today", LocalDate.now());
        query.setParameter("futureDate", futureDate);
        return query.getResultList();
    }
    
    public List<Subscription> findAll() {
        TypedQuery<Subscription> query = entityManager.createQuery(
            "SELECT s FROM Subscription s ORDER BY s.createdAt DESC",
            Subscription.class
        );
        return query.getResultList();
    }
    
    public List<Subscription> findByStatus(Subscription.SubscriptionStatus status) {
        TypedQuery<Subscription> query = entityManager.createQuery(
            "SELECT s FROM Subscription s WHERE s.status = :status ORDER BY s.createdAt DESC",
            Subscription.class
        );
        query.setParameter("status", status);
        return query.getResultList();
    }
    
    public long countByUserId(Long userId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(s) FROM Subscription s WHERE s.userId = :userId",
            Long.class
        );
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }
}

