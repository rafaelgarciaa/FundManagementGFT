package com.fondosGFT.fondosGFT.service.transaction;

import com.fondosGFT.fondosGFT.model.client.Client;
import com.fondosGFT.fondosGFT.model.fund.Fund;
import com.fondosGFT.fondosGFT.model.investment.Investment;
import com.fondosGFT.fondosGFT.model.transaction.Transaction;
import com.fondosGFT.fondosGFT.repository.client.ClientRepository;
import com.fondosGFT.fondosGFT.repository.fund.FundRepository;
import com.fondosGFT.fondosGFT.repository.transaction.TransactionRepository;
import com.fondosGFT.fondosGFT.service.notification.NotificationService;
import com.fondosGFT.fondosGFT.util.NotificationRequest;
import com.fondosGFT.fondosGFT.util.enums.TransactionType;
import com.pruebagft.gestionFondosGFT.util.enums.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class responsible for managing financial transactions related to investment funds.
 * This includes handling fund subscriptions, cancellations, and retrieving transaction history.
 * It coordinates operations across client, fund, and transaction repositories, and also
 * integrates with the {@link NotificationService}.
 * <p>
 * {@code @Service} indicates that this class is a Spring service component.
 * {@code @Slf4j} provides a logger instance named 'log' for logging messages.
 * </p>
 */
@Service
@Slf4j
public class TransactionService {

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;
    private final MongoTemplate mongoTemplate;

    /**
     * Constructs a new TransactionService with the required repositories and services.
     * Spring's dependency injection automatically provides these instances.
     *
     * @param clientRepository      The repository for managing client data.
     * @param fundRepository        The repository for managing fund data.
     * @param transactionRepository The repository for managing transaction data.
     * @param notificationService   The service for sending notifications.
     * @param mongoTemplate         The MongoTemplate for advanced MongoDB operations.
     */
    @Autowired
    public TransactionService(
            ClientRepository clientRepository,
            FundRepository fundRepository,
            TransactionRepository transactionRepository,
            NotificationService notificationService,
            MongoTemplate mongoTemplate) {
        this.clientRepository = clientRepository;
        this.fundRepository = fundRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Allows a client to subscribe to an investment fund with a specified amount.
     * This method performs necessary business validations, updates client balance and investments,
     * records the transaction, and sends a notification.
     *
     * @param clientId The ID of the client initiating the subscription.
     * @param fundId   The ID of the fund to subscribe to.
     * @param amount   The amount to be subscribed.
     * @return The created {@link Transaction} record.
     * @throws RuntimeException if validation errors occur (e.g., client/fund not found,
     * insufficient balance, amount below minimum, or client already subscribed).
     */
    @Transactional
    public Transaction subscribeFund(String clientId, String fundId, BigDecimal amount) {
        log.info("Initiating subscription: ClientID={}, FundID={}, Amount={}", clientId, fundId, amount);

        // Retrieve Client and Fund
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));
        Fund fund = fundRepository.findById(fundId)
                .orElseThrow(() -> new RuntimeException("Fund not found with ID: " + fundId));

