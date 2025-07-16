package com.fondosGFT.fondosGFT.util;

import com.pruebagft.gestionFondosGFT.util.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a request to send a notification. This class encapsulates all the necessary
 * information to dispatch a message, whether it's an email or an SMS, based on the
 * specified notification type.
 * <p>
 * Lombok annotations are used to reduce boilerplate code:
 * <ul>
 * <li>{@code @Data}: Automatically generates **getters**, **setters**, {@code equals()},
 * {@code hashCode()}, and {@code toString()} methods for all fields.</li>
 * <li>{@code @Builder}: Provides a fluent API for constructing instances of this class,
 * making object creation more readable and robust, especially when dealing with
 * multiple optional fields.</li>
 * </ul>
 */
@Data
@Builder
public class NotificationRequest {
    /**
     * The recipient of the notification. This can be an email address for {@link NotificationType#EMAIL}
     * or a phone number for {@link NotificationType#SMS}.
     */
    private String addressee; // Email or phone number
    /**
     * The subject line of the notification. This field is primarily used for
     * {@link NotificationType#EMAIL} notifications and may be ignored for other types.
     */
    private String subject; // Only for email
    /**
     * The main content or body of the notification message. This text will be sent
     * to the addressee.
     */
    private String message;
    /**
     * The type of notification to be sent (e.g., EMAIL, SMS). This determines
     * the channel through which the message will be dispatched.
     * @see NotificationType
     */
    private NotificationType type;
}