package com.example.demo.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"outgoingTransactions", "incomingTransactions"})
@EqualsAndHashCode(exclude = {"outgoingTransactions", "incomingTransactions"})
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 34) // Format IBAN standard
    private String iban;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    private String currency;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> outgoingTransactions = new HashSet<>();

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Transaction> incomingTransactions = new HashSet<>();

    public Set<Transaction> getAllTransactions() {
        Set<Transaction> allTransactions = new HashSet<>();
        allTransactions.addAll(outgoingTransactions);
        allTransactions.addAll(incomingTransactions);
        return allTransactions;
    }

    public void addOutgoingTransaction(Transaction transaction) {
        outgoingTransactions.add(transaction);
        transaction.setFromAccount(this);
    }

    public void addIncomingTransaction(Transaction transaction) {
        incomingTransactions.add(transaction);
        transaction.setToAccount(this);
    }

    public void removeOutgoingTransaction(Transaction transaction) {
        outgoingTransactions.remove(transaction);
        transaction.setFromAccount(null);
    }

    public void removeIncomingTransaction(Transaction transaction) {
        incomingTransactions.remove(transaction);
        transaction.setToAccount(null);
    }
}
