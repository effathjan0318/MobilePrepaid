package com.prepaidplus.mobicomm.repository;

import com.prepaidplus.mobicomm.model.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, Integer> {
    
    // Find all recharges for a specific user
    List<Recharge> findByUser_UserId(Integer userId);
    
    // Find all recharges for a specific plan
    List<Recharge> findByPlan_PlanId(Integer planId);
}
