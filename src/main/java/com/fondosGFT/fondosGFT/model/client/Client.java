package com.fondosGFT.fondosGFT.model.client;

import com.fondosGFT.fondosGFT.model.investment.Investment;
import com.pruebagft.gestionFondosGFT.util.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a client entity in the financial application. This class maps directly
 * to a document in the "clients" collection within MongoDB. It holds core information
 * about a client, including personal details, financial balance, notification preferences,
 * and their active investments.
 * <p>
 * Lombok annotations are used to streamline boilerplate code generation:
 * <ul>
 * <li>{@code @Data}: Generates getters, setters, {@code equals()}, {@code hashCode()}, and {@code toString()} methods.</li>
 * <li>{@code @NoArgsConstructor}: Creates a no-argument constructor, useful for object instantiation by frameworks.</li>
 * <li>{@code @AllArgsConstructor}: Generates a constructor with arguments for all fields, providing a convenient way
 * to initialize all properties.</li>
 * </ul>
 * {@code @Document(collection = "clients")} indicates that this class is a MongoDB document
 * and specifies the name of the collection where these documents will be stored.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clients")
public class Client {

    /**
     * The unique identifier for the client.
     * {@code @Id} marks this field as the primary key for the MongoDB document.
     */
    @Id
    private String id;

    /**
     * The first name of the client.
     */
    private String firstName;

    /**
     * The last name of the client.
     */
    private String lastName;

    /**
     * The city where the client resides.
     */
    private String city;

    /**
     * The client's current aggregated balance across all their accounts or funds.
     * This field uses {@link BigDecimal} for precise monetary calculations.
     */
    private BigDecimal currentBalance;

    /**
     * The client's preferred method for receiving notifications.
     * This field utilizes the {@link NotificationType} enum.
     * @see NotificationType
     */
    private NotificationType notificationPreference;

    /**
     * A list of active investments currently held by the client.
     * Each investment is represented by an {@link Investment} object.
     * This list is initialized as an empty {@link ArrayList} when a client is created
     * using the specific constructor for new clients.
     */
    private List<Investment> activeInvestments;

    /**
     * The primary phone number of the client, used for communication.
     */
    private String phoneNumber;

    /**
     * The primary email address of the client, used for electronic communication.
     */
    private String email;

    /**
     * Custom constructor for creating a new Client instance with essential details.
     * This constructor initializes the {@code currentBalance} to a default of "500000.00"
     * and {@code activeInvestments} as an empty {@link ArrayList}.
     *
     * @param id The unique identifier for the client.
     * @param firstName The first name of the client.
     * @param lastName The last name of the client.
     * @param city The city where the client resides.
     * @param notificationPreference The client's preferred notification method.
     * @param phoneNumber The client's phone number.
     * @param email The client's email address.
     */
    public Client(String id, String firstName, String lastName, String city, NotificationType notificationPreference, String phoneNumber, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.currentBalance = new BigDecimal("500000.00"); // Default initial balance
        this.notificationPreference = notificationPreference;
        this.activeInvestments = new ArrayList<>(); // Initialize as empty for new clients
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}