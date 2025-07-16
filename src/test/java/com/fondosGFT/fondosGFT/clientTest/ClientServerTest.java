package com.fondosGFT.fondosGFT.clientTest;

import com.fondosGFT.fondosGFT.model.client.Client;
import com.fondosGFT.fondosGFT.repository.client.ClientRepository;
import com.fondosGFT.fondosGFT.service.client.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ClientService}.
 * This class uses Mockito to isolate the service layer from its dependencies (like {@link ClientRepository}),
 * allowing for focused testing of the service's business logic.
 * <p>
 * {@code @ExtendWith(MockitoExtension.class)} enables Mockito annotations for JUnit 5.
 * </p>
 */
@ExtendWith(MockitoExtension.class) // Enables Mockito for JUnit 5
class ClientServiceTest {

    /**
     * {@code @Mock} creates a mock instance of {@link ClientRepository}.
     * This mock will simulate the behavior of the actual repository.
     */
    @Mock
    private ClientRepository clientRepository;

    /**
     * {@code @InjectMocks} injects the created mocks (like {@link ClientRepository})
     * into the {@link ClientService} instance under test.
     */
    @InjectMocks
    private ClientService clientService;

    private Client testClient1;
    private Client testClient2;

    /**
     * Sets up test data before each test method execution.
     * Initializes two {@link Client} objects with sample data for consistent testing.
     */
    @BeforeEach
    void setUp() {
        // Initialize test clients before each test
        testClient1 = new Client();
        testClient1.setId("client1");
        testClient1.setFirstName("Alice");
        testClient1.setEmail("alice@example.com");
        // Other fields if they exist

        testClient2 = new Client();
        testClient2.setId("client2");
        testClient2.setFirstName("Bob");
        testClient2.setEmail("bob@example.com");
        // Other fields if they exist
    }

    /**
     * Tests the {@code getAllClientes} method.
     * It verifies that the service correctly retrieves all clients by mocking the repository's {@code findAll()} method.
     */
    @Test
    void testGetAllClients() {
        // Configure the mock's behavior
        when(clientRepository.findAll()).thenReturn(Arrays.asList(testClient1, testClient2));

        // Call the method being tested
        List<Client> clients = clientService.getAllClientes();

        // Verify the result
        assertNotNull(clients);
        assertEquals(2, clients.size());
        assertEquals(testClient1.getFirstName(), clients.get(0).getFirstName());
        assertEquals(testClient2.getFirstName(), clients.get(1).getFirstName());

        // Verify that the repository's method was called exactly once
        verify(clientRepository, times(1)).findAll();
    }

    /**
     * Tests the {@code getClienteById} method when a client is found.
     * It mocks the repository's {@code findById()} method to return an {@link Optional}
     * containing the test client and asserts that the service returns it correctly.
     */
    @Test
    void testGetClientByIdFound() {
        // Configure the mock's behavior for a found client
        when(clientRepository.findById("client1")).thenReturn(Optional.of(testClient1));

        // Call the method
        Optional<Client> foundClient = clientService.getClienteById("client1");

        // Verify the result
        assertTrue(foundClient.isPresent());
        assertEquals(testClient1.getFirstName(), foundClient.get().getFirstName());
        verify(clientRepository, times(1)).findById("client1");
    }

    /**
     * Tests the {@code getClienteById} method when a client is not found.
     * It mocks the repository's {@code findById()} method to return an empty {@link Optional}
     * and asserts that the service correctly returns an empty Optional.
     */
    @Test
    void testGetClientByIdNotFound() {
        // Configure the mock's behavior for a not-found client
        when(clientRepository.findById("nonExistent")).thenReturn(Optional.empty());

        // Call the method
        Optional<Client> foundClient = clientService.getClienteById("nonExistent");

        // Verify the result
        assertFalse(foundClient.isPresent());
        verify(clientRepository, times(1)).findById("nonExistent");
    }

