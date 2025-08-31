package com.app6768688.service;

import com.app6768688.model.Certificate;
import com.app6768688.repository.CertificateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CertificateService {

    @Inject
    CertificateRepository certificateRepository;

    @Transactional
    public Certificate createCertificate(String certificateName, String description, BigDecimal priceUsdt, 
                                       Integer durationDays, String benefits, Integer maxSupply, Long createdBy) {
        
        Certificate certificate = new Certificate(certificateName, description, priceUsdt, durationDays, createdBy);
        certificate.setBenefits(benefits);
        certificate.setMaxSupply(maxSupply);
        
        return certificateRepository.create(certificate);
    }

    @Transactional
    public Certificate createCertificate(String certificateName, String description, BigDecimal priceUsdt, 
                                       Integer durationDays, String benefits, Integer maxSupply, Long createdBy, Boolean isActive) {
        
        Certificate certificate = new Certificate(certificateName, description, priceUsdt, durationDays, createdBy);
        certificate.setBenefits(benefits);
        certificate.setMaxSupply(maxSupply);
        if (isActive != null) {
            certificate.setIsActive(isActive);
        }
        
        return certificateRepository.create(certificate);
    }

    public Optional<Certificate> findById(Long id) {
        return certificateRepository.findById(id);
    }

    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }



    public List<Certificate> findActive() {
        return certificateRepository.findActive();
    }

    public List<Certificate> findAvailable() {
        return certificateRepository.findAvailable();
    }

    @Transactional
    public Certificate updateCertificate(Certificate certificate) {
        return certificateRepository.update(certificate);
    }

    @Transactional
    public void deleteCertificate(Long id) {
        certificateRepository.delete(id);
    }

    @Transactional
    public Certificate updateCertificateStatus(Long id, boolean isActive) {
        Optional<Certificate> certOpt = certificateRepository.findById(id);
        if (certOpt.isPresent()) {
            Certificate certificate = certOpt.get();
            certificate.setIsActive(isActive);
            return certificateRepository.update(certificate);
        }
        throw new RuntimeException("Certificate not found with ID: " + id);
    }

    @Transactional
    public Certificate updateCertificatePrice(Long id, BigDecimal newPrice) {
        Optional<Certificate> certOpt = certificateRepository.findById(id);
        if (certOpt.isPresent()) {
            Certificate certificate = certOpt.get();
            certificate.setPriceUsdt(newPrice);
            return certificateRepository.update(certificate);
        }
        throw new RuntimeException("Certificate not found with ID: " + id);
    }

    @Transactional
    public Certificate updateCertificateSupply(Long id, Integer maxSupply) {
        Optional<Certificate> certOpt = certificateRepository.findById(id);
        if (certOpt.isPresent()) {
            Certificate certificate = certOpt.get();
            certificate.setMaxSupply(maxSupply);
            return certificateRepository.update(certificate);
        }
        throw new RuntimeException("Certificate not found with ID: " + id);
    }

    @Transactional
    public void incrementCertificateSupply(Long id) {
        Optional<Certificate> certOpt = certificateRepository.findById(id);
        if (certOpt.isPresent()) {
            Certificate certificate = certOpt.get();
            if (!certificate.isAvailable()) {
                throw new RuntimeException("Certificate is not available for purchase");
            }
            certificateRepository.incrementSupply(id);
        } else {
            throw new RuntimeException("Certificate not found with ID: " + id);
        }
    }

    public boolean isCertificateAvailable(Long id) {
        Optional<Certificate> certOpt = certificateRepository.findById(id);
        return certOpt.map(Certificate::isAvailable).orElse(false);
    }

    public boolean canPurchaseCertificate(Long id) {
        Optional<Certificate> certOpt = certificateRepository.findById(id);
        if (certOpt.isPresent()) {
            Certificate certificate = certOpt.get();
            return certificate.isAvailable();
        }
        return false;
    }

    public List<Certificate> findCertificatesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return certificateRepository.findActive().stream()
                .filter(cert -> {
                    BigDecimal price = cert.getPriceUsdt();
                    return price != null && 
                           price.compareTo(minPrice) >= 0 && 
                           price.compareTo(maxPrice) <= 0;
                })
                .toList();
    }

    public List<Certificate> findCertificatesByDuration(Integer minDays, Integer maxDays) {
        return certificateRepository.findActive().stream()
                .filter(cert -> {
                    Integer duration = cert.getDurationDays();
                    return duration != null && 
                           duration >= minDays && 
                           duration <= maxDays;
                })
                .toList();
    }

    public long getTotalCertificateCount() {
        return certificateRepository.findAll().size();
    }

    public long getActiveCertificateCount() {
        return certificateRepository.findActive().size();
    }

    public long getAvailableCertificateCount() {
        return certificateRepository.findAvailable().size();
    }

    public BigDecimal getTotalValueOfAllCertificates() {
        return certificateRepository.findAll().stream()
                .map(Certificate::getPriceUsdt)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getAverageCertificatePrice() {
        List<Certificate> certificates = certificateRepository.findActive();
        if (certificates.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalPrice = certificates.stream()
                .map(Certificate::getPriceUsdt)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return totalPrice.divide(BigDecimal.valueOf(certificates.size()), 2, BigDecimal.ROUND_HALF_UP);
    }



    public List<Certificate> getCertificatesWithLowSupply(int threshold) {
        return certificateRepository.findActive().stream()
                .filter(cert -> {
                    if (cert.getMaxSupply() == null) return false;
                    return cert.getRemainingSupply() <= threshold;
                })
                .toList();
    }

    public List<Certificate> getCertificatesExpiringSoon() {
        // This would need to be implemented based on business logic
        // For now, return active certificates
        return certificateRepository.findActive();
    }

    public boolean validateCertificatePurchase(Long certificateId, BigDecimal userBalance) {
        Optional<Certificate> certOpt = certificateRepository.findById(certificateId);
        if (certOpt.isPresent()) {
            Certificate certificate = certOpt.get();
            return certificate.isAvailable() && 
                   certificate.getPriceUsdt().compareTo(userBalance) <= 0;
        }
        return false;
    }
}
