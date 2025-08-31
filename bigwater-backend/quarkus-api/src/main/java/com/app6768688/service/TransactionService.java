package com.app6768688.service;

import com.app6768688.model.Transaction;
import com.app6768688.repository.TransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class TransactionService {

    @Inject
    TransactionRepository transactionRepository;

    @Transactional
    public Transaction createTransaction(Long userId, Long walletId, Transaction.TransactionType transactionType, 
                                       BigDecimal amount, String description) {
        Transaction transaction = new Transaction(userId, transactionType, amount, description, walletId);
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
        transactionRepository.updateStatus(transactionId, status);
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