    /**
     * Tests the {@code createCliente} method.
     * It mocks the repository's {@code save()} method to return the saved client
     * and asserts that the service correctly persists the new client.
     */
    @Test
    void testCreateClient() {
        // Configure the mock's behavior when saving a new client
        when(clientRepository.save(any(Client.class))).thenReturn(testClient1); // Simulates that save returns the saved client

        // Call the method
        Client createdClient = clientService.createCliente(testClient1);

        // Verify the result
        assertNotNull(createdClient);
        assertEquals(testClient1.getFirstName(), createdClient.getFirstName());
        verify(clientRepository, times(1)).save(testClient1);
    }

    /**
     * Tests the {@code updateCliente} method for a successful update.
     * It mocks the repository's {@code findById()} (if used internally by update)
     * and {@code save()} methods, and asserts that the client's information is updated.
     */
    @Test
    void testUpdateClientSuccess() {
        Client updatedInfo = new Client();
        updatedInfo.setId("client1"); // Important for the findById in the service logic
        updatedInfo.setFirstName("Alice Updated");
        updatedInfo.setEmail("alice.updated@example.com");

        when(clientRepository.findById("client1")).thenReturn(Optional.of(testClient1)); // Mock the initial retrieval
        when(clientRepository.save(any(Client.class))).thenReturn(updatedInfo); // Simulate the saving of the update

        Client result = clientService.updateCliente(updatedInfo);

        assertNotNull(result);
        assertEquals("Alice Updated", result.getFirstName());
        assertEquals("alice.updated@example.com", result.getEmail());
        verify(clientRepository, times(1)).save(any(Client.class)); // Verify that save was called
    }

    /**
     * Tests the {@code updateCliente} method when the client to update is not found.
     * It mocks {@code findById()} to return empty and asserts that a {@link RuntimeException} is thrown.
     */
    @Test
    void testUpdateClientNotFound() {
        Client updatedInfo = new Client();
        updatedInfo.setId("nonExistent"); // Set ID for the method call

        when(clientRepository.findById("nonExistent")).thenReturn(Optional.empty());

        // Assuming ClientService throws RuntimeException if client not found for update
        assertThrows(RuntimeException.class, () -> {
            clientService.updateCliente(updatedInfo);
        });
        verify(clientRepository, times(1)).findById("nonExistent");
        verify(clientRepository, never()).save(any(Client.class)); // Ensure save was never called
    }


    /**
     * Tests the {@code deleteClient} method.
     * It mocks the repository's {@code deleteById()} method and verifies that
     * the service calls it. It also mocks {@code findById()} to simulate a found client.
     */
    @Test
    void testDeleteClient() {
        // Configure the mock to return a client for findById before deletion
        when(clientRepository.findById("client1")).thenReturn(Optional.of(testClient1));
        // Configure the mock so that deleteById does nothing when called with "client1"
        doNothing().when(clientRepository).deleteById("client1");

        // Call the method
        boolean isDeleted = clientService.deleteClient("client1");

        // Verify the result
        assertTrue(isDeleted);
        // Verify that the repository's methods were called
        verify(clientRepository, times(1)).findById("client1");
        verify(clientRepository, times(1)).deleteById("client1");
    }

    /**
     * Tests the {@code deleteClient} method when the client to delete is not found.
     * It mocks {@code findById()} to return empty and asserts that deletion is reported as false.
     */
    @Test
    void testDeleteClientNotFound() {
        // Configure the mock to return an empty Optional for findById
        when(clientRepository.findById("nonExistent")).thenReturn(Optional.empty());

        // Call the method
        boolean isDeleted = clientService.deleteClient("nonExistent");

        // Verify the result
        assertFalse(isDeleted);
        // Verify that findById was called, but deleteById was not
        verify(clientRepository, times(1)).findById("nonExistent");
        verify(clientRepository, never()).deleteById(anyString());
    }
}