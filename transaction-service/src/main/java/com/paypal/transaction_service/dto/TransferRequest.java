package com.paypal.transaction_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransferRequest {
    private String senderName;
    private  String receiverName;
    private  Double amount;

}
