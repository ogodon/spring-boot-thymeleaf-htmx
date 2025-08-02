package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String index(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/accounts";
        }
        return "redirect:/login";
    }

    @GetMapping("/contact")
    public String contact() {
        return "pages/contact";
    }

    @GetMapping("/contact-detail")
    public String contactDetail() {
        return "fragments/contact-detail :: contact-detail";
    }
}
