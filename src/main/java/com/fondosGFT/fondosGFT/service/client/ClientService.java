package com.fondosGFT.fondosGFT.service.client;
import com.fondosGFT.fondosGFT.model.client.Client;
import com.fondosGFT.fondosGFT.repository.client.ClientRepository;
import com.pruebagft.gestionFondosGFT.util.enums.NotificationType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing client-related business logic.
 * This class interacts with the {@link ClientRepository} to perform CRUD operations
 * and other client-specific functionalities.
 * <p>
 * {@code @Service} indicates that this class is a Spring service component,
 * eligible for Spring's component scanning and dependency injection.
 * </p>
 */
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Constructs a new ClientService with the specified ClientRepository.
     * Spring's dependency injection automatically provides the ClientRepository instance.
     *
     * @param clienteRepository The repository for accessing and managing client data in the database.
     */
    @Autowired
    public ClientService(ClientRepository clienteRepository) {
        this.clientRepository = clienteRepository;
    }

    /**
     * Retrieves a list of all clients stored in the database.
     *
     * @return A {@link List} of {@link Client} objects representing all registered clients.
     */
    public List<Client> getAllClientes() {
        return clientRepository.findAll();
    }

    /**
     * Retrieves a client by their unique identifier.
     *
     * @param id The unique ID of the client to retrieve.
     * @return An {@link Optional} containing the {@link Client} if found, or an empty {@link Optional} if not.
     */
    public Optional<Client> getClienteById(String id) {
        return clientRepository.findById(id);
    }

    /**
     * Creates a new client in the database.
     * This method ensures that a new client is initialized with a default current balance
     * and an empty list of active investments if these are not provided.
     *
     * @param client The {@link Client} object containing the data for the new client.
     * @return The saved {@link Client} object, including any database-generated ID.
     */
    public Client createCliente(Client client) {
        // Ensure the initial balance is set if not provided in the client object
        if (client.getCurrentBalance() == null) {
            client.setCurrentBalance(new BigDecimal("500000.00")); // Initial amount for new clients
        }
        // Ensure the active investments list is initialized if not provided
        if (client.getActiveInvestments() == null) {
            client.setActiveInvestments(new ArrayList<>());
        }
        return clientRepository.save(client);
    }

    /**
     * Initializes the database with sample client data if the client collection is empty.
     * This method is automatically executed after the application context is initialized.
     * It's useful for development and testing environments to quickly populate data.
     */
    @PostConstruct
    public void initializeClientes() {
        if (clientRepository.count() == 0) {
            Client cliente1 = new Client("CLIENTE001", "Juan", "Perez", "Bogotá", NotificationType.EMAIL, "573001234567", "juan.perez@example.com");
            Client cliente2 = new Client("CLIENTE002", "Maria", "Gomez", "Medellín", NotificationType.SMS, "573109876543", "maria.gomez@example.com");
            clientRepository.saveAll(List.of(cliente1, cliente2));
            System.out.println("Initial clients loaded.");
        }
    }

    /**
     * Updates an existing client's information in the database.
     * The client object must contain the ID of an existing client.
     *
     * @param cliente The {@link Client} object with updated information.
     * @return The updated {@link Client} object after saving to the database.
     */
    public Client updateCliente(Client cliente) {
        return clientRepository.save(cliente);
    }

    /**
     * Deletes a client from the database by their unique identifier.
     *
     * @param id The unique ID of the client to delete.
     * @return {@code true} if the client was found and successfully deleted, {@code false} otherwise.
     */
    public boolean deleteClient(String id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            clientRepository.deleteById(id);
            return true; // Client found and deleted
        }
        return false; // Client not found
    }
}