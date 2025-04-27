package com.example.spring.security.controller;

import com.example.spring.security.dto.RegisterUserRequestDTO;
import com.example.spring.security.model.Account;
import com.example.spring.security.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequestDTO request) {
        try {
            Account account = new Account();
            account.setEmail(request.getEmail());
            account.setPwd(passwordEncoder.encode(request.getPwd()));
            account.setRole(request.getRole());
            Account savedAccount = accountRepository.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered: " + savedAccount.getId().toString());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user: " + e.getMessage());
        }
    }
}
