package com.fondosGFT.fondosGFT.transactionTest;
import com.fondosGFT.fondosGFT.model.client.Client;
import com.fondosGFT.fondosGFT.repository.client.ClientRepository;
import com.fondosGFT.fondosGFT.repository.transaction.TransactionRepository;
import com.fondosGFT.fondosGFT.service.notification.NotificationService;
import com.fondosGFT.fondosGFT.service.transaction.TransactionService;
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

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ClientRepository clientRepository; // Asumo que TransactionService también usa ClientRepository
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransactionService transactionService;

    private Client testClient;
    private com.pruebagft.gestionFondosGFT.model.transaction.Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId("clientId123");
        testClient.setFirstName("Test Client");
        testClient.setEmail("test@example.com");

        testTransaction = new com.pruebagft.gestionFondosGFT.model.transaction.Transaction();
        testTransaction.setId("transId123");
        testTransaction.setClientId("clientId123");
        testTransaction.setAmount(new BigDecimal("100.00"));
        testTransaction.setDate(LocalDateTime.now());
    }

    @Test
    void testGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(testTransaction));

        List<com.pruebagft.gestionFondosGFT.model.transaction.Transaction> transactions = transactionService.getTransactionsHistory(testClient.getId());

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(testTransaction.getId(), transactions.get(0).getId());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void testCreateTransactionSuccess() {
        // Configura el mock para que cuando busque el cliente, lo encuentre
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.of(testClient));
        // Configura el mock para cuando se guarde la transacción
        when(transactionRepository.save(any(com.pruebagft.gestionFondosGFT.model.transaction.Transaction.class))).thenReturn(testTransaction);
        // Configura el mock para que el servicio de notification no haga nada (o simule un envío exitoso)
        doNothing().when(notificationService).sendNotification(com.pruebagft.gestionFondosGFT.util.NotificationRequest.builder().build());

        com.pruebagft.gestionFondosGFT.model.transaction.Transaction createdTransaction = transactionService.createTransaction(testTransaction);

        assertNotNull(createdTransaction);
        assertEquals(testTransaction.getId(), createdTransaction.getId());
        verify(clientRepository, times(1)).findById(testClient.getId());
        verify(transactionRepository, times(1)).save(any(com.pruebagft.gestionFondosGFT.model.transaction.Transaction.class));
        // Verifica que la notificación fue enviada
        verify(notificationService, times(1)).sendNotification(com.pruebagft.gestionFondosGFT.util.NotificationRequest.builder().build());
    }

    @Test
    void testCreateTransactionClientNotFound() {
        // Configura el mock para que el cliente no sea encontrado
        when(clientRepository.findById(testClient.getId())).thenReturn(Optional.empty());

        // Verifica que se lanza una excepción si el cliente no existe
        assertThrows(RuntimeException.class, () -> { // Asume que TransactionService lanza una RuntimeException
            transactionService.createTransaction(testTransaction);
        }, "Se esperaba una excepción cuando el cliente no existe");

        verify(clientRepository, times(1)).findById(testClient.getId());
        // Asegúrate de que no se intenta guardar la transacción ni enviar notificación
        verify(transactionRepository, never()).save(any(com.pruebagft.gestionFondosGFT.model.transaction.Transaction.class));
        verify(notificationService, never()).sendNotification(com.pruebagft.gestionFondosGFT.util.NotificationRequest.builder().build());
    }

    // Aquí podrías añadir más tests para casos de fallo, validaciones de monto, etc.
}