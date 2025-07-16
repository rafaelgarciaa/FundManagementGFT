package com.fondosGFT.fondosGFT.model.client.dto;

import com.pruebagft.gestionFondosGFT.util.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used for creating or updating client information via API requests.
 * This DTO encapsulates the essential data required from the client-side for client registration
 * or modification, excluding sensitive or server-generated fields like IDs or balances.
 * <p>
 * {@code @Data} from Lombok automatically generates boilerplate code such as getters, setters,
 * {@code equals()}, {@code hashCode()}, and {@code toString()} methods.
 * {@code @NoArgsConstructor} generates a constructor with no arguments.
 * {@code @AllArgsConstructor} generates a constructor with arguments for all fields.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {

    /**
     * The first name of the client.
     * This field is typically required when creating or updating a client record.
     */
    private String firstName;

    /**
     * The last name of the client.
     * This field is typically required when creating or updating a client record.
     */
    private String lastName;

    /**
     * The city where the client resides.
     * This provides geographical context for the client.
     */
    private String city;

    /**
     * The client's preferred method for receiving notifications.
     * This field utilizes the {@link NotificationType} enum to specify options like EMAIL, SMS, or NONE.
     * It dictates how the client wishes to receive alerts or communications.
     * @see NotificationType
     */
    private NotificationType notificationPreference;

    /**
     * The primary phone number of the client.
     * This is used for communication, especially if {@code notificationPreference} is SMS.
     */
    private String phoneNumber;

    /**
     * The primary email address of the client.
     * This is used for communication, especially if {@code notificationPreference} is EMAIL.
     * It typically requires a valid email format.
     */
    private String email;
}