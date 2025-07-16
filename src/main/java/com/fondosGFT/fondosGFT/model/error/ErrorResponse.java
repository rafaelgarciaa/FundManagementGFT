package com.fondosGFT.fondosGFT.model.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) designed to standardize the structure of error responses
 * sent from the API. This class provides a consistent format for communicating
 * error details to clients, including a timestamp, HTTP status, error type,
 * a descriptive message, and the request path that caused the error.
 * <p>
 * Lombok annotations are utilized to reduce boilerplate code:
 * <ul>
 * <li>{@code @Data}: Automatically generates getters, setters, {@code equals()},
 * {@code hashCode()}, and {@code toString()} methods for all fields.</li>
 * <li>{@code @NoArgsConstructor}: Creates a constructor with no arguments, useful
 * for frameworks that require a default constructor (e.g., for JSON deserialization).</li>
 * <li>{@code @AllArgsConstructor}: Generates a constructor with arguments for all fields,
 * providing a convenient way to initialize all properties upon object creation.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    /**
     * The timestamp indicating when the error occurred.
     * This uses {@link LocalDateTime} to capture the exact date and time.
     */
    private LocalDateTime timestamp;
    /**
     * The HTTP status code of the error (e.g., 400 for Bad Request, 404 for Not Found, 500 for Internal Server Error).
     */
    private int status;
    /**
     * A brief, categorical description of the error (e.g., "Bad Request", "Not Found", "Internal Server Error").
     */
    private String error;
    /**
     * A detailed, human-readable message explaining the specific issue that occurred.
     */
    private String message;
    /**
     * The specific request URI or path that triggered this error.
     */
    private String path;
}