package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage(Authentication authentication, HttpServletRequest request,
            HttpServletResponse response) {
        if (authentication != null && authentication.isAuthenticated()
                && request.getParameter("logout") != null) {
            return "redirect:/login";
        }
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/accounts";
        }
        return "pages/login";
    }
}
