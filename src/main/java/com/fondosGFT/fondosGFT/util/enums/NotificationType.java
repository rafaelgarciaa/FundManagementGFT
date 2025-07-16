package com.pruebagft.gestionFondosGFT.util.enums;

/**
 * Defines the possible types of notification preferences a client can have.
 * This enum is used to specify how clients prefer to receive communications
 * from the application, such as transaction confirmations or alerts.
 */
public enum NotificationType {
    /**
     * Indicates that notifications should be sent via email.
     */
    EMAIL,
    /**
     * Indicates that notifications should be sent via SMS (Short Message Service) to a phone number.
     */
    SMS,
    /**
     * Indicates that the user does not wish to receive any notifications.
     */
    NONE // If the user does not wish to receive notifications
}