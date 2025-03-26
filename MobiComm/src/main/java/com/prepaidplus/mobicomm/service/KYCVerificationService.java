package com.prepaidplus.mobicomm.service;

import com.prepaidplus.mobicomm.model.KYCVerification;
import com.prepaidplus.mobicomm.repository.KYCVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class KYCVerificationService {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private KYCVerificationRepository kycVerificationRepository;

    public List<KYCVerification> getAllKYC() {
        return kycVerificationRepository.findAll();
    }

    public Optional<KYCVerification> getKYCById(Integer id) {
        return kycVerificationRepository.findById(id);
    }

    public Optional<KYCVerification> getKYCByUserId(Integer userId) {
        return kycVerificationRepository.findByUser_UserId(userId);
    }

    public KYCVerification createKYC(KYCVerification kyc) {
        return kycVerificationRepository.save(kyc);
    }

    public void deleteKYC(Integer id) {
        kycVerificationRepository.deleteById(id);
    }

    public KYCVerification updateKYCStatus(Integer id, String status, String verifiedAt, String remarks) {
        Optional<KYCVerification> optionalKYC = kycVerificationRepository.findById(id);
        if (optionalKYC.isPresent()) {
            KYCVerification kyc = optionalKYC.get();
            kyc.setStatus(status);
            kyc.setVerifiedAt(verifiedAt);
            kyc.setRemarks(remarks);
            return kycVerificationRepository.save(kyc);
        }
        throw new RuntimeException("KYC record not found.");
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path destinationPath = Paths.get(UPLOAD_DIR + fileName).toAbsolutePath().normalize();
        Files.createDirectories(destinationPath.getParent());
        Files.copy(file.getInputStream(), destinationPath);

        return destinationPath.toString();
    }
}
