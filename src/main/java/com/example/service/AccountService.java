package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Registers a new account.
     *
     * @param username The username for the new account.
     * @param password The password for the new account.
     * @return An Optional containing the created Account if successful, or an empty Optional if not.
     */
    public Optional<Account> registerAccount(String username, String password) {
        if (username == null || username.isBlank() || password == null ||
            password.length() < 4) {
            return Optional.empty();
        }

        Account account = new Account(username, password);

        if (accountRepository.findAccountByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }


        try {
            accountRepository.save(account);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(account);
    }

    /**
     * Logs in a user.
     *
     * @param username The username of the account.
     * @param password The password of the account.
     * @return An Optional containing the Account if login is successful, or an empty Optional if not.
     */
    public Optional<Account> login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }

        Account account = accountRepository.findAccountByUsername(username);
        if (account == null || !account.getPassword().equals(password)) {
            return Optional.empty();
        }

        return Optional.of(account);
    }
}
