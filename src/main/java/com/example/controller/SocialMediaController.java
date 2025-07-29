package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

import com.example.entity.*;
import com.example.service.*;
import com.example.exception.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController (AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> findAllMessages(){
        List<Message> foundMessages = messageService.findAllMessages();
        return ResponseEntity.status(200).body(foundMessages);
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> findMessageById(@PathVariable int messageId){
        Message foundMessage = messageService.findMessageById(messageId).orElse(null);
        return ResponseEntity.status(200).body(foundMessage);
    }

    @PostMapping("register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account transientAccount){
        Account persistedAccount;
        try{
            persistedAccount = accountService.registerAccount(transientAccount);    
        } catch (DuplicateUsernameException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(persistedAccount);   
    }

    @PostMapping("login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account possibleAccount){
        Account retrievedAccount;
        try{
            retrievedAccount = accountService.loginAccount(possibleAccount);
        } catch (InvalidCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(retrievedAccount);
    }
    
    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message transientMessage){
        Message persistedMessage;
        try{
            persistedMessage = messageService.createMessage(transientMessage);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(persistedMessage);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId){
        if (messageService.deleteMessageById(messageId)){
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable int messageId, @RequestBody Message transientMessage){
        if (messageService.updateMessageById(messageId, transientMessage)){
            return ResponseEntity.status(HttpStatus.OK).body(1); 
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesFromUser(@PathVariable int accountId){
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(messageService.getMessagesFromUser(accountId));
    }

}
