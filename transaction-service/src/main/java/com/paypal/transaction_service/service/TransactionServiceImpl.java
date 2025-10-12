package com.paypal.transaction_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.kafka.KafkaEventProducer;
import com.paypal.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private final TransactionRepository repository;
    @Autowired
    private final ObjectMapper objectMapper;
    private final KafkaEventProducer kafkaEventProducer;
    @Override
    public Transaction createTransaction(Transaction transaction) {
        System.out.println("Entered createTransaction()");
        Long senderId=transaction.getSenderId();
        Long receiverId=transaction.getReceiverId();
        Double amt=transaction.getAmount();
        Transaction  transaction1=new Transaction();
        transaction1.setStatus("SUCCESS");
        transaction1.setTimestamp(LocalDateTime.now());
        transaction1.setSenderId(senderId);
        transaction1.setAmount(amt);
        transaction1.setReceiverId(receiverId);
        System.out.println("Incoming transaction objet"+transaction1);
        Transaction saved=repository.save(transaction1);
        System.out.println("Saved transaction to DB"+saved);
        try {
            String eventPayload= objectMapper.writeValueAsString(saved);
            String key=String.valueOf(saved.getId());
            kafkaEventProducer.sendTransactionEvent(key,saved);
            System.out.println("Kafka message sent");
        } catch (Exception e) {
            System.err.println("Failed to send kafka event: "+ e.getMessage());
            e.printStackTrace();
        }
        return saved;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }
}
