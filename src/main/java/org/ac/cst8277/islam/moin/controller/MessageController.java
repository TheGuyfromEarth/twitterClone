package org.ac.cst8277.islam.moin.controller;

import org.ac.cst8277.islam.moin.entity.Message;
import org.ac.cst8277.islam.moin.service.MessageService;
import org.ac.cst8277.islam.moin.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    MessageService messageService;

    // delete message
    @DeleteMapping
    public ResponseEntity<Boolean> deleteMessage(@RequestHeader("Authorization") String token,
                                 @RequestParam(required = true) String userId,
                                 @RequestParam(required = true) String messageId) {
        return messageService.deleteMessage(token, userId,messageId);
    }

    // get all messages
    @GetMapping
    public ResponseEntity<List<Message>> getMessages(@RequestHeader("Authorization") String token,
                                                     @RequestParam(required = true) String userId)
    {
        return messageService.getMessages(userId, Util.getToken(token));
    }

    // get all messages created by specific producer
    @GetMapping("producer")
    public ResponseEntity<List<Message>> getMessagesByProducer(@RequestHeader("Authorization") String token,
                                                               @RequestParam(required = true) String producerId
    ) {
        return messageService.getMessagesByProducer(producerId, Util.getToken(token));
    }

    // get all messages for given subscriber and messages from multiple producers
    @GetMapping("subscriber")
    public ResponseEntity<List<Message>> getMessagesBySubscriber(@RequestHeader("Authorization") String token,
                                                                 @RequestParam(required = true) String subscriberId
    ) {

        return messageService.getMessagesBySubscriber(subscriberId, Util.getToken(token));
    }

    // create message
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        return messageService.createMessage( message);
    }
}
