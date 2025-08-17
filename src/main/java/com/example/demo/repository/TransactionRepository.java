package com.example.demo.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.demo.model.Account;
import com.example.demo.model.Transaction;


public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
        List<Transaction> findByFromAccountOrToAccountOrderByExecutionDateDesc(Account fromAccount,
                        Account toAccount);

        List<Transaction> findByFromAccountOrderByExecutionDateDesc(Account fromAccount);

        List<Transaction> findByToAccountOrderByExecutionDateDesc(Account toAccount);

        @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account ORDER BY t.executionDate DESC")
        Page<Transaction> findTransactionsByAccount(@Param("account") Account account,
                        Pageable pageable);
}
