package org.ac.cst8277.islam.moin.service;

import org.ac.cst8277.islam.moin.entity.AuthRequest;
import org.ac.cst8277.islam.moin.entity.Message;
import org.ac.cst8277.islam.moin.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<Message> createMessage(Message message) {
            message.setId(UUID.randomUUID().toString());
            return new ResponseEntity<>(messageRepository.save(message),HttpStatus.OK);
    }

    public ResponseEntity<Boolean> deleteMessage(String token, String userId, String messageId) {
        if (getToken(userId, token)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("message_id").is(messageId));
            return new ResponseEntity<>(mongoTemplate.remove(query, Message.class).getDeletedCount() > 0, HttpStatus.OK);
        } else if (!checkUserLogin(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<List<Message>> getMessagesByProducer(String producerId, String userToken) {
        if (getToken(producerId, userToken)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("author").is(producerId));

            return new ResponseEntity<>(mongoTemplate.find(query, Message.class), HttpStatus.OK);
        } else if (!checkUserLogin(producerId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<List<Message>> getMessages(String userId, String userToken) {
        if (getToken(userId, userToken)) {
            return new ResponseEntity<>(mongoTemplate.findAll(Message.class), HttpStatus.OK);
        } else if (!checkUserLogin(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    public ResponseEntity<List<Message>> getMessagesBySubscriber(String subscriberId, String userToken) {
        if (getToken(subscriberId, userToken)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("author").is(subscriberId));

            return new ResponseEntity<>(mongoTemplate.find(query, Message.class), HttpStatus.OK);
        } else if (!checkUserLogin(subscriberId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public Boolean getToken(String userId, String userToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        Boolean isValid = Boolean.FALSE;
        String url = "http://localhost:9000/users/get-token?userId=" + userId;
        String token = restTemplate.getForEntity(url, String.class, Map.class).getBody();
        if (token != null) {
            if (token.equals(userToken)) {
                isValid = Boolean.TRUE;
            }
        }
        return isValid;
    }

    public Boolean checkUserLogin(String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String url = "http://localhost:9000/users/check-login";
        return restTemplate.postForEntity(url, AuthRequest.class, Boolean.class, Map.class).getBody();
    }
}
