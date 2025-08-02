package com.example.demo.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Account;
import com.example.demo.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByFromAccountOrToAccountOrderByExecutionDateDesc(Account fromAccount,
            Account toAccount);

    List<Transaction> findByFromAccountOrderByExecutionDateDesc(Account fromAccount);

    List<Transaction> findByToAccountOrderByExecutionDateDesc(Account toAccount);
}
