package com.paypal.user_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequest {
private String name;
private String email;
    private String password;
    private String adminRequest;

}
