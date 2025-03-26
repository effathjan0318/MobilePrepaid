package com.prepaidplus.mobicomm.controller;

import com.prepaidplus.mobicomm.model.PlanCategory;
import com.prepaidplus.mobicomm.service.PlanCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class PlanCategoryController {

    @Autowired
    private PlanCategoryService planCategoryService;

    @GetMapping
    public ResponseEntity<List<PlanCategory>> getAllCategories() {
        return ResponseEntity.ok(planCategoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanCategory> getCategoryById(@PathVariable Integer id) {
        Optional<PlanCategory> category = planCategoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PlanCategory> createCategory(@RequestBody PlanCategory planCategory) {
        return ResponseEntity.ok(planCategoryService.createCategory(planCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanCategory> updateCategory(@PathVariable Integer id, @RequestBody PlanCategory updatedCategory) {
        PlanCategory updated = planCategoryService.updateCategory(id, updatedCategory);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        return planCategoryService.deleteCategory(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
