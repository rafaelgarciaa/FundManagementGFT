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

@ExtendWith(MockitoExtension.class) // Habilita Mockito para JUnit 5
class ClientServiceTest {

    @Mock // Crea un mock del ClientRepository
    private ClientRepository clientRepository;

    @InjectMocks // Inyecta los mocks en ClientService
    private ClientService clientService;

    // Puedes inicializar objetos de prueba aquí si son comunes para varios tests
    private Client testClient1;
    private Client testClient2;

    @BeforeEach
    void setUp() {
        // Inicializa los clientes de prueba antes de cada test
        testClient1 = new Client();
        testClient1.setId("client1");
        testClient1.setFirstName("Alice");
        testClient1.setEmail("alice@example.com");
        // Otros campos si existen

        testClient2 = new Client();
        testClient2.setId("client2");
        testClient2.setFirstName("Bob");
        testClient2.setEmail("bob@example.com");
        // Otros campos si existen
    }

    @Test
    void testGetAllClients() {
        // Configura el comportamiento del mock
        when(clientRepository.findAll()).thenReturn(Arrays.asList(testClient1, testClient2));

        // Llama al método que estamos probando
        List<Client> clients = clientService.getAllClientes();

        // Verifica el resultado
        assertNotNull(clients);
        assertEquals(2, clients.size());
        assertEquals(testClient1.getFirstName(), clients.get(0).getFirstName());
        assertEquals(testClient2.getFirstName(), clients.get(1).getFirstName());

        // Verifica que el método del repositorio fue llamado exactamente una vez
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testGetClientByIdFound() {
        // Configura el comportamiento del mock para un cliente encontrado
        when(clientRepository.findById("client1")).thenReturn(Optional.of(testClient1));

        // Llama al método
        Optional<Client> foundClient = clientService.getClienteById("client1");

        // Verifica
        assertTrue(foundClient.isPresent());
        assertEquals(testClient1.getFirstName(), foundClient.get().getFirstName());
        verify(clientRepository, times(1)).findById("client1");
    }

    @Test
    void testGetClientByIdNotFound() {
        // Configura el comportamiento del mock para un cliente no encontrado
        when(clientRepository.findById("nonExistent")).thenReturn(Optional.empty());

        // Llama al método
        Optional<Client> foundClient = clientService.getClienteById("nonExistent");

        // Verifica
        assertFalse(foundClient.isPresent());
        verify(clientRepository, times(1)).findById("nonExistent");
    }

    @Test
    void testCreateClient() {
        // Configura el comportamiento del mock al guardar un nuevo cliente
        when(clientRepository.save(any(Client.class))).thenReturn(testClient1); // Simula que save devuelve el cliente guardado

        // Llama al método
        Client createdClient = clientService.createCliente(testClient1);

        // Verifica
        assertNotNull(createdClient);
        assertEquals(testClient1.getFirstName(), createdClient.getFirstName());
        verify(clientRepository, times(1)).save(testClient1);
    }

    @Test
    void testUpdateClientSuccess() {
        Client updatedInfo = new Client();
        updatedInfo.setFirstName("Alice Updated");
        updatedInfo.setEmail("alice.updated@example.com");

        when(clientRepository.findById("client1")).thenReturn(Optional.of(testClient1));
        when(clientRepository.save(any(Client.class))).thenReturn(updatedInfo); // Simula el guardado de la actualización

        Client result = clientService.updateCliente(updatedInfo);

        assertNotNull(result);
        assertEquals("Alice Updated", result.getFirstName());
        assertEquals("alice.updated@example.com", result.getEmail());
        verify(clientRepository, times(1)).findById("client1");
        verify(clientRepository, times(1)).save(any(Client.class)); // Verifica que save fue llamado
    }

    @Test
    void testUpdateClientNotFound() {
        Client updatedInfo = new Client();
        updatedInfo.setFirstName("Alice Updated");

        when(clientRepository.findById("nonExistent")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> { // Asumiendo que ClientService lanza RuntimeException
            clientService.updateCliente(updatedInfo);
        });
        verify(clientRepository, times(1)).findById("nonExistent");
        verify(clientRepository, never()).save(any(Client.class)); // Asegura que save no fue llamado
    }


    @Test
    void testDeleteClient() {
        // Configura el mock para que no haga nada cuando se llame a deleteById
        doNothing().when(clientRepository).deleteById("client1");

        // Llama al método
        clientService.deleteClient("client1");

        // Verifica que el método del repositorio fue llamado
        verify(clientRepository, times(1)).deleteById("client1");
    }
}
