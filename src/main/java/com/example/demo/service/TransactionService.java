package com.example.demo.service;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.model.Account;
import com.example.demo.model.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        // Validate transaction
        validateTransaction(transaction);

        // Check sufficient balance
        Account fromAccount = transaction.getFromAccount();
        if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        // Update balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        Account toAccount = transaction.getToAccount();
        toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));

        // Save accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Save transaction
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public void validateTransaction(Transaction transaction) {
        if (transaction.getFromAccount() == null || transaction.getToAccount() == null) {
            throw new IllegalStateException("Source and destination accounts are required");
        }
        if (transaction.getFromAccount().equals(transaction.getToAccount())) {
            throw new IllegalStateException("Source and destination accounts must be different");
        }
        if (transaction.getAmount() == null
                || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Amount must be positive");
        }
        if (!transaction.getFromAccount().getCurrency()
                .equals(transaction.getToAccount().getCurrency())) {
            throw new IllegalStateException("Accounts must have the same currency");
        }
    }
}
