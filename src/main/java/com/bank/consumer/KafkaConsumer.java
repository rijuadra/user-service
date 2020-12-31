package com.bank.consumer;

import com.bank.bo.TransactionRequest;
import com.bank.document.User;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    @Autowired
    UserService userService;

    @KafkaListener(topics = "userDetails1", groupId = "group_id",
    containerFactory = "userKafkaListenerContainerFactory")
    public void consume(User user) {
        userService.saveUser(user);
    }

    @KafkaListener(topics = "userAuthentication1", groupId = "group_id",
    containerFactory = "transactionKafkaListenerContainerFactory")
    public void authorize(TransactionRequest transactionRequest) {
        userService.authenticateUser(transactionRequest);
    }
}
