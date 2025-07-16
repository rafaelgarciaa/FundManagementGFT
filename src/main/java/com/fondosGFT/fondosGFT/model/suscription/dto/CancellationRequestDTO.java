package com.fondosGFT.fondosGFT.model.suscription.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancellationRequestDTO {
    private String clientId;

    private String fundId;
}