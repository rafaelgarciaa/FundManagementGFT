package com.fondosGFT.fondosGFT.controller.transaction;
import com.fondosGFT.fondosGFT.model.error.ErrorResponse;
import com.fondosGFT.fondosGFT.model.suscription.dto.CancellationRequestDTO;
import com.fondosGFT.fondosGFT.model.suscription.dto.SubscriptionRequestDTO;
import com.fondosGFT.fondosGFT.model.transaction.Transaction;
import com.fondosGFT.fondosGFT.model.transaction.dto.TransactionResponseDTO;
import com.fondosGFT.fondosGFT.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing financial transactions related to investment funds.
 * This class provides API endpoints for operations such as fund subscriptions,
 * cancellations, and retrieving transaction history.
 * It acts as the HTTP interface, delegating the core business logic to the {@link TransactionService}.
 * <p>
 * {@code @RestController} is a convenience annotation that combines {@code @Controller} and {@code @ResponseBody},
 * making it suitable for building RESTful web services.
 * {@code @RequestMapping("/api/transactions")} maps all HTTP requests to this controller under the base path.
 * {@code @Slf4j} is a Lombok annotation that automatically provides a SLF4J Logger instance named `log`.
 * {@code @Tag} is an OpenAPI (Swagger) annotation used to group and describe related operations
 * in the generated API documentation.
 */
