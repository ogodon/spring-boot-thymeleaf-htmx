package com.example.demo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserService userService;

    @ModelAttribute("currentUser")
    public String currentUser() {
        return userService.getCurrentUsername();
    }

    @ModelAttribute("isUserLoggedIn")
    public boolean isUserLoggedIn() {
        return userService.isUserLoggedIn();
    }
}
