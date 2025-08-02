package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return findByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
    }

    public User getCurrentUser() {
        return SecurityUtils.getCurrentUser();
    }

    public boolean isUserLoggedIn() {
        return SecurityUtils.isAuthenticated();
    }

    public String getCurrentUsername() {
        return SecurityUtils.getCurrentUsername();
    }

    public User getCurrentUserEntity() {
        return getCurrentUser();
    }
}
