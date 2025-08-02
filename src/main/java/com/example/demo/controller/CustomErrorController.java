package com.example.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

// @Slf4j
// @Controller
public class CustomErrorController implements ErrorController {

    // @RequestMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public String handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            }
        }

        // For other types of errors, return a generic error page
        return "error/error";
    }
}
