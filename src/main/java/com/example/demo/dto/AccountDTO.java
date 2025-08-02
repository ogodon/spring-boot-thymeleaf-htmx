package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class AccountDTO {
    private String id;
    private String iban;
    private BigDecimal balance;
    private String currency;
    private List<TransactionDTO> transactions = new ArrayList<>();
}
