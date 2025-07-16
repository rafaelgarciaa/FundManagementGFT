package com.fondosGFT.fondosGFT.model.investment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents an individual investment made by a client in a specific fund.
 * This class serves as an embedded document within the `Client` entity
 * (or could be a standalone document if required by design). It captures
 * the essential details of a single investment holding.
 * <p>
 * Lombok annotations are used to reduce boilerplate code:
 * <ul>
 * <li>{@code @Data}: Automatically generates **getters**, **setters**, {@code equals()},
 * {@code hashCode()}, and {@code toString()} methods for all fields.</li>
 * <li>{@code @NoArgsConstructor}: Creates a **no-argument constructor**, useful for
 * object instantiation.</li>
 * <li>{@code @AllArgsConstructor}: Generates a **constructor with arguments for all fields**,
 * providing a convenient way to initialize all properties upon creation.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Investment {
    /**
     * The unique identifier of the fund to which this investment belongs.
     * This links the investment to its associated fund details.
     */
    private String fundId;
    /**
     * The name of the fund in which the investment is held.
     */
    private String fundName;
    /**
     * The **initial amount** of money invested in this fund at the time of subscription.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal initialAmountInvested;
    /**
     * The **current market value** of this investment. This amount can fluctuate
     * based on the fund's performance and is updated over time.
     * This field uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal currentAmount;
    /**
     * The **date and time** when the investment subscription was officially made.
     * This uses {@link LocalDateTime} to capture the exact point in time.
     */
    private LocalDateTime subscriptionDate;
    /**
     * The unique identifier of the **transaction** that originated this specific investment.
     * This links the investment to its creating transaction record.
     */
    private String transactionId;
}