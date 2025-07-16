package com.fondosGFT.fondosGFT.model.transaction.dto;

import com.pruebagft.gestionFondosGFT.util.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for representing detailed transaction information.
 * This DTO is used to convey a comprehensive view of a financial transaction,
 * including its type, associated client and fund, amounts, dates, and status.
 * It's designed for use in API responses or when transferring transaction data
 * between different layers of the application.
 * <p>
 * Lombok annotations streamline boilerplate code:
 * <ul>
 * <li>{@code @Data}: Automatically generates **getters**, **setters**, {@code equals()},
 * {@code hashCode()}, and {@code toString()} methods for all fields.</li>
 * <li>{@code @NoArgsConstructor}: Creates a **no-argument constructor**, useful for
 * object instantiation during deserialization.</li>
 * <li>{@code @AllArgsConstructor}: Generates a **constructor with arguments for all fields**,
 * providing a convenient way to initialize all properties upon creation.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    /**
     * The unique identifier for the transaction. This ID is typically generated
     * by the persistence layer.
     */
    private String id;
    /**
     * An identifier that groups related business operations, if applicable (e.g.,
     * a unique ID for a subscription process that might involve multiple internal transactions).
     */
    private String businessTransactionId;
    /**
     * The unique identifier of the client associated with this transaction.
     */
    private String clientId;
    /**
     * The unique identifier of the fund involved in this transaction.
     */
    private String fundId;
    /**
     * The name of the fund involved in this transaction.
     */
    private String fundName;
    /**
     * The type of transaction (e.g., SUBSCRIPTION, CANCELLATION).
     * This field uses the {@link TransactionType} enum.
     * @see TransactionType
     */
    private TransactionType type;
    /**
     * The monetary amount involved in the transaction.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal amount;
    /**
     * The date and time when the transaction occurred.
     * This uses {@link LocalDateTime} to capture the exact point in time.
     */
    private LocalDateTime date;
    /**
     * The client's balance immediately before this transaction was processed.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal clientBalanceBefore;
    /**
     * The client's balance immediately after this transaction was processed.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal clientBalanceAfter;
    /**
     * The current status of the transaction (e.g., "COMPLETED", "PENDING", "FAILED").
     */
    private String status;
    /**
     * A descriptive message indicating an error, if the transaction failed.
     * This field will be null if the transaction was successful.
     */
    private String errorMessage;
}