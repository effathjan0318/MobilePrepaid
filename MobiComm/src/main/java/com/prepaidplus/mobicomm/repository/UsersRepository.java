package com.prepaidplus.mobicomm.repository;

import com.prepaidplus.mobicomm.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByPhoneNumber(String phoneNumber);
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    
}
