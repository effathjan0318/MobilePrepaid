package com.prepaidplus.mobicomm.service;

import com.prepaidplus.mobicomm.model.PlanCategory;
import com.prepaidplus.mobicomm.repository.PlanCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanCategoryService {

    @Autowired
    private PlanCategoryRepository planCategoryRepository;

    public List<PlanCategory> getAllCategories() {
        return planCategoryRepository.findAll();
    }

    public Optional<PlanCategory> getCategoryById(Integer id) {
        return planCategoryRepository.findById(id);
    }

    public PlanCategory createCategory(PlanCategory planCategory) {
        return planCategoryRepository.save(planCategory);
    }

    public PlanCategory updateCategory(Integer id, PlanCategory updatedCategory) {
        return planCategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setCategoryName(updatedCategory.getCategoryName());
                    return planCategoryRepository.save(existingCategory);
                }).orElse(null);
    }

    public boolean deleteCategory(Integer id) {
        if (planCategoryRepository.existsById(id)) {
            planCategoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
