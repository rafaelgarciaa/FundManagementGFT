package com.fondosGFT.fondosGFT.model.suscription.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) used for requesting a new fund subscription.
 * This DTO encapsulates the necessary information from the client to
 * initiate an investment in a specific fund. It includes the client's identifier,
 * the target fund's identifier, and the amount to be invested.
 * <p>
 * Lombok annotations are used to reduce boilerplate code:
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
public class SubscriptionRequestDTO {

    /**
     * The unique identifier of the client initiating the subscription.
     * This ensures the investment is associated with the correct client account.
     */
    private String clientId;

    /**
     * The unique identifier of the fund to which the client wishes to subscribe.
     * This specifies the target investment product.
     */
    private String fundId;

    /**
     * The amount of money the client wishes to invest in the specified fund.
     * This field uses {@link BigDecimal} for precise monetary calculations, ensuring accuracy
     * with financial values.
     */
    private BigDecimal amount;
}