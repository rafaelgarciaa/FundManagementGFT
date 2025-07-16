package com.fondosGFT.fondosGFT.service.notification;

import com.fondosGFT.fondosGFT.util.NotificationRequest;
import com.pruebagft.gestionFondosGFT.util.enums.NotificationType;
import io.awspring.cloud.ses.SimpleEmailServiceMailSender;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling various types of notifications (e.g., email, SMS).
 * This service integrates with AWS SES for email and AWS SNS for SMS, allowing the application
 * to send messages based on client preferences.
 * <p>
 * {@code @Service} indicates that this class is a Spring service component,
 * eligible for Spring's component scanning and dependency injection.
 * {@code @Slf4j} provides a logger instance named 'log' for logging messages.
 * </p>
 */
@Service
@Slf4j
public class NotificationService {

    private final SimpleEmailServiceMailSender mailSender;
    private final SnsTemplate snsTemplate;

    /**
     * The email address used as the sender for SES notifications, configured via application properties.
     */
    @Value("${cloud.aws.ses.from-address}")
    private String sesFromAddress;

    /**
     * Constructs a new NotificationService with the necessary mail sender and SNS template.
     * Spring's dependency injection automatically provides these instances.
     *
     * @param mailSender The mail sender for sending emails, typically configured for AWS SES.
     * @param snsTemplate The SNS template for sending SMS messages, configured for AWS SNS.
     */
    @Autowired
    public NotificationService(SimpleEmailServiceMailSender mailSender, SnsTemplate snsTemplate) {
        this.mailSender = mailSender;
        this.snsTemplate = snsTemplate;
    }

    /**
     * Sends a notification based on the type specified in the request.
     * It dispatches the notification to the appropriate method (email or SMS)
     * after a general check for the recipient's validity.
     *
     * @param request The {@link NotificationRequest} containing details about the notification,
     * including its type, addressee, subject, and message.
     */
    public void sendNotification(NotificationRequest request) {
        // Add a general check for the recipient
        if (request.getAddressee() == null || request.getAddressee().trim().isEmpty()) {
            log.warn("Cannot send notification for type {} because the addressee is empty or null.", request.getType());
            return; // Exit the method if there's no recipient
        }

        if (request.getType() == NotificationType.EMAIL) {
            sendEmail(request);
        } else if (request.getType() == NotificationType.SMS) {
            sendSms(request);
        } else {
            log.info("No notification required for type: {}", request.getType());
        }
    }

    /**
     * Sends an email notification using AWS SES.
     * The sender address is configured via application properties.
     *
     * @param request The {@link NotificationRequest} containing email-specific details
     * like the recipient's email address, subject, and message body.
     */
    private void sendEmail(NotificationRequest request) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sesFromAddress);
            mailMessage.setTo(request.getAddressee());
            mailMessage.setSubject(request.getSubject());
            mailMessage.setText(request.getMessage());

            mailSender.send(mailMessage);
            log.info("Email sent to: {} with subject: {}", request.getAddressee(), request.getSubject());
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", request.getAddressee(), e.getMessage());
            // Here you could throw a custom exception or handle the error
        }
    }

    /**
     * Sends an SMS notification using AWS SNS.
     * The recipient is typically a phone number.
     *
     * @param request The {@link NotificationRequest} containing SMS-specific details
     * like the recipient's phone number and the message body.
     */
    private void sendSms(NotificationRequest request) {
        try {
            Message<String> snsMessage = MessageBuilder.withPayload(request.getMessage()).build();
            snsTemplate.send(request.getAddressee(), snsMessage);

            log.info("SMS sent to: {}", request.getAddressee());
        } catch (Exception e) {
            log.error("Error sending SMS to {}: {}", request.getAddressee(), e.getMessage());
            // Here you could throw a custom exception or handle the error
        }
    }
}