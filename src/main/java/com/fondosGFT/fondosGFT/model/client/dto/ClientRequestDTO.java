package com.fondosGFT.fondosGFT.model.client.dto;

import com.pruebagft.gestionFondosGFT.util.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {


    private String firstName;


    private String lastName;


    private String city;


    private NotificationType notificationPreference;

    private String phoneNumber;
    private String email;
}