@RestController
@RequestMapping("/api/transactions")
@Slf4j
@Tag(name = "Transaction Management", description = "Operations for fund subscriptions, cancellations, and transaction history") // Tag for transaction endpoints
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Constructs a new TransactionController instance.
     * Spring's dependency injection mechanism automatically provides the necessary
     * {@link TransactionService} instance at application startup.
     *
     * @param transactionService The service layer component responsible for handling all
     * transaction-related business logic and data operations.
     */
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Handles a request to subscribe a client to an investment fund.
     * This endpoint processes the subscription details provided in the request body,
     * validates them, and invokes the business logic via the {@link TransactionService}.
     * Access to this endpoint is restricted to authenticated users with either
     * 'USER' or 'ADMIN' roles.
     *
     * @param requestDTO The {@link SubscriptionRequestDTO} containing the client ID, fund ID,
     * and the amount to subscribe. This object is validated using {@code @Valid}.
     * @return A {@link ResponseEntity} containing a {@link TransactionResponseDTO} representing
     * the newly created subscription transaction, along with an HTTP status of {@code 201 Created}.
     * @apiNote
     * {@code @Operation} provides summary and description for OpenAPI documentation.
     * {@code @ApiResponses} documents various possible HTTP responses including success,
     * invalid input, and authorization errors.
     * {@code @PostMapping("/subscribe")} maps HTTP POST requests to this specific path.
     * {@code @PreAuthorize("hasAnyRole('USER', 'ADMIN')")} enforces role-based access control.
     */
    @Operation(summary = "Subscribe to a fund",
            description = "Allows a client to subscribe to an investment fund with a specified amount. Requires 'USER' or 'ADMIN' role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fund subscribed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or business rule violation (e.g., insufficient balance, fund not found, minimum amount not met)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have the required role")
    })
    @PostMapping("/subscribe")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TransactionResponseDTO> subscribeFund(@Valid @RequestBody SubscriptionRequestDTO requestDTO) {
       Transaction transaction = transactionService.subscribeFund(
                requestDTO.getClientId(),
                requestDTO.getFundId(),
                requestDTO.getAmount()
        );
        TransactionResponseDTO responseDTO = mapTransactionToTransactionResponseDTO(transaction);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Handles a request to cancel a client's fund subscription.
     * This endpoint processes the cancellation details provided in the request body
     * and executes the cancellation logic via the {@link TransactionService}.
     * Access is restricted to authenticated users with 'USER' or 'ADMIN' roles.
     *
     * @param requestDTO The {@link CancellationRequestDTO} containing the client ID
     * and the fund ID for the subscription to be cancelled.
     * This object is validated using {@code @Valid}.
     * @return A {@link ResponseEntity} containing a {@link TransactionResponseDTO} representing
     * the cancellation transaction, along with an HTTP status of {@code 200 OK}.
     * @apiNote
     * {@code @Operation} provides summary and description for OpenAPI documentation.
     * {@code @ApiResponses} documents various possible HTTP responses including success,
     * business rule violations, and authorization errors.
     * {@code @PostMapping("/cancel")} maps HTTP POST requests to this specific path.
     * {@code @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")} enforces role-based access control.
     */
    @Operation(summary = "Cancel a fund subscription",
            description = "Allows a client to cancel their subscription from an investment fund. Requires 'USER' or 'ADMIN' role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fund cancellation initiated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or business rule violation (e.g., investment not found, fund not active)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have the required role")
    })
    @PostMapping("/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TransactionResponseDTO> cancelFund(@Valid @RequestBody CancellationRequestDTO requestDTO) {
        // The RuntimeException will now be caught by GlobalExceptionHandler
        // Note: The package path for 'com.pruebagft.gestionFondosGFT.model.transaction.Transaction' might be incorrect
        // if your main package is 'com.fondosGFT.fondosGFT'. Please verify your Transaction model's package.
        Transaction transaction = transactionService.cancelFund(
                requestDTO.getClientId(),
                requestDTO.getFundId()
        );
        TransactionResponseDTO responseDTO = mapTransactionToTransactionResponseDTO(transaction);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Retrieves the transaction history for a specific client.
     * This endpoint fetches all subscription and cancellation records associated with a given client ID.
     * Access control is applied based on the authenticated user's role:
     * <ul>
     * <li>An 'ADMIN' user can retrieve the transaction history for any client.</li>
     * <li>A 'USER' can only retrieve their own transaction history (i.e., when the `clientId`
     * in the path matches their authenticated username or user ID).</li>
     * </ul>
     *
     * @param clientId The unique identifier of the client whose transaction history is requested.
     * This ID is extracted from the URL path.
     * @return A {@link ResponseEntity} containing a {@link List} of {@link TransactionResponseDTO}s
     * representing the client's transaction history, along with an HTTP status of {@code 200 OK}.
     * @apiNote
     * {@code @Operation} provides summary and description for OpenAPI documentation.
     * {@code @ApiResponses} documents various possible HTTP responses including success,
     * and different authorization errors (unauthorized, forbidden).
     * {@code @GetMapping("/history/{clientId}")} maps HTTP GET requests to this specific path.
     * {@code @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #clientId == authentication.name)")}
     * provides complex role-based and data-based access control.
     */
    @Operation(summary = "Get client transaction history",
            description = "Retrieves a list of all transactions (subscriptions and cancellations) for a specific client. Access is restricted by role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction history retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have the required role or is not authorized for this client ID"),
            @ApiResponse(responseCode = "404", description = "Client not found (if implemented in service)")
    })
    @GetMapping("/history/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #clientId == authentication.name)")
    public ResponseEntity<List<TransactionResponseDTO>> getClientTransactionHistory(@PathVariable String clientId) {
        // Note: The package path for 'com.pruebagft.gestionFondosGFT.model.transaction.Transaction' might be incorrect
        // if your main package is 'com.fondosGFT.fondosGFT'. Please verify your Transaction model's package.
        List<Transaction> transactions = transactionService.getTransactionsHistory(clientId);
        List<TransactionResponseDTO> responseDTOs = transactions.stream()
                .map(this::mapTransactionToTransactionResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Helper method to map a {@code com.pruebagft.gestionFondosGFT.model.transaction.Transaction} entity object
     * to a {@link TransactionResponseDTO}.
     * This method is used internally within the controller to transform the domain model
     * (the entity) into a Data Transfer Object (DTO) that is exposed via the API.
     * This ensures that only relevant and formatted data is sent in the API response.
     *
     * @param transaction The {@code com.pruebagft.gestionFondosGFT.model.transaction.Transaction} entity object
     * to be mapped.
     * @return A {@link TransactionResponseDTO} populated with data copied from the provided transaction entity.
     * It returns a new DTO instance.
     */
    private TransactionResponseDTO mapTransactionToTransactionResponseDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        BeanUtils.copyProperties(transaction, dto); // Copies matching fields by name
        return dto;
    }
}