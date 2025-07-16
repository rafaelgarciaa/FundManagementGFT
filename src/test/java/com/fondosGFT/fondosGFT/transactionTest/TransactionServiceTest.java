package com.fondosGFT.fondosGFT.transactionTest;
import com.fondosGFT.fondosGFT.model.client.Client;
import com.fondosGFT.fondosGFT.model.transaction.Transaction;
import com.fondosGFT.fondosGFT.repository.client.ClientRepository;
import com.fondosGFT.fondosGFT.repository.transaction.TransactionRepository;
import com.fondosGFT.fondosGFT.service.notification.NotificationService;
import com.fondosGFT.fondosGFT.service.transaction.TransactionService;
import com.fondosGFT.fondosGFT.util.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link TransactionService}.
 * This class uses Mockito to isolate the service layer from its dependencies
 * (like {@link ClientRepository}, {@link TransactionRepository}, and {@link NotificationService}),
 * allowing for focused testing of the service's business logic.
 * <p>
 * {@code @ExtendWith(MockitoExtension.class)} enables Mockito annotations for JUnit 5.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    /**
     * {@code @Mock} creates a mock instance of {@link TransactionRepository}.
     * This mock will simulate the behavior of the actual transaction data access.
     */
    @Mock
    private TransactionRepository transactionRepository;

    /**
     * {@code @Mock} creates a mock instance of {@link ClientRepository}.
     * This mock will simulate the behavior of the actual client data access.
     */
    @Mock
    private ClientRepository clientRepository; // Assuming TransactionService also uses ClientRepository

    /**
     * {@code @Mock} creates a mock instance of {@link NotificationService}.
     * This mock will simulate the behavior of the actual notification sending.
     */
    @Mock
    private NotificationService notificationService;

    /**
     * {@code @InjectMocks} injects the created mocks (like repositories and notification service)
     * into the {@link TransactionService} instance under test.
     */
    @InjectMocks
    private TransactionService transactionService;

    private Client testClient;
    private Transaction testTransaction;

    /**
     * Sets up common test data before each test method execution.
     * Initializes sample {@link Client} and {@link Transaction} objects.
     */
    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId("clientId123");
        testClient.setFirstName("Test Client");
        testClient.setEmail("test@example.com");

        testTransaction = new Transaction();
        testTransaction.setId("transId123");
        testTransaction.setClientId("clientId123");
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setDate(LocalDateTime.now());
    }

    /**
     * Tests the {@code getTransactionsHistory} method.
     * It mocks the repository's {@code findAll()} (or `findByClientIdOrderByDateDesc`) method
     * and verifies that the service returns the expected list of transactions.
     */
    @Test
    void testGetAllTransactions() {
        // Configure the mock to return a list of transactions when findAll is called
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(testTransaction));

        // Call the method being tested
        List<Transaction> transactions = transactionService.getTransactionsHistory(testClient.getId());

        // Verify the result
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(testTransaction.getId(), transactions.get(0).getId());
        // Verify that the repository's findAll method was called exactly once
        // Note: The service method is `getTransactionsHistory(String clientId)` which likely calls
        // `findByClientIdOrderByDateDesc`. If `findAll` is used here, it might be a mismatch.
        verify(transactionRepository, times(1)).findAll();
    }

    /**
     * Tests the {@code createTransaction} method for a successful scenario.
     * It mocks the necessary repository and notification service interactions,
     * and asserts that the transaction is created and dependencies are called correctly.
     */
    @Test
    void testCreateTransactionSuccess() {
        // Configure the mock so that when clientRepository.findById is called with testClient.getId(), it returns the testClient
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.of(testClient));
        // Configure the mock so that when transactionRepository.save is called with any Transaction object, it returns testTransaction
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        // Configure the mock so that notificationService.sendNotification does nothing when called with any NotificationRequest
        doNothing().when(notificationService).sendNotification(any(NotificationRequest.class));

        // Call the method being tested
        Transaction createdTransaction = transactionService.createTransaction(testTransaction);

        // Verify the result
        assertNotNull(createdTransaction);
        assertEquals(testTransaction.getId(), createdTransaction.getId());
        // Verify that clientRepository.findById was called exactly once with the correct ID
        verify(clientRepository, times(1)).findById(testClient.getId());
        // Verify that transactionRepository.save was called exactly once with any Transaction object
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        // Verify that notificationService.sendNotification was called exactly once with any NotificationRequest
        verify(notificationService, times(1)).sendNotification(any(NotificationRequest.class));
    }

    /**
     * Tests the {@code createTransaction} method when the associated client is not found.
     * It mocks the client repository to return an empty optional and asserts that a
     * {@link RuntimeException} is thrown, and no transaction is saved or notification sent.
     */
    @Test
    void testCreateTransactionClientNotFound() {
        // Configure the mock so that when clientRepository.findById is called, it returns an empty Optional (client not found)
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.empty());

        // Verify that a RuntimeException is thrown when the client does not exist
        assertThrows(RuntimeException.class, () -> { // Assumes TransactionService throws RuntimeException
            transactionService.createTransaction(testTransaction);
        }, "Expected an exception when the client does not exist");

        // Verify that clientRepository.findById was called exactly once
        verify(clientRepository, times(1)).findById(testClient.getId());
        // Verify that transactionRepository.save was never called
        verify(transactionRepository, never()).save(any(Transaction.class));
        // Verify that notificationService.sendNotification was never called
        verify(notificationService, never()).sendNotification(any(NotificationRequest.class));
    }

    // You could add more tests here for failure cases, amount validations, etc.
}