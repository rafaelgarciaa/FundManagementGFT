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

@WebMvcTest(ClientController.class) // Prueba solo la capa web para ClientController
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc; // Utilidad para simular peticiones HTTP

    @MockBean // Crea un mock del ClientService e lo inyecta en el contexto de Spring
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos Java a JSON y viceversa

    private Client testClient1;
    private Client testClient2;

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

    @Test
    void testGetAllClients() throws Exception {
        when(clientService.getAllClientes()).thenReturn(Arrays.asList(testClient1, testClient2));

        mockMvc.perform(get("/api/clients")) // Simula una petición GET a /api/clients
                .andExpect(status().isOk()) // Espera un código de estado 200 OK
                .andExpect(jsonPath("$", hasSize(2))) // Espera una lista de 2 elementos
                .andExpect(jsonPath("$[0].name", is(testClient1.getFirstName()))) // Verifica el nombre del primer cliente
                .andExpect(jsonPath("$[1].email", is(testClient2.getEmail()))); // Verifica el email del segundo cliente

        verify(clientService, times(1)).getAllClientes(); // Verifica que el método del servicio fue llamado
    }

    @Test
    void testGetClientByIdFound() throws Exception {
        when(clientService.getClienteById("clientId1")).thenReturn(Optional.of(testClient1));

        mockMvc.perform(get("/api/clients/clientId1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testClient1.getId())))
                .andExpect(jsonPath("$.name", is(testClient1.getFirstName())));

        verify(clientService, times(1)).getClienteById("clientId1");
    }

    @Test
    void testGetClientByIdNotFound() throws Exception {
        when(clientService.getClienteById("nonExistentId")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/nonExistentId"))
                .andExpect(status().isNotFound()); // Espera un código de estado 404 Not Found

        verify(clientService, times(1)).getClienteById("nonExistentId");
    }

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

        mockMvc.perform(post("/api/clients") // Simula una petición POST a /api/clients
                        .contentType(MediaType.APPLICATION_JSON) // Establece el tipo de contenido a JSON
                        .content(objectMapper.writeValueAsString(newClient))) // Convierte el objeto a JSON
                .andExpect(status().isCreated()) // Espera un código de estado 201 Created
                .andExpect(jsonPath("$.id", is("newId123")))
                .andExpect(jsonPath("$.name", is("Charlie")));

        verify(clientService, times(1)).createCliente(any(Client.class));
    }

    @Test
    void testUpdateClient() throws Exception {
        Client updatedInfo = new Client();
        updatedInfo.setFirstName("Alice Modified");
        updatedInfo.setEmail("alice.modified@example.com");

        Client returnedClient = new Client();
        returnedClient.setId("clientId1");
        returnedClient.setFirstName("Alice Modified");
        returnedClient.setEmail("alice.modified@example.com");

        when(clientService.updateCliente(testClient2)).thenReturn(returnedClient);

        mockMvc.perform(put("/api/clients/clientId1") // Simula una petición PUT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alice Modified")));

        verify(clientService, times(1)).updateCliente(testClient1);
    }

    @Test
    void testDeleteClient() throws Exception {
        doNothing().when(clientService).deleteClient("clientId1"); // No hace nada al llamar deleteClient

        mockMvc.perform(delete("/api/clients/clientId1")) // Simula una petición DELETE
                .andExpect(status().isNoContent()); // Espera un código de estado 204 No Content

        verify(clientService, times(1)).deleteClient("clientId1");
    }
}