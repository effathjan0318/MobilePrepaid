package com.prepaidplus.mobicomm.repository;

import com.prepaidplus.mobicomm.model.PlanCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanCategoryRepository extends JpaRepository<PlanCategory, Integer> {
}
