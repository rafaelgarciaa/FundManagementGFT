package com.fondosGFT.fondosGFT.model.transaction;

import com.fondosGFT.fondosGFT.util.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a financial transaction entity within the application. This class maps directly
 * to a document in the "transactions" collection in MongoDB. It captures comprehensive
 * details of any financial operation related to client investments, such as subscriptions
 * or cancellations.
 * <p>
 * Lombok annotations are used to reduce boilerplate code:
 * <ul>
 * <li>{@code @Data}: Automatically generates **getters**, **setters**, {@code equals()},
 * {@code hashCode()}, and {@code toString()} methods for all fields.</li>
 * <li>{@code @NoArgsConstructor}: Creates a **no-argument constructor**, useful for
 * object instantiation by frameworks.</li>
 * <li>{@code @AllArgsConstructor}: Generates a **constructor with arguments for all fields**,
 * providing a convenient way to initialize all properties.</li>
 * </ul>
 * {@code @Document(collection = "transactions")} indicates that this class is a MongoDB document
 * and specifies the name of the collection where these transaction records will be stored.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    /**
     * The **unique identifier** for the transaction document in MongoDB.
     * {@code @Id} marks this field as the primary key for the MongoDB document.
     */
    @Id
    private String id;

    /**
     * A **business-level identifier** for the transaction, distinct from the MongoDB {@code _id}.
     * This might be used to group related operations or for external referencing.
     */
    private String businessTransactionId;

    /**
     * The **unique identifier of the client** who initiated or is affected by this transaction.
     */
    private String clientId;

    /**
     * The **unique identifier of the fund** involved in this transaction.
     */
    private String fundId;

    /**
     * The **name of the fund** involved in this transaction.
     */
    private String fundName;

    /**
     * The **type of transaction** (e.g., SUBSCRIPTION, CANCELLATION).
     * This field uses the {@link TransactionType} enum to categorize the operation.
     * @see TransactionType
     */
    private TransactionType type;

    /**
     * The **monetary amount** associated with this transaction.
     * This field uses {@link BigDecimal} for precise financial calculations, ensuring accuracy.
     */
    private BigDecimal amount;

    /**
     * The **date and time** when the transaction occurred.
     * This uses {@link LocalDateTime} to capture the exact point in time.
     */
    private LocalDateTime date;

    /**
     * The **client's balance immediately before** this transaction was processed.
     * This provides a snapshot of the client's funds prior to the operation.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal clientBalanceBefore;

    /**
     * The **client's balance immediately after** this transaction was processed.
     * This reflects the client's updated funds post-operation.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal clientBalanceAfter;

    /**
     * The **current status** of the transaction (e.g., "COMPLETED", "PENDING", "FAILED").
     * This indicates the outcome of the transaction processing.
     */
    private String status;

    /**
     * A **descriptive message detailing any error** that occurred during the transaction,
     * if the transaction failed. This field will be {@code null} for successful transactions.
     */
    private String errorMessage;

}