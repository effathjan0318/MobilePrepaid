package com.prepaidplus.mobicomm.repository;

import com.prepaidplus.mobicomm.model.KYCVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KYCVerificationRepository extends JpaRepository<KYCVerification, Integer> {

    Optional<KYCVerification> findByDocumentNumber(String documentNumber);

    Optional<KYCVerification> findByUser_UserId(Integer userId);
}
