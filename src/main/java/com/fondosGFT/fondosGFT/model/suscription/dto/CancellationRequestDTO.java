package com.fondosGFT.fondosGFT.model.suscription.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data Transfer Object (DTO) used for requesting the cancellation of a fund subscription.
 * This DTO encapsulates the minimum required information—the client's identifier and
 * the fund's identifier—to process a cancellation request. It's designed for API
 * requests where a client wishes to withdraw from an investment.
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
public class CancellationRequestDTO {
    /**
     * The unique identifier of the client initiating the cancellation request.
     * This ensures the cancellation is associated with the correct client.
     */
    private String clientId;

    /**
     * The unique identifier of the fund from which the client wishes to cancel their subscription.
     * This specifies which investment is being targeted for cancellation.
     */
    private String fundId;
}