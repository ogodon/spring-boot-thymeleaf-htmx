package com.example.demo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.example.demo.dto.AccountDTO;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Account;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public List<AccountDTO> getAccountsForUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }
        List<Account> accounts = accountRepository.findByUser(user);
        return accounts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public AccountDTO getAccountById(UUID id, User currentUser) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du compte ne peut pas être null");
        }
        if (currentUser == null) {
            throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
        }

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Vous n'avez pas accès à ce compte");
        }

        AccountDTO dto = convertToDTO(account);
        dto.setTransactions(getAllTransactionsForAccount(account));
        return dto;
    }

    // public AccountDTO getAccountByIdAndPage(UUID id, User currentUser, int transactionsPage) {
    // if (id == null) {
    // throw new IllegalArgumentException("L'ID du compte ne peut pas être null");
    // }
    // if (currentUser == null) {
    // throw new IllegalArgumentException("L'utilisateur ne peut pas être null");
    // }

    // Account account = accountRepository.findById(id)
    // .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

    // if (!account.getUser().getId().equals(currentUser.getId())) {
    // throw new AccessDeniedException("Vous n'avez pas accès à ce compte");
    // }

    // AccountDTO dto = convertToDTO(account);
    // dto.setTransactions(getTransactionsForAccount(account, transactionsPage));
    // return dto;
    // }

    private AccountDTO convertToDTO(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Le compte ne peut pas être null");
        }

        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId().toString());
        dto.setIban(account.getIban());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        return dto;
    }

    private List<TransactionDTO> getAllTransactionsForAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Le compte ne peut pas être null");
        }

        List<Transaction> transactions =
                transactionRepository.findAllTransactionsByAccount(account);
        return transactions.stream().map(transaction -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setId(transaction.getId().toString());
            dto.setAmount(transaction.getAmount());
            dto.setCurrency(transaction.getCurrency());
            dto.setDescription(transaction.getDescription());
            dto.setExecutionDate(transaction.getExecutionDate());
            dto.setType(transaction.getFromAccount().getId().equals(account.getId()) ? "outgoing"
                    : "incoming");
            AccountDTO fromAccount = new AccountDTO();
            fromAccount.setId(transaction.getFromAccount().getId().toString());
            dto.setFromAccount(fromAccount);
            AccountDTO toAccount = new AccountDTO();
            toAccount.setId(transaction.getToAccount().getId().toString());
            dto.setToAccount(toAccount);
            return dto;
        }).collect(Collectors.toList());
    }

    // private List<TransactionDTO> getTransactionsForAccount(Account account, int page) {
    // if (account == null) {
    // throw new IllegalArgumentException("Le compte ne peut pas être null");
    // }

    // Pageable pageable = PageRequest.of(page, 10);
    // Page<Transaction> transactionPage =
    // transactionRepository.findTransactionsByAccount(account, pageable);
    // List<Transaction> transactions = transactionPage.getContent();
    // return transactions.stream().map(transaction -> {
    // TransactionDTO dto = new TransactionDTO();
    // dto.setId(transaction.getId().toString());
    // dto.setAmount(transaction.getAmount());
    // dto.setCurrency(transaction.getCurrency());
    // dto.setDescription(transaction.getDescription());
    // dto.setExecutionDate(transaction.getExecutionDate());
    // dto.setType(transaction.getFromAccount().getId().equals(account.getId()) ? "outgoing"
    // : "incoming");
    // AccountDTO fromAccount = new AccountDTO();
    // fromAccount.setId(transaction.getFromAccount().getId().toString());
    // dto.setFromAccount(fromAccount);
    // AccountDTO toAccount = new AccountDTO();
    // toAccount.setId(transaction.getToAccount().getId().toString());
    // dto.setToAccount(toAccount);
    // return dto;
    // }).collect(Collectors.toList());
    // }
}
