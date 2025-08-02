package com.example.demo.service;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.model.User;
import com.example.demo.repository.AccountRepository;
import com.example.demo.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AccountRepository accountRepository;

    public boolean checkUserConnected() {
        SecurityUtils.requireCurrentUser();
        return true;
    }

    public boolean checkAccountAccess(UUID accountId) {
        User currentUser = SecurityUtils.requireCurrentUser();

        boolean hasAccess = accountRepository.findById(accountId)
                .map(account -> account.getUser().getId().equals(currentUser.getId()))
                .orElse(false);

        if (!hasAccess) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Accès non autorisé à ce compte");
        }
        return true;
    }
}
