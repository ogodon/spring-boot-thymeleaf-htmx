package com.example.demo.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.model.User;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/accounts")
    @PreAuthorize("@securityService.checkUserConnected()")
    public String listAccounts(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("accounts", accountService.getAccountsForUser(user));
        return "pages/accounts";
    }

    @GetMapping("/accounts/partial")
    @PreAuthorize("@securityService.checkUserConnected()")
    public String listAccountsPartial(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("accounts", accountService.getAccountsForUser(user));
        return "fragments/accounts-list :: accounts-list";
    }

    @GetMapping("/account/{id}")
    @PreAuthorize("@securityService.checkAccountAccess(#id)")
    public String accountDetail(@PathVariable UUID id, @AuthenticationPrincipal User user,
            Model model) {
        model.addAttribute("account", accountService.getAccountById(id, user));
        return "pages/account";
    }

    // @GetMapping("/account/{id}/transactions")
    // @PreAuthorize("@securityService.checkAccountAccess(#id)")
    // public String accountTransactionsByPage(@PathVariable UUID id,
    // @AuthenticationPrincipal User user, @RequestParam(defaultValue = "0") int page,
    // Model model) {
    // model.addAttribute("account", accountService.getAccountByIdAndPage(id, user, page));
    // model.addAttribute("page", page);
    // return "fragments/transactions-list-by-page :: transactions-list-by-page";
    // }
}
