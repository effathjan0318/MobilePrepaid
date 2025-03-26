package com.prepaidplus.mobicomm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    @Value("${otp.expiry.seconds:300}")
    private int otpExpirySeconds;

    @Value("${otp.request.limit:3}") // Max OTP requests per user (default: 3)
    private int otpRequestLimit;

    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, Integer> otpRequestCount = new ConcurrentHashMap<>();

    @Autowired
    private EmailService emailService;

    private static class OtpEntry {
        String otp;
        Instant timestamp;

        OtpEntry(String otp) {
            this.otp = otp;
            this.timestamp = Instant.now();
        }
    }

    /**
     * Generates and sends OTP to the user's email.
     */
    public String generateOTP(String email) {
        // Prevent multiple OTP requests in a short time
        int requestCount = otpRequestCount.getOrDefault(email, 0);
        if (requestCount >= otpRequestLimit) {
            logger.warn("OTP request limit reached for {}", email);
            throw new RuntimeException("Too many OTP requests. Please try again later.");
        }

        String otp = String.valueOf(100000 + new Random().nextInt(900000)); // 6-digit OTP
        otpStorage.put(email, new OtpEntry(otp));

        // Increment OTP request count
        otpRequestCount.put(email, requestCount + 1);

        // Send OTP via email
        String subject = "Your PrepaidPlus OTP Code";
        String body = "Your OTP for PrepaidPlus login is: " + otp + ". This OTP is valid for " + otpExpirySeconds / 60 + " minutes.";
        emailService.sendEmail(email, subject, body);

        logger.info("OTP sent to {}", email); // Masked logging
        return otp;
    }
    
    public boolean sendOtpEmail(String email, String otp) {
        String subject = "Your PrepaidPlus OTP Code";
        String body = "Your OTP for PrepaidPlus login is: " + otp + ". This OTP is valid for " + otpExpirySeconds / 60 + " minutes.";

        try {
            emailService.sendEmail(email, subject, body);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send OTP email to {}: {}", email, e.getMessage());
            return false;
        }
    }


    /**
     * Retrieves the stored OTP if it has not expired.
     */
    public String getStoredOTP(String email) {
        OtpEntry otpEntry = otpStorage.get(email);
        if (otpEntry != null) {
            if (Instant.now().isBefore(otpEntry.timestamp.plusSeconds(otpExpirySeconds))) {
                return otpEntry.otp;
            } else {
                otpStorage.remove(email); // Remove expired OTP
                otpRequestCount.remove(email); // Reset request count after expiration
            }
        }
        return null;
    }
    
    

    /**
     * Validates the OTP entered by the user.
     */
    public boolean isOtpValid(String email, String enteredOtp) {
        String storedOtp = getStoredOTP(email);
        boolean isValid = storedOtp != null && storedOtp.equals(enteredOtp);
        
        if (isValid) {
            clearOTP(email);
        }
        return isValid;
    }

    /**
     * Removes an OTP after successful validation or expiry.
     */
    public void clearOTP(String email) {
        otpStorage.remove(email);
        otpRequestCount.remove(email);
    }

    /**
     * Periodic cleanup of expired OTPs (Runs every 5 minutes).
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void cleanExpiredOtps() {
        otpStorage.entrySet().removeIf(entry -> 
            Instant.now().isAfter(entry.getValue().timestamp.plusSeconds(otpExpirySeconds))
        );
        logger.info("Expired OTPs cleaned up.");
    }
}
