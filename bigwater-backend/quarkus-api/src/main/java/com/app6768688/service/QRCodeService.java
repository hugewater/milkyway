package com.app6768688.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class QRCodeService {
    
    private static final int QR_CODE_SIZE = 300;
    private static final String IMAGE_FORMAT = "PNG";
    
    /**
     * Generate QR code as Base64 string
     */
    public String generateQRCodeBase64(String text) throws WriterException, IOException {
        byte[] qrCodeBytes = generateQRCodeBytes(text);
        return Base64.getEncoder().encodeToString(qrCodeBytes);
    }
    
    /**
     * Generate QR code as byte array
     */
    public byte[] generateQRCodeBytes(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, IMAGE_FORMAT, outputStream);
        
        return outputStream.toByteArray();
    }
    
    /**
     * Generate QR code for TRON address
     */
    public String generateTronQRCode(String tronAddress) throws WriterException, IOException {
        if (tronAddress == null || tronAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("TRON address cannot be null or empty");
        }
        return generateQRCodeBase64(tronAddress.trim());
    }
    
    /**
     * Generate QR code for POLYGON address
     */
    public String generatePolygonQRCode(String polygonAddress) throws WriterException, IOException {
        if (polygonAddress == null || polygonAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("POLYGON address cannot be null or empty");
        }
        return generateQRCodeBase64(polygonAddress.trim());
    }
    
    /**
     * Generate QR code for payment with amount (for TRON)
     * Format: tron:address?amount=value
     */
    public String generateTronPaymentQRCode(String tronAddress, String amount) throws WriterException, IOException {
        if (tronAddress == null || tronAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("TRON address cannot be null or empty");
        }
        
        String paymentUri;
        if (amount != null && !amount.trim().isEmpty()) {
            paymentUri = String.format("tron:%s?amount=%s", tronAddress.trim(), amount.trim());
        } else {
            paymentUri = tronAddress.trim();
        }
        
        return generateQRCodeBase64(paymentUri);
    }
    
    /**
     * Generate QR code for payment with amount (for POLYGON)
     * Format: ethereum:address?amount=value
     */
    public String generatePolygonPaymentQRCode(String polygonAddress, String amount) throws WriterException, IOException {
        if (polygonAddress == null || polygonAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("POLYGON address cannot be null or empty");
        }
        
        String paymentUri;
        if (amount != null && !amount.trim().isEmpty()) {
            paymentUri = String.format("ethereum:%s?amount=%s", polygonAddress.trim(), amount.trim());
        } else {
            paymentUri = polygonAddress.trim();
        }
        
        return generateQRCodeBase64(paymentUri);
    }
}

