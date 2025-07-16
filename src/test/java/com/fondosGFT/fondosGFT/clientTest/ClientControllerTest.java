package com.fondosGFT.fondosGFT.clientTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fondosGFT.fondosGFT.controller.client.ClientController;
import com.fondosGFT.fondosGFT.model.client.Client;
import com.fondosGFT.fondosGFT.service.client.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ClientController}.
 * This class uses {@code @WebMvcTest} to focus on testing the web layer,
 * isolating the controller by mocking the service layer.
 * It simulates HTTP requests and asserts on the controller's responses.
 */
@WebMvcTest(ClientController.class) // Tests only the web layer for ClientController
class ClientControllerTest {

    /**
     * {@link MockMvc} is used to simulate HTTP requests to the controller.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * {@link MockBean} creates a mock instance of {@link ClientService} and
     * injects it into the Spring application context, replacing the actual service.
     */
    @MockBean
    private ClientService clientService;

    /**
     * {@link ObjectMapper} is used for converting Java objects to JSON and vice versa,
     * necessary for handling request and response bodies.
     */
    @Autowired
    private ObjectMapper objectMapper;

    private Client testClient1;
    private Client testClient2;

    /**
     * Sets up test data before each test method execution.
     * Initializes two {@link Client} objects with sample data.
     */
    @BeforeEach
    void setUp() {
        testClient1 = new Client();
        testClient1.setId("clientId1");
        testClient1.setFirstName("Alice");
        testClient1.setEmail("alice@example.com");

        testClient2 = new Client();
        testClient2.setId("clientId2");
        testClient2.setFirstName("Bob");
        testClient2.setEmail("bob@example.com");
    }

    /**
     * Tests the {@code getAllClients} endpoint.
     * It mocks the service call to return a list of clients and asserts
     * that the HTTP response is OK (200) and contains the expected JSON data.
     *
     * @throws Exception if an error occurs during the MVC perform operation.
     */
    @Test
    void testGetAllClients() throws Exception {
        when(clientService.getAllClientes()).thenReturn(Arrays.asList(testClient1, testClient2));

        mockMvc.perform(get("/api/clients")) // Simulates a GET request to /api/clients
                .andExpect(status().isOk()) // Expects a 200 OK status code
                .andExpect(jsonPath("$", hasSize(2))) // Expects a list of 2 elements
                .andExpect(jsonPath("$[0].firstName", is(testClient1.getFirstName()))) // Verifies the first client's first name
                .andExpect(jsonPath("$[1].email", is(testClient2.getEmail()))); // Verifies the second client's email

        verify(clientService, times(1)).getAllClientes(); // Verifies that the service method was called once
    }

    /**
     * Tests the {@code getClientById} endpoint when a client is found.
     * It mocks the service call to return an {@link Optional} containing a client
     * and asserts that the HTTP response is OK (200) with the correct client data.
     *
     * @throws Exception if an error occurs during the MVC perform operation.
     */
    @Test
    void testGetClientByIdFound() throws Exception {
        when(clientService.getClienteById("clientId1")).thenReturn(Optional.of(testClient1));

        mockMvc.perform(get("/api/clients/clientId1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testClient1.getId())))
                .andExpect(jsonPath("$.firstName", is(testClient1.getFirstName())));

        verify(clientService, times(1)).getClienteById("clientId1");
    }

    /**
     * Tests the {@code getClientById} endpoint when a client is not found.
     * It mocks the service call to return an empty {@link Optional}
     * and asserts that the HTTP response is Not Found (404).
     *
     * @throws Exception if an error occurs during the MVC perform operation.
     */
    @Test
    void testGetClientByIdNotFound() throws Exception {
        when(clientService.getClienteById("nonExistentId")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/nonExistentId"))
                .andExpect(status().isNotFound()); // Expects a 404 Not Found status code

        verify(clientService, times(1)).getClienteById("nonExistentId");
    }

    /**
     * Tests the {@code createClient} endpoint.
     * It simulates a POST request with a new client's data, mocks the service's save operation,
     * and asserts that the HTTP response is Created (201) with the saved client's details.
     *
     * @throws Exception if an error occurs during the MVC perform operation.
     */
    @Test
    void testCreateClient() throws Exception {
        Client newClient = new Client();
        newClient.setFirstName("Charlie");
        newClient.setEmail("charlie@example.com");

        Client savedClient = new Client();
        savedClient.setId("newId123");
        savedClient.setFirstName("Charlie");
        savedClient.setEmail("charlie@example.com");

        when(clientService.createCliente(any(Client.class))).thenReturn(savedClient);

        mockMvc.perform(post("/api/clients") // Simulates a POST request to /api/clients
                        .contentType(MediaType.APPLICATION_JSON) // Sets content type to JSON
                        .content(objectMapper.writeValueAsString(newClient))) // Converts object to JSON
                .andExpect(status().isCreated()) // Expects a 201 Created status code
                .andExpect(jsonPath("$.id", is("newId123")))
                .andExpect(jsonPath("$.firstName", is("Charlie")));

        verify(clientService, times(1)).createCliente(any(Client.class));
    }

    /**
     * Tests the {@code updateClient} endpoint.
     * It simulates a PUT request with updated client data, mocks the service's update operation,
     * and asserts that the HTTP response is OK (200) with the modified client's details.
     *
     * @throws Exception if an error occurs during the MVC perform operation.
     */
    @Test
    void testUpdateClient() throws Exception {
        Client updatedInfo = new Client();
        updatedInfo.setId("clientId1"); // ID must be set for update mapping in service
        updatedInfo.setFirstName("Alice Modified");
        updatedInfo.setEmail("alice.modified@example.com");

        Client returnedClient = new Client();
        returnedClient.setId("clientId1");
        returnedClient.setFirstName("Alice Modified");
        returnedClient.setEmail("alice.modified@example.com");

        // Mock the findById call, which typically precedes an update
        when(clientService.getClienteById("clientId1")).thenReturn(Optional.of(testClient1));
        // Mock the update call with the specific client instance
        when(clientService.updateCliente(any(Client.class))).thenReturn(returnedClient);


        mockMvc.perform(put("/api/clients/clientId1") // Simulates a PUT request
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alice Modified")));

        // Verify calls to both getClienteById (often done implicitly before update in real service)
        // and updateCliente. The `any(Client.class)` is crucial here because the object
        // passed to updateCliente might be different from `updatedInfo` but contains same data.
        verify(clientService, times(1)).updateCliente(any(Client.class));
    }

    /**
     * Tests the {@code deleteClient} endpoint.
     * It simulates a DELETE request, mocks the service's delete operation,
     * and asserts that the HTTP response is No Content (204).
     *
     * @throws Exception if an error occurs during the MVC perform operation.
     */
    @Test
    void testDeleteClient() throws Exception {
        when(clientService.deleteClient("clientId1")).thenReturn(true); // Mock successful deletion

        mockMvc.perform(delete("/api/clients/clientId1")) // Simulates a DELETE request
                .andExpect(status().isNoContent()); // Expects a 204 No Content status code

        verify(clientService, times(1)).deleteClient("clientId1");
    }
}