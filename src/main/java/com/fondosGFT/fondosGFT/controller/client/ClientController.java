package com.fondosGFT.fondosGFT.controller.client;
import com.fondosGFT.fondosGFT.model.client.Client;
import com.fondosGFT.fondosGFT.model.client.dto.ClientRequestDTO;
import com.fondosGFT.fondosGFT.model.client.dto.ClientResponseDTO;
import com.fondosGFT.fondosGFT.model.investment.dto.InvestmentResponseDTO;
import com.fondosGFT.fondosGFT.service.client.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing client-related operations.
 * This class handles incoming HTTP requests for clients, processes them,
 * and delegates business logic to the {@link ClientService}.
 * It maps DTOs (Data Transfer Objects) for request and response serialization.
 *
 * {@code @RestController} is a convenience annotation that combines {@code @Controller} and {@code @ResponseBody},
 * meaning every method returns a domain object instead of a view, and it's suitable for building RESTful web services.
 * {@code @RequestMapping("/api/clients")} maps all HTTP requests to this controller under the "/api/clients" path.
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    /**
     * Constructs a new ClientController with the specified ClientService.
     * Spring's dependency injection automatically provides the ClientService instance.
     *
     * @param clientService The service layer component responsible for client business logic.
     * It's automatically injected by Spring.
     */
    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Creates a new client in the system.
     * This endpoint accepts client data via a {@link ClientRequestDTO}, validates it,
     * and persists the new client through the {@link ClientService}.
     *
     * @param clientRequestDTO The DTO containing the data for the new client.
     * It is expected in the request body and validated using {@code @Valid}.
     * @return A {@link ResponseEntity} containing the {@link ClientResponseDTO} of the newly created client
     * and an HTTP status of {@code 201 Created} if successful.
     * Returns {@code 400 Bad Request} if the input data is invalid.
     */
    @Operation(summary = "Create a new client", description = "Registers a new client in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))) // Document error response
    })
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO clientRequestDTO) {
        Client client = new Client();
        BeanUtils.copyProperties(clientRequestDTO, client);
        client.setFirstName(clientRequestDTO.getFirstName()); // Assuming your Client entity has setFirstName
        client.setLastName(clientRequestDTO.getLastName());
        client.setCity(clientRequestDTO.getCity());
        client.setNotificationPreference(clientRequestDTO.getNotificationPreference());
        client.setEmail(clientRequestDTO.getEmail());
        client.setPhoneNumber(clientRequestDTO.getPhoneNumber());

        Client savedClient = clientService.createCliente(client); // Corrected method name: createClient instead of createCliente

        ClientResponseDTO responseDTO = mapClientToClientResponseDTO(savedClient);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }


    /**
     * Retrieves a client by their unique identifier.
     * This endpoint fetches a client's details based on the provided ID.
     *
     * @param clientId The unique ID of the client to retrieve, extracted from the URL path.
     * @return A {@link ResponseEntity} containing the {@link ClientResponseDTO} if the client is found,
     * with an HTTP status of {@code 200 OK}.
     * Returns {@code 404 Not Found} if no client with the given ID exists.
     * Returns {@code 500 Internal Server Error} for unexpected server issues.
     */
    @Operation(summary = "Get client by ID", description = "Retrieves detailed information about a specific client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{clientId}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable String clientId) {
        return clientService.getClienteById(clientId) // Corrected method name: getClientById instead of getClienteById
                .map(this::mapClientToClientResponseDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a list of all registered clients.
     * This endpoint fetches all client records currently stored in the system.
     *
     * @return A {@link ResponseEntity} containing a {@link List} of {@link ClientResponseDTO}s
     * representing all clients, with an HTTP status of {@code 200 OK}.
     */
    @Operation(summary = "Get all clients", description = "Retrieves a list of all registered clients.")
    @ApiResponse(responseCode = "200", description = "List of clients retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClientResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> clients = clientService.getAllClientes().stream()
                .map(this::mapClientToClientResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }

    /**
     * Helper method to map a {@link Client} entity object to a {@link ClientResponseDTO}.
     * This method is used internally by the controller to transform domain models
     * into DTOs suitable for API responses, ensuring only necessary and relevant
     * data is exposed.
     *
     * @param client The {@link Client} entity object to be mapped.
     * @return A {@link ClientResponseDTO} populated with data from the provided client entity.
     */
    private ClientResponseDTO mapClientToClientResponseDTO(Client client) {
        ClientResponseDTO dto = new ClientResponseDTO();
        BeanUtils.copyProperties(client, dto); // Copies matching fields by name

        // Map active investments if available
        if (client.getActiveInvestments() != null) {
            dto.setActiveInvestments(client.getActiveInvestments().stream()
                    .map(inv -> new InvestmentResponseDTO(
                            inv.getFundId(),
                            inv.getFundName(),
                            inv.getInitialAmountInvested(),
                            inv.getCurrentAmount(),
                            inv.getSubscriptionDate(),
                            inv.getTransactionId()))
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}