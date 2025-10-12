package com.paypal.transaction_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yaml.snakeyaml.util.EnumUtils;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction")
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long senderId;
    @Column(nullable = false)
    private Long receiverId;
    @Column(nullable = false)
    @Positive(message = "Amount must be positive")
    private Double amount;
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public  void prePersist(){
        if(timestamp==null){
            timestamp=LocalDateTime.now();
        }
        if(status== null){
            status="PENDING";
        }
    }

    @Override
    public String toString() {
        return "Transaction{"+"id"+id+", senderId='"+senderId+'\''+", receiverId='"+receiverId+'\''+", amount="+amount+", timestamp="+timestamp + ",status='"+status+'\''+'}';
    }
}
