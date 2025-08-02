package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TransactionDTO {
    private String id;
    private LocalDateTime executionDate;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String type; // "incoming" ou "outgoing"
    private String otherAccountIban;
    private AccountDTO fromAccount;
    private AccountDTO toAccount;
}
