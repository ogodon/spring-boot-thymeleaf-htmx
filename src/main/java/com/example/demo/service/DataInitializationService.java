package com.example.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.Account;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DataInitializationService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    private void deleteAllData() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    private User createUser(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    private Account createAccount(User user) {
        Account account = new Account();
        account.setUser(user);
        account.setIban(generateIBAN());
        account.setCurrency("EUR");
        account.setBalance(BigDecimal.ZERO);
        return accountRepository.save(account);
    }

    private Transaction createTransaction(Account fromAccount, Account toAccount,
            BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setCurrency(fromAccount.getCurrency());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime randomDate = now.minusDays(random.nextInt(365)).withHour(random.nextInt(24))
                .withMinute(random.nextInt(60)).withSecond(random.nextInt(60));
        transaction.setExecutionDate(randomDate);
        transaction.setDescription(generateTransactionDescription(true));
        return transactionRepository.save(transaction);
    }

    @PostConstruct
    @Transactional
    public void initializeData() {
        deleteAllData();

        // Create users with simple identifiers
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User user = createUser("user" + i + "@example.com", "ojskiaju");
            users.add(user);
        }

        // Create accounts
        List<Account> accounts = new ArrayList<>();
        String[] currencies = {"EUR", "USD", "GBP"};

        // 2 to 4 accounts per user
        for (User user : users) {
            int numAccounts = 2 + random.nextInt(3);
            for (int i = 0; i < numAccounts; i++) {
                Account account = createAccount(user);
                account.setCurrency(currencies[random.nextInt(currencies.length)]);
                accounts.add(accountRepository.save(account));
            }
        }

        // Create random transactions between different accounts
        for (Account account : accounts) {
            // For each account, create 3 to 7 transactions
            int numTransactions = 100 + random.nextInt(100);
            for (int i = 0; i < numTransactions; i++) {
                // Find a different account with the same currency
                Account otherAccount;
                do {
                    otherAccount = accounts.get(random.nextInt(accounts.size()));
                } while (otherAccount.equals(account)
                        || !otherAccount.getCurrency().equals(account.getCurrency()));

                // Create a random amount between 10 and 1000
                BigDecimal amount = new BigDecimal(random.nextInt(99000) + 1000)
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

                // Randomly choose if it's an outgoing or incoming transaction
                boolean isOutgoing = random.nextBoolean();

                if (isOutgoing) {
                    // Create outgoing transaction
                    Transaction transaction = createTransaction(account, otherAccount, amount);
                    // Update balances
                    account.setBalance(account.getBalance().subtract(amount));
                    otherAccount.setBalance(otherAccount.getBalance().add(amount));
                    accountRepository.save(account);
                    accountRepository.save(otherAccount);
                } else {
                    // Create incoming transaction
                    Transaction transaction = createTransaction(otherAccount, account, amount);
                    // Update balances
                    otherAccount.setBalance(otherAccount.getBalance().subtract(amount));
                    account.setBalance(account.getBalance().add(amount));
                    accountRepository.save(otherAccount);
                    accountRepository.save(account);
                }
            }
        }
    }

    private String generateIBAN() {
        StringBuilder iban = new StringBuilder("FR76");
        for (int i = 0; i < 20; i++) {
            iban.append(random.nextInt(10));
        }
        return iban.toString();
    }

    private String generateTransactionDescription(boolean isOutgoing) {
        String[] descriptions = {isOutgoing ? "Virement vers " : "Virement de ",
                isOutgoing ? "Paiement à " : "Paiement de ",
                isOutgoing ? "Transfert à " : "Transfert de ",
                isOutgoing ? "Achat chez " : "Remboursement de "};
        String[] recipients = {"Alice", "Bob", "Charlie", "David", "Eve", "Carrefour", "Amazon",
                "Netflix", "Spotify", "Apple"};
        return descriptions[random.nextInt(descriptions.length)]
                + recipients[random.nextInt(recipients.length)];
    }
}
