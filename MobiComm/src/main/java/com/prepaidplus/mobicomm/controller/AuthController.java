package com.prepaidplus.mobicomm.controller;

import com.prepaidplus.mobicomm.service.AuthService;
import com.prepaidplus.mobicomm.service.OtpService;
import com.prepaidplus.mobicomm.service.EmailService;
import com.prepaidplus.mobicomm.repository.UsersRepository;
import com.prepaidplus.mobicomm.model.Users;
import com.prepaidplus.mobicomm.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UsersRepository userRepository;

    /**
     * Refresh Access Token
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> tokenData) {
        String refreshToken = tokenData.get("refreshToken");
        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    /**
     * Send OTP to Email (Based on Phone Number Lookup)
     */
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Phone number is required."));
        }

        // Find user by phone number
        Optional<Users> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found."));
        }

        Users user = userOptional.get();
        String email = user.getEmail(); // Use the email associated with this phone number

        // Generate and send OTP
        String otp = otpService.generateOTP(email);
        boolean emailSent = otpService.sendOtpEmail(email, otp);

        if (!emailSent) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to send OTP email."));
        }

        return ResponseEntity.ok(Map.of("message", "OTP sent successfully to registered email."));
    }



    /**
     * Login with OTP (Email-Based)
     */
    @PostMapping("/login/otp")
    public ResponseEntity<?> loginWithOTP(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");
        String enteredOTP = request.get("otp");

        // Find user by phone number
        Optional<Users> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found."));
        }

        Users user = userOptional.get();
        String email = user.getEmail(); // Use email for OTP validation

        // Validate OTP
        if (!otpService.isOtpValid(email, enteredOTP)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid or expired OTP."));
        }

        // Generate JWT tokens
        String accessToken = jwtUtil.generateToken(user.getPhoneNumber(), user.getRole().getRoleName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getPhoneNumber());

        return ResponseEntity.ok(Map.of(
            "message", "OTP validated successfully!",
            "accessToken", accessToken,
            "refreshToken", refreshToken
        ));
    }

    /**
     * Check if JWT Token is Expired
     */
    @GetMapping("/check-expiry")
    public ResponseEntity<?> checkTokenExpiry(@RequestParam String token) {
        boolean isExpired = jwtUtil.isTokenExpired(token);
        return ResponseEntity.ok(Map.of("expired", isExpired));
    }

    /**
     * Test Stored OTP (For Debugging)
     */ 
    @GetMapping("/test-otp")
    public ResponseEntity<?> testOtp(@RequestParam String phoneNumber) {
        // Fetch user email using phone number
        Optional<Users> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found."));
        }

        Users user = userOptional.get();
        if (user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "User email is not set."));
        }

        String email = user.getEmail();
        String storedOTP = otpService.getStoredOTP(email);

        if (storedOTP == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No OTP found for this user."));
        }

        return ResponseEntity.ok(Map.of("storedOTP", storedOTP));
    }

}
