package com.app6768688.repository;

import com.app6768688.model.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TransactionRepository {

    @Inject
    EntityManager entityManager;

    @Transactional
    public Transaction create(Transaction transaction) {
        entityManager.persist(transaction);
        return transaction;
    }

    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Transaction.class, id));
    }

    public List<Transaction> findByUserId(Long userId) {
        TypedQuery<Transaction> query = entityManager.createQuery(
            "SELECT t FROM Transaction t WHERE t.userId = :userId ORDER BY t.createdAt DESC", 
            Transaction.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public List<Transaction> findByWalletId(Long walletId) {
        TypedQuery<Transaction> query = entityManager.createQuery(
            "SELECT t FROM Transaction t WHERE t.walletId = :walletId ORDER BY t.createdAt DESC", 
            Transaction.class);
        query.setParameter("walletId", walletId);
        return query.getResultList();
    }

    public List<Transaction> findByStatus(Transaction.TransactionStatus status) {
        TypedQuery<Transaction> query = entityManager.createQuery(
            "SELECT t FROM Transaction t WHERE t.status = :status ORDER BY t.createdAt DESC", 
            Transaction.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<Transaction> findByType(Transaction.TransactionType type) {
        TypedQuery<Transaction> query = entityManager.createQuery(
            "SELECT t FROM Transaction t WHERE t.transactionType = :type ORDER BY t.createdAt DESC", 
            Transaction.class);
        query.setParameter("type", type);
        return query.getResultList();
    }

    @Transactional
    public Transaction update(Transaction transaction) {
        return entityManager.merge(transaction);
    }

    @Transactional
    public void updateStatus(Long transactionId, Transaction.TransactionStatus status) {
        Optional<Transaction> transactionOpt = findById(transactionId);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            transaction.setStatus(status);
            transaction.setUpdatedAt(LocalDateTime.now());
            update(transaction);
        }
    }

    @Transactional
    public void updateTransactionHash(Long transactionId, String transactionHash) {
        Optional<Transaction> transactionOpt = findById(transactionId);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            transaction.setTransactionHash(transactionHash);
            transaction.setUpdatedAt(LocalDateTime.now());
            update(transaction);
        }
    }

    @Transactional
    public void delete(Long id) {
        Transaction transaction = entityManager.find(Transaction.class, id);
        if (transaction != null) {
            entityManager.remove(transaction);
        }
    }

    public List<Transaction> findAll() {
        TypedQuery<Transaction> query = entityManager.createQuery(
            "SELECT t FROM Transaction t ORDER BY t.createdAt DESC", 
            Transaction.class);
        return query.getResultList();
    }

    public long countByUserId(Long userId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(t) FROM Transaction t WHERE t.userId = :userId", 
            Long.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public long countByWalletId(Long walletId) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(t) FROM Transaction t WHERE t.walletId = :walletId", 
            Long.class);
        query.setParameter("walletId", walletId);
        return query.getSingleResult();
    }
}
