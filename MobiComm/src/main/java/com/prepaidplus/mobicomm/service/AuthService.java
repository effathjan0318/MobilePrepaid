package com.prepaidplus.mobicomm.service;

import com.prepaidplus.mobicomm.model.Users;
import com.prepaidplus.mobicomm.repository.UsersRepository;
import com.prepaidplus.mobicomm.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** ✅ Customers Login via Phone Number */
    public String loginWithPhoneNumber(String phoneNumber, String otp) {
        Optional<Users> user = usersRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent() && user.get().getRole().getRoleName().equals("ROLE_USER")) {
            return jwtUtil.generateToken(phoneNumber, "ROLE_USER"); // Customer Token
        }
        throw new RuntimeException("Invalid phone number or not a customer");
    }


    /** ✅ Admin Login via Username & Password */
    public String loginWithUsername(String username, String password) {
        Optional<Users> user = usersRepository.findByUsername(username);
        if (user.isPresent() && user.get().getRole().getRoleName().equals("ROLE_ADMIN")) {
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                return jwtUtil.generateToken(username, "ROLE_ADMIN"); // Admin Token
            }
            throw new RuntimeException("Invalid password");
        }
        throw new RuntimeException("Invalid username or not an admin");
    }


    /** ✅ Refresh Token */
    public String refreshToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String subject = jwtUtil.getPhoneNumberFromToken(refreshToken); // Could be username or phone
            Optional<Users> user = usersRepository.findByPhoneNumber(subject);
            if (user.isEmpty()) {
                user = usersRepository.findByUsername(subject); // Check username if phone not found
            }
            if (user.isPresent()) {
                return jwtUtil.generateToken(subject, user.get().getRole().getRoleName());
            }
        }
        throw new RuntimeException("Invalid refresh token");
    }
}
