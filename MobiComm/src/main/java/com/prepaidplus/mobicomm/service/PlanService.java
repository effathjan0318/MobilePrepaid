package com.prepaidplus.mobicomm.service;

import com.prepaidplus.mobicomm.model.Plan;
import com.prepaidplus.mobicomm.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

	@Autowired
    private PlanRepository planRepository;

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Optional<Plan> getPlanById(Integer id) {
        return planRepository.findById(id);
    }


    public Optional<Plan> updatePlan(Integer id, Plan updatedPlan) {
        return planRepository.findById(id).map(existingPlan -> {
            existingPlan.setValidity(updatedPlan.getValidity());
            existingPlan.setCategory(updatedPlan.getCategory());
            existingPlan.setPrice(updatedPlan.getPrice());
            existingPlan.setData(updatedPlan.getData());
            existingPlan.setVoice(updatedPlan.getVoice());
            existingPlan.setTalktime(updatedPlan.getTalktime());
            existingPlan.setSms(updatedPlan.getSms());
            existingPlan.setNetwork(updatedPlan.getNetwork());
            existingPlan.setNote(updatedPlan.getNote());
            return planRepository.save(existingPlan);
        });
    }

    public boolean deletePlan(Integer id) {
        if (planRepository.existsById(id)) {
            planRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Plan> getPlansByCategory(String categoryName) {
        return planRepository.findByCategory_CategoryNameIgnoreCase(categoryName.trim());
    }
    
    public boolean deactivatePlan(int id) {
        Optional<Plan> optionalPlan = planRepository.findById(id);
        if (optionalPlan.isPresent()) {
            Plan plan = optionalPlan.get();
            if (!plan.isActive()) {
                return false; 
            }
            plan.setActive(false); 
            planRepository.save(plan);
            return true;
        }
        return false;
    }
    
    public List<Plan> getFilteredPlans(Double maxPrice, String network, String talktime, String data) {
        return planRepository.findFilteredPlans(maxPrice, network, talktime, data);
    }

}