        // Business Validations
        if (amount.compareTo(fund.getMinimumSubscriptionAmount()) < 0) {
            String errorMessage = "The subscription amount (" + amount + ") is less than the fund's minimum amount (" + fund.getMinimumSubscriptionAmount() + ").";
            log.warn(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        if (client.getCurrentBalance().compareTo(amount) < 0) {
            String errorMessage = "Insufficient balance. Current balance: " + client.getCurrentBalance() + ", Subscription amount: " + amount;
            log.warn(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        // Check if the client already has an active investment in this fund
        boolean alreadySubscribed = client.getActiveInvestments().stream()
                .anyMatch(inv -> inv.getFundId().equals(fundId));
        if (alreadySubscribed) {
            String errorMessage = "The client already has an active investment in fund " + fund.getName();
            log.warn(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        // Create Transaction Record
        Transaction transaction = new Transaction();
        transaction.setBusinessTransactionId(UUID.randomUUID().toString());
        transaction.setClientId(client.getId());
        transaction.setFundId(fund.getId());
        transaction.setFundName(fund.getName());
        transaction.setType(TransactionType.SUSCRIPTION);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setStatus("COMPLETED");
        transaction.setClientBalanceBefore(client.getCurrentBalance());

        // Update Client Balance and Add Investment
        client.setCurrentBalance(client.getCurrentBalance().subtract(amount));
        Investment newInvestment = new Investment(
                fund.getId(),
                fund.getName(),
                amount,
                amount, // Current amount is initially the same as initial amount
                transaction.getDate(),
                transaction.getBusinessTransactionId()
        );
        client.getActiveInvestments().add(newInvestment);
        clientRepository.save(client);

        transaction.setClientBalanceAfter(client.getCurrentBalance());
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Prepare and Send Notification
        String notificationMessage = String.format(
                "Dear %s %s, your subscription to fund %s has been successful for an amount of COP %.2f. " +
                        "Your new available balance is COP %.2f.",
                client.getFirstName(), client.getLastName(), fund.getName(), amount, client.getCurrentBalance()
        );
        String emailSubject = "Fund Subscription Confirmation";

        NotificationRequest notification = NotificationRequest.builder()
                .subject(emailSubject)
                .message(notificationMessage)
                .type(client.getNotificationPreference())
                .build();

        // Assign addressee based on preference and send
        if (client.getNotificationPreference() == NotificationType.EMAIL) {
            if (client.getEmail() == null || client.getEmail().isEmpty()) {
                log.warn("Client {} prefers email, but has no registered email address. No notification will be sent.", client.getId());
            } else {
                notification.setAddressee(client.getEmail());
                notificationService.sendNotification(notification);
            }
        } else if (client.getNotificationPreference() == NotificationType.SMS) {
            if (client.getPhoneNumber() == null || client.getPhoneNumber().isEmpty()) {
                log.warn("Client {} prefers SMS, but has no registered phone number. No notification will be sent.", client.getId());
            } else {
                notification.setAddressee(client.getPhoneNumber());
                notificationService.sendNotification(notification);
            }
        } else {
            log.info("Client {} does not wish to receive notifications.", client.getId());
        }

        log.info("Subscription completed and notification sent for ClientID={}, FundID={}", clientId, fundId);
        return savedTransaction;
    }

    /**
     * Allows a client to cancel their entire subscription to a specific fund.
     * This method validates the existence of the investment, reverses the amount,
     * updates client balance and removes the investment, records the cancellation transaction,
     * and sends a notification.
     *
     * @param clientId The ID of the client initiating the cancellation.
     * @param fundId   The ID of the fund for which the subscription is to be cancelled.
     * @return The created {@link Transaction} record for the cancellation.
     * @throws RuntimeException if validation errors occur (e.g., client not found,
     * or no active investment found for the specified fund).
     */
    @Transactional
    public Transaction cancelFund(String clientId, String fundId) {
        log.info("Initiating cancellation: ClientID={}, FundID={}", clientId, fundId);

        // Retrieve Client
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        // Find the client's active investment for this fund
        Optional<Investment> investmentOptional = client.getActiveInvestments().stream()
                .filter(inv -> inv.getFundId().equals(fundId))
                .findFirst();

        if (investmentOptional.isEmpty()) {
            String errorMessage = "Client does not have an active investment in fund with ID: " + fundId;
            log.warn(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        Investment investmentToCancel = investmentOptional.get();
        BigDecimal amountToReturn = investmentToCancel.getInitialAmountInvested();

        // Create Cancellation Transaction Record
        Transaction transaction = new Transaction();
        transaction.setBusinessTransactionId(UUID.randomUUID().toString());
        transaction.setClientId(client.getId());
        transaction.setFundId(investmentToCancel.getFundId());
        transaction.setFundName(investmentToCancel.getFundName());
        transaction.setType(TransactionType.CANCELATION);
        transaction.setAmount(amountToReturn);
        transaction.setDate(LocalDateTime.now());
        transaction.setStatus("COMPLETED");
        transaction.setClientBalanceBefore(client.getCurrentBalance());

        // Update Client Balance and Remove Investment
        client.setCurrentBalance(client.getCurrentBalance().add(amountToReturn));
        client.getActiveInvestments().remove(investmentToCancel);
        clientRepository.save(client);

        transaction.setClientBalanceAfter(client.getCurrentBalance());
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Prepare and Send Notification
        String notificationMessage = String.format(
                "Dear %s %s, the cancellation of your subscription to fund %s has been successful. " +
                        "COP %.2f has been returned to your account. Your new available balance is COP %.2f.",
                client.getFirstName(), client.getLastName(), investmentToCancel.getFundName(), amountToReturn, client.getCurrentBalance()
        );
        String emailSubject = "Subscription Cancellation Confirmation";

        NotificationRequest notification = NotificationRequest.builder()
                .subject(emailSubject)
                .message(notificationMessage)
                .type(client.getNotificationPreference())
                .build();

        // Assign addressee based on preference and send
        if (client.getNotificationPreference() == NotificationType.EMAIL) {
            if (client.getEmail() == null || client.getEmail().isEmpty()) {
                log.warn("Client {} prefers email, but has no registered email address. No notification will be sent.", client.getId());
            } else {
                notification.setAddressee(client.getEmail());
                notificationService.sendNotification(notification);
            }
        } else if (client.getNotificationPreference() == NotificationType.SMS) {
            if (client.getPhoneNumber() == null || client.getPhoneNumber().isEmpty()) {
                log.warn("Client {} prefers SMS, but has no registered phone number. No notification will be sent.", client.getId());
            } else {
                notification.setAddressee(client.getPhoneNumber());
                notificationService.sendNotification(notification);
            }
        } else {
            log.info("Client {} does not wish to receive notifications.", client.getId());
        }

        log.info("Cancellation completed and notification sent for ClientID={}, FundID={}", clientId, fundId);
        return savedTransaction;
    }

    /**
     * Retrieves the transaction history for a specific client, ordered by date in descending order.
     *
     * @param clientId The ID of the client whose transaction history is requested.
     * @return A {@link List} of {@link Transaction} objects representing the client's historical transactions.
     */
    public List<Transaction> getTransactionsHistory(String clientId) {
        return transactionRepository.findByClientIdOrderByDateDesc(clientId);
    }

    /**
     * Creates and saves a new transaction record.
     * This method can be used for recording general transactions. It automatically generates
     * a business transaction ID if one is not provided and sets the transaction date if null.
     *
     * @param transaction The {@link Transaction} object to be saved.
     * @return The saved {@link Transaction} object, including any database-generated ID.
     * @throws IllegalArgumentException if the provided transaction object is null.
     */
    public Transaction createTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null.");
        }

        // If no business ID is provided, generate one
        if (transaction.getBusinessTransactionId() == null || transaction.getBusinessTransactionId().isEmpty()) {
            transaction.setBusinessTransactionId(UUID.randomUUID().toString());
        }

        // Ensure the date is set if not already (or is null)
        if (transaction.getDate() == null) {
            transaction.setDate(LocalDateTime.now());
        }

        log.info("Creating transaction record: Type={}, ClientID={}, FundID={}, Amount={}"
        );

        return transactionRepository.save(transaction);
    }
}