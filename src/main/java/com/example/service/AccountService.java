package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.*;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Account registerAccount(Account transientAccount) 
        throws DuplicateUsernameException, BadAccountException{
        if (accountRepository.findByUsername(transientAccount.getUsername()).isPresent()){
            throw new DuplicateUsernameException("The user already exists");
        }
        if (transientAccount.getUsername().isBlank() 
            || transientAccount.getPassword().length() < 4){
            throw new BadAccountException("Improperly Formatted Credentials");
        }
        return accountRepository.save(transientAccount);
    }

    public Account loginAccount(Account transientAccount) throws InvalidCredentialsException{
        return accountRepository
            .findByUsernameAndPassword(transientAccount.getUsername(), transientAccount.getPassword())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid Username or Password"));
    }
}
