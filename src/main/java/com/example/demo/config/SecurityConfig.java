package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.demo.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final UserService userService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.addFilterBefore((ServletRequest request, ServletResponse response,
                                FilterChain chain) -> {
                        HttpServletRequest httpRequest = (HttpServletRequest) request;
                        chain.doFilter(request, response);
                }, UsernamePasswordAuthenticationFilter.class);

                http.authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/login", "/contact", "/contact-detail",
                                                "/change-language", "/css/**", "/js/**",
                                                "/webjars/**", "/images/**")
                                .permitAll().requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated())
                                .formLogin(form -> form.loginPage("/login")
                                                .defaultSuccessUrl("/accounts", true)
                                                .failureUrl("/login?error=true").permitAll())
                                .logout(logout -> logout.logoutUrl("/logout")
                                                .logoutSuccessUrl("/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true).permitAll())
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                                                (request, response, authException) -> {
                                                        if (authException instanceof InsufficientAuthenticationException) {
                                                                response.sendRedirect("/login");
                                                        } else {
                                                                response.sendRedirect(
                                                                                "/login?error=true");
                                                        }
                                                }))
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                                .headers(headers -> headers.frameOptions(
                                                frameOptions -> frameOptions.sameOrigin()))
                                .userDetailsService(userService);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
