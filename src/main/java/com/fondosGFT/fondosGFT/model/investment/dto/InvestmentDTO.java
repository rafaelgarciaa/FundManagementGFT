package com.fondosGFT.fondosGFT.model.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for representing investment details.
 * This DTO is used to transfer a summary of an individual investment
 * between different layers of the application, often as part of a larger
 * client response or for internal processing. It encapsulates key attributes
 * of a client's holding in a specific fund.
 * <p>
 * Lombok annotations streamline boilerplate code:
 * <ul>
 * <li>{@code @Data}: Automatically generates getters, setters, {@code equals()},
 * {@code hashCode()}, and {@code toString()} methods for all fields.</li>
 * <li>{@code @NoArgsConstructor}: Creates a no-argument constructor, useful for
 * object instantiation by frameworks (e.g., for JSON deserialization).</li>
 * <li>{@code @AllArgsConstructor}: Generates a constructor with arguments for all fields,
 * providing a convenient way to initialize all properties upon creation.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentDTO {

    /**
     * The unique identifier of the fund to which this investment belongs.
     */
    private String fundId;
    /**
     * The name of the fund in which the investment is held.
     */
    private String fundName;
    /**
     * The initial amount of money invested in this fund.
     * Uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal initialAmountInvested;
    /**
     * The current value of the investment. This amount can fluctuate based on fund performance.
     * Uses {@link BigDecimal} for precise financial calculations.
     */
    private BigDecimal currentAmount;
    /**
     * The date and time when the investment subscription was made.
     * Uses {@link LocalDateTime} to capture the exact point in time.
     */
    private LocalDateTime subscriptionDate;
    /**
     * The unique identifier of the transaction that initiated this investment.
     */
    private String transactionId;
}