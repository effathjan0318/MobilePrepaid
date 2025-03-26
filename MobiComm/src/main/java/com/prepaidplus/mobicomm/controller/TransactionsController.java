package com.prepaidplus.mobicomm.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prepaidplus.mobicomm.model.Transactions;

import java.util.List;
import java.util.Optional;
import com.prepaidplus.mobicomm.service.TransactionsService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;

    @GetMapping
    public List<Transactions> getAllTransactions() {
        return transactionsService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transactions> getTransactionById(@PathVariable Integer id) {
        Optional<Transactions> transaction = transactionsService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Transactions> getTransactionsByUserId(@PathVariable Integer userId) {
        return transactionsService.getTransactionsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Transactions transaction) {
        try {
            Transactions savedTransaction = transactionsService.createTransaction(transaction);
            return ResponseEntity.ok(savedTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTransactionStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            Transactions updatedTransaction = transactionsService.updateTransactionStatus(id, status);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        transactionsService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
