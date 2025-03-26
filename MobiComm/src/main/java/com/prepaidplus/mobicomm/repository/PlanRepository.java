package com.prepaidplus.mobicomm.repository;

import com.prepaidplus.mobicomm.model.Plan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
	 List<Plan> findByCategory_CategoryNameIgnoreCase(String categoryName);
	 
	 @Query("SELECT p FROM Plan p WHERE (:maxPrice IS NULL OR p.price <= :maxPrice) " +
		       "AND (:network IS NULL OR :network = '' OR p.network = :network) " +
		       "AND (:talktime IS NULL OR :talktime = '' OR p.talktime = :talktime) " +
		       "AND (:data IS NULL OR :data = '' OR p.data = :data)")
		List<Plan> findFilteredPlans(
		    @Param("maxPrice") Double maxPrice,
		    @Param("network") String network,
		    @Param("talktime") String talktime,
		    @Param("data") String data
		);

}

