package com.prepaidplus.mobicomm.controller;

import com.prepaidplus.mobicomm.model.KYCVerification;
import com.prepaidplus.mobicomm.model.Users;
import com.prepaidplus.mobicomm.service.KYCVerificationService;
import com.prepaidplus.mobicomm.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/kyc")
public class KYCVerificationController {

    @Autowired
    private KYCVerificationService kycVerificationService;
    
    @Autowired
    private UsersService userService;

    @GetMapping
    public ResponseEntity<List<KYCVerification>> getAllKYC() {
        List<KYCVerification> kycList = kycVerificationService.getAllKYC();
        return kycList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(kycList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KYCVerification> getKYCById(@PathVariable Integer id) {
        Optional<KYCVerification> kyc = kycVerificationService.getKYCById(id);
        return kyc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<KYCVerification> getKYCByUserId(@PathVariable Integer userId) {
        Optional<KYCVerification> kyc = kycVerificationService.getKYCByUserId(userId);
        return kyc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> submitKYC(@RequestParam Integer userId, // Add userId parameter
                                       @RequestParam String documentType,
                                       @RequestParam String documentNumber,
                                       @RequestParam MultipartFile idProofFile,
                                       @RequestParam MultipartFile faceImage) {
        try {
            String idProofPath = kycVerificationService.saveFile(idProofFile);
            String faceImagePath = kycVerificationService.saveFile(faceImage);

            
            Optional<Users> userOptional = userService.getUserById(userId);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
            }

            KYCVerification kyc = new KYCVerification();
            kyc.setUser(userOptional.get()); // Set the user
            kyc.setDocumentType(documentType);
            kyc.setDocumentNumber(documentNumber);
            kyc.setDocumentImageUrl(idProofPath);
            kyc.setSelfieImageUrl(faceImagePath);
            kyc.setStatus("Pending");
            kyc.setSubmittedAt(LocalDateTime.now().toString());

            kycVerificationService.createKYC(kyc);

            return ResponseEntity.ok("KYC Submitted Successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error submitting KYC: " + e.getMessage());
        }
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<KYCVerification> updateKYCStatus(@PathVariable Integer id,
                                                            @RequestParam String status,
                                                            @RequestParam(required = false) String verifiedAt,
                                                            @RequestParam(required = false) String remarks) {
        try {
            KYCVerification updatedKYC = kycVerificationService.updateKYCStatus(id, status, verifiedAt, remarks);
            return ResponseEntity.ok(updatedKYC);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKYC(@PathVariable Integer id) {
        kycVerificationService.deleteKYC(id);
        return ResponseEntity.noContent().build();
    }
}
