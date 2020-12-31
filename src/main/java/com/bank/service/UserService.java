package com.bank.service;

import com.bank.bo.TransactionRequest;
import com.bank.document.User;
import com.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    KafkaTemplate kafkaTemplate;
    private static final String TOPIC_NAME = "transactionDetails";

    public boolean saveUser(User user) {
        userRepository.save(user);
        return true;
    }

    public boolean authenticateUser(TransactionRequest transactionRequest) {
        Optional<User> fetchedUserDetails = userRepository.findById(transactionRequest.getUser().getAccountId());
        if (fetchedUserDetails.get().getUserName().equals(transactionRequest.getUser().getUserName())) {
            if (fetchedUserDetails.get().getPassword().equals(transactionRequest.getUser().getPassword())) {
                sendTransactionRequest(transactionRequest);
            }
        }
        return true;
    }

    public void sendTransactionRequest(TransactionRequest transactionRequest) {
        kafkaTemplate.send(TOPIC_NAME, transactionRequest);
    }

}
