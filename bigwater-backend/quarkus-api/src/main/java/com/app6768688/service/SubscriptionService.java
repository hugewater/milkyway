package com.app6768688.service;

import com.app6768688.model.Subscription;
import com.app6768688.repository.SubscriptionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SubscriptionService {
    
    @Inject
    SubscriptionRepository subscriptionRepository;
    
    @Transactional
    public Subscription createSubscription(Long userId, BigDecimal payment, 
                                         LocalDate fromDate, LocalDate toDate, 
                                         LocalDate paymentDate, String description) {
        // Check if user already has an active subscription
        Optional<Subscription> existingActive = subscriptionRepository.findActiveByUserId(userId);
        if (existingActive.isPresent()) {
            throw new IllegalArgumentException("User already has an active subscription");
        }
        
        Subscription subscription = new Subscription(userId, payment, fromDate, toDate, paymentDate, description);
        return subscriptionRepository.create(subscription);
    }
    
    @Transactional
    public Subscription updateSubscription(Long id, BigDecimal payment, 
                                         LocalDate fromDate, LocalDate toDate, 
                                         LocalDate paymentDate, String description) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(id);
        if (subscriptionOpt.isEmpty()) {
            throw new IllegalArgumentException("Subscription not found");
        }
        
        Subscription subscription = subscriptionOpt.get();
        subscription.setPayment(payment);
        subscription.setFromDate(fromDate);
        subscription.setToDate(toDate);
        subscription.setPaymentDate(paymentDate);
        subscription.setDescription(description);
        
        return subscriptionRepository.update(subscription);
    }
    
    @Transactional
    public void cancelSubscription(Long id) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(id);
        if (subscriptionOpt.isEmpty()) {
            throw new IllegalArgumentException("Subscription not found");
        }
        
        Subscription subscription = subscriptionOpt.get();
        subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);
        subscriptionRepository.update(subscription);
    }
    
    @Transactional
    public void expireSubscription(Long id) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(id);
        if (subscriptionOpt.isEmpty()) {
            throw new IllegalArgumentException("Subscription not found");
        }
        
        Subscription subscription = subscriptionOpt.get();
        subscription.setStatus(Subscription.SubscriptionStatus.EXPIRED);
        subscriptionRepository.update(subscription);
    }
    
    @Transactional
    public void processExpiredSubscriptions() {
        List<Subscription> expiredSubscriptions = subscriptionRepository.findExpiredSubscriptions();
        for (Subscription subscription : expiredSubscriptions) {
            subscription.setStatus(Subscription.SubscriptionStatus.EXPIRED);
            subscriptionRepository.update(subscription);
        }
    }
    
    public Optional<Subscription> getActiveSubscription(Long userId) {
        return subscriptionRepository.findActiveByUserId(userId);
    }
    
    public List<Subscription> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }
    
    public List<Subscription> getExpiringSoon(int days) {
        return subscriptionRepository.findExpiringSoon(days);
    }
    
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }
    
    public List<Subscription> getSubscriptionsByStatus(Subscription.SubscriptionStatus status) {
        return subscriptionRepository.findByStatus(status);
    }
    
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }
    
    public boolean hasActiveSubscription(Long userId) {
        return subscriptionRepository.findActiveByUserId(userId).isPresent();
    }
    
    public long getSubscriptionCount(Long userId) {
        return subscriptionRepository.countByUserId(userId);
    }
    
    public boolean isSubscriptionValid(Long userId) {
        Optional<Subscription> activeSubscription = subscriptionRepository.findActiveByUserId(userId);
        return activeSubscription.isPresent() && activeSubscription.get().isActive();
    }
    
    public long getDaysRemaining(Long userId) {
        Optional<Subscription> activeSubscription = subscriptionRepository.findActiveByUserId(userId);
        if (activeSubscription.isPresent()) {
            return activeSubscription.get().getDaysRemaining();
        }
        return 0;
    }
}

