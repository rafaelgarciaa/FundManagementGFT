package com.fondosGFT.fondosGFT.model.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for conveying detailed investment information as part of an API response.
 * This DTO provides a snapshot of a specific investment, including details about the fund,
 * the amounts involved, and the subscription timeline. It's designed to ensure only
 * relevant data is exposed to the API consumer.
 * <p>
 * Lombok annotations are utilized to reduce boilerplate code:
 * <ul>
 * <li>{@code @Data}: Automatically generates **getters**, **setters**, {@code equals()},
 * {@code hashCode()}, and {@code toString()} methods for all fields.</li>
 * <li>{@code @NoArgsConstructor}: Creates a **no-argument constructor**, useful for
 * object instantiation by frameworks (e.g., for JSON deserialization).</li>
 * <li>{@code @AllArgsConstructor}: Generates a **constructor with arguments for all fields**,
 * providing a convenient way to initialize all properties upon creation.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentResponseDTO {
    /**
     * The unique identifier of the fund associated with this investment.
     */
    private String fundId;
    /**
     * The name of the fund in which the investment is held.
     */
    private String fundName;
    /**
     * The initial **amount** that was invested when the subscription was made.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal initialAmountInvested;
    /**
     * The **current market value** of the investment. This value can fluctuate
     * based on the fund's performance.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal currentAmount;
    /**
     * The **date and time** when the investment subscription officially began.
     * This uses {@link LocalDateTime} to capture the exact point in time.
     */
    private LocalDateTime subscriptionDate;
    /**
     * The unique identifier of the **transaction** that originated this investment.
     */
    private String transactionId;
}