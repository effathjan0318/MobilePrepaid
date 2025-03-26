package com.prepaidplus.mobicomm.service;

import com.prepaidplus.mobicomm.model.Plan;
import com.prepaidplus.mobicomm.model.Recharge;
import com.prepaidplus.mobicomm.model.Transactions;
import com.prepaidplus.mobicomm.model.Users;
import com.prepaidplus.mobicomm.repository.PlanRepository;
import com.prepaidplus.mobicomm.repository.RechargeRepository;
import com.prepaidplus.mobicomm.repository.TransactionsRepository;
import com.prepaidplus.mobicomm.repository.UsersRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RechargeService {

    @Autowired
    private RechargeRepository rechargeRepository;
    @Autowired
    private UsersRepository userRepository;
    
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private PlanRepository planRepository;

    public List<Recharge> getAllRecharges() {
        return rechargeRepository.findAll();
    }

    public Optional<Recharge> getRechargeById(Integer id) {
        return rechargeRepository.findById(id);
    }

    public List<Recharge> getRechargesByUserId(Integer userId) {
        return rechargeRepository.findByUser_UserId(userId);
    }

    public List<Recharge> getRechargesByPlanId(Integer planId) {
        return rechargeRepository.findByPlan_PlanId(planId);
    }

    public Recharge createRecharge(Recharge recharge) {
        return rechargeRepository.save(recharge);
    }

    public void deleteRecharge(Integer id) {
        rechargeRepository.deleteById(id);
    }

 // Method to process recharge and save it
    @Transactional
    public Recharge processRecharge(Integer userId, Integer planId, Integer validityDays, Double amount, String paymentMethod) {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.plusDays(validityDays);

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        // Step 1: Create a new Recharge
        Recharge newRecharge = new Recharge();
        newRecharge.setUser(user);
        newRecharge.setPlan(plan);
        newRecharge.setRechargeDate(today.toString());
        newRecharge.setExpiryDate(expiryDate.toString());

        Recharge savedRecharge = rechargeRepository.save(newRecharge);

        // Step 2: Insert into Transactions table
        Transactions transaction = new Transactions();
        transaction.setAmount(amount);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setStatus("SUCCESS");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setRecharge(savedRecharge); // Set FK reference
        transaction.setUser(user);

        transactionsRepository.save(transaction);

        return savedRecharge;
    }
    
    public Optional<Recharge> getCurrentActiveRecharge(Integer userId) {
        List<Recharge> recharges = rechargeRepository.findByUser_UserId(userId);

        return recharges.stream()
            .filter(r -> r.getPlan().isActive())
            .max(Comparator.comparing(Recharge::getExpiryDate)); // Get the most recent active plan
    }


}
    

