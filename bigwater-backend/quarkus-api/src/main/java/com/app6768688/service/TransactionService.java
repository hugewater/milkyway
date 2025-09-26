package com.app6768688.service;

import com.app6768688.model.Transaction;
import com.app6768688.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@ApplicationScoped
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;
    
    @Inject
    DataSource dataSource;
    
    @Inject
    UserService userService;

    @Transactional
    public Transaction createTransaction(Long userId, Long walletId, Transaction.TransactionType transactionType, 
                                       BigDecimal amount, String description) {
        Transaction transaction = new Transaction(userId, transactionType, amount, description, walletId);
        return transactionRepository.create(transaction);
    }

    @Transactional
    public Transaction createTransaction(Long userId, Long walletId, Transaction.TransactionType transactionType, 
                                       BigDecimal amount, String description, Long toWalletId) {
        Transaction transaction = new Transaction(userId, transactionType, amount, description, walletId, toWalletId);
        return transactionRepository.create(transaction);
    }

    @Transactional
    public Transaction createTransferTransaction(Long fromUserId, Long fromWalletId, Long toUserId, Long toWalletId, 
                                               BigDecimal amount, String description) {
        // Create withdrawal transaction for sender
        Transaction withdrawalTransaction = new Transaction(fromUserId, Transaction.TransactionType.WITHDRAWAL, 
                                                          amount, "Transfer to wallet " + toWalletId, fromWalletId);
        withdrawalTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transactionRepository.create(withdrawalTransaction);

        // Create deposit transaction for receiver
        Transaction depositTransaction = new Transaction(toUserId, Transaction.TransactionType.DEPOSIT, 
                                                       amount, "Transfer from wallet " + fromWalletId, toWalletId);
        depositTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        return transactionRepository.create(depositTransaction);
    }

    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByWalletId(Long walletId) {
        return transactionRepository.findByWalletId(walletId);
    }

    @Transactional
    public void updateTransactionStatus(Long transactionId, Transaction.TransactionStatus status) {
        System.out.println("TransactionService.updateTransactionStatus called: transactionId=" + transactionId + ", status=" + status);
        
        // Get current transaction status and details before updating
        Transaction.TransactionStatus oldStatus = null;
        Long userId = null;
        BigDecimal amountUsdt = null;
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT status, user_id, amount_usdt FROM transactions WHERE id = ?")) {
            stmt.setLong(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                oldStatus = Transaction.TransactionStatus.valueOf(rs.getString("status"));
                userId = rs.getLong("user_id");
                amountUsdt = rs.getBigDecimal("amount_usdt");
                System.out.println("Current transaction details: oldStatus=" + oldStatus + ", userId=" + userId + ", amountUsdt=" + amountUsdt);
            } else {
                System.err.println("No transaction found with ID: " + transactionId);
            }
        } catch (Exception e) {
            System.err.println("Failed to get transaction details: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Update transaction status
        System.out.println("Updating transaction status from " + oldStatus + " to " + status);
        transactionRepository.updateStatus(transactionId, status);
        
        // Update user's total pay based on status changes
        if (userId != null && amountUsdt != null) {
            try {
                if (status == Transaction.TransactionStatus.COMPLETED && oldStatus != Transaction.TransactionStatus.COMPLETED) {
                    // Status changed TO COMPLETED - add to total_pay
                    System.out.println("Status changed TO COMPLETED - adding " + amountUsdt + " to user " + userId + " total pay");
                    userService.updateTotalPay(userId, amountUsdt);
                    System.out.println("Successfully added " + amountUsdt + " to user " + userId + " total pay (status changed to COMPLETED)");
                } else if (oldStatus == Transaction.TransactionStatus.COMPLETED && status != Transaction.TransactionStatus.COMPLETED) {
                    // Status changed FROM COMPLETED - subtract from total_pay
                    System.out.println("Status changed FROM COMPLETED - subtracting " + amountUsdt + " from user " + userId + " total pay");
                    userService.updateTotalPay(userId, amountUsdt.negate());
                    System.out.println("Successfully subtracted " + amountUsdt + " from user " + userId + " total pay (status changed from COMPLETED)");
                } else {
                    System.out.println("No total_pay update needed: status=" + status + ", oldStatus=" + oldStatus);
                }
            } catch (Exception e) {
                // Log the error but don't fail the transaction status update
                System.err.println("Failed to update user total pay: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Cannot update total_pay: userId=" + userId + ", amountUsdt=" + amountUsdt);
        }
    }

    @Transactional
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.update(transaction);
    }

    @Transactional
    public void updateTransactionHash(Long transactionId, String transactionHash) {
        transactionRepository.updateTransactionHash(transactionId, transactionHash);
    }

    public List<Transaction> getPendingTransactions() {
        return transactionRepository.findByStatus(Transaction.TransactionStatus.PENDING);
    }

    public List<Transaction> getCompletedTransactions() {
        return transactionRepository.findByStatus(Transaction.TransactionStatus.COMPLETED);
    }
}
