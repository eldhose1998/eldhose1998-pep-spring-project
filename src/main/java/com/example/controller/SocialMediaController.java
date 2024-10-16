package com.example.controller;


import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {

    @Autowired
    private AccountService accountService;


    @Autowired
    private MessageService messageService;

    public SocialMediaController(AccountService accountService,
                                 MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }


    @PostMapping("/register")
    public ResponseEntity<Account> registerAccountHandler(@RequestBody Account account) {

        try {
            Optional<Account>
                registeredAccount =
                accountService.registerAccount(account.getUsername(), account.getPassword());

            return registeredAccount.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).build();
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginAccountHandler(@RequestBody Account account) {
        Optional<Account> loggedInAccount =
            accountService.login(account.getUsername(), account.getPassword());
        return loggedInAccount.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(401).build());
    }


    @PostMapping("/messages")
    public ResponseEntity<Message> messageCreateHandler(@RequestBody Message message) {
        Optional<Message> createdMessage =
            messageService.createMessage(message.getPostedBy(), message.getMessageText(),
                message.getTimePostedEpoch());
        return createdMessage.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessageHandler() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageByIDHandler(
        @PathVariable("message_id") int messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        return message.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> messageDeleteByIDHandler(
        @PathVariable("message_id") int messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);
        if (message.isPresent() && messageService.deleteMessageById(messageId)) {
            return message.map(msg -> ResponseEntity.ok(1))
                .orElseGet(() -> ResponseEntity.badRequest().build());
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMsgByIDHandler(
        @PathVariable("message_id") int messageId, @RequestBody Message message) {
        String newText = message.getMessageText();
        Optional<Message> updatedMessage =
            messageService.updateMessageText(messageId, newText);
        return updatedMessage.map(msg -> ResponseEntity.ok(1))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessageByUserHandler(
        @PathVariable("account_id") int userId) {
        List<Message> messages = messageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }


}
