package com.prepaidplus.mobicomm.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prepaidplus.mobicomm.model.Transactions;
import com.prepaidplus.mobicomm.repository.TransactionsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionsService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    public List<Transactions> getAllTransactions() {
        return transactionsRepository.findAll();
    }

    public Optional<Transactions> getTransactionById(Integer id) {
        return transactionsRepository.findById(id);
    }

    public List<Transactions> getTransactionsByUserId(Integer userId) {
        return transactionsRepository.findByUser_UserId(userId);
    }

    @Transactional
    public Transactions createTransaction(Transactions transaction) {
        if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid transaction amount");
        }
        if (transaction.getPaymentMethod() == null || transaction.getPaymentMethod().isBlank()) {
            throw new IllegalArgumentException("Payment method cannot be empty");
        }
        return transactionsRepository.save(transaction);
    }

    @Transactional
    public Transactions updateTransactionStatus(Integer id, String status) {
        return transactionsRepository.findById(id).map(transaction -> {
            transaction.setStatus(status);
            return transactionsRepository.save(transaction);
        }).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public void deleteTransaction(Integer id) {
        transactionsRepository.deleteById(id);
    }
}
