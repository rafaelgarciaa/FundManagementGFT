package com.fondosGFT.fondosGFT.model.suscription.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubscriptionRequestDTO {


    private String clientId;


    private String fundId;


    private BigDecimal amount;
}