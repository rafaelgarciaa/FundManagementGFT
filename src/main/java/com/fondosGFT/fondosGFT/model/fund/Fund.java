package com.fondosGFT.fondosGFT.model.fund;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Represents an investment fund entity in the financial application. This class maps directly
 * to a document in the "funds" collection within MongoDB. It defines the core attributes
 * of an investment fund, such as its unique identifier, name, type, and minimum subscription requirement.
 * <p>
 * Lombok annotations are utilized to reduce boilerplate code generation:
 * <ul>
 * <li>{@code @Data}: Automatically generates getters, setters, {@code equals()}, {@code hashCode()}, and {@code toString()} methods.</li>
 * <li>{@code @NoArgsConstructor}: Creates a no-argument constructor, useful for object instantiation by frameworks.</li>
 * <li>{@code @AllArgsConstructor}: Generates a constructor with arguments for all fields, providing a convenient way
 * to initialize all properties.</li>
 * </ul>
 * {@code @Document(collection = "funds")} indicates that this class is a MongoDB document
 * and specifies the name of the collection where these fund documents will be stored.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "funds")
public class Fund {

    /**
     * The unique identifier for the fund.
     * {@code @Id} marks this field as the primary key for the MongoDB document.
     */
    @Id
    private String id;
    /**
     * The official name of the investment fund.
     */
    private String name;
    /**
     * The type of product the fund represents (e.g., "Equity Fund", "Bond Fund", "Mixed Fund").
     */
    private String productType;
    /**
     * The minimum amount required for a single subscription to this fund.
     * This field uses {@link BigDecimal} for precise monetary calculations.
     */
    private BigDecimal minimumSubscriptionAmount;
}