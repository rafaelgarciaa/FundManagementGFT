package com.fondosGFT.fondosGFT.controller.fund;

import com.fondosGFT.fondosGFT.model.fund.Fund;
import com.fondosGFT.fondosGFT.model.fund.dto.FundResponseDTO;
import com.fondosGFT.fondosGFT.service.fund.FundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for managing investment fund information.
 * This class provides API endpoints to retrieve details about available funds.
 * It delegates the actual data fetching to the {@link FundService}.
 *
 * {@code @RestController} indicates that this class is a controller where every method returns a domain object
 * instead of a view, making it suitable for building RESTful web services.
 * {@code @RequestMapping("/api/funds")} maps all HTTP requests to this controller under the "/api/funds" base path.
 * {@code @Tag(name = "Fund Information", description = "...")} is an OpenAPI (Swagger) annotation
 * used to group and describe related operations in the API documentation.
 */
@RestController
@RequestMapping("/api/funds")
@Tag(name = "Fund Information", description = "Operations to retrieve information about available investment funds")
public class FundController {

    private final FundService fundService;

    /**
     * Constructs a new FundController with the specified FundService.
     * Spring's dependency injection automatically provides the FundService instance.
     *
     * @param fundService The service layer component responsible for fund-related business logic and data retrieval.
     * It's automatically injected by Spring.
     */
    @Autowired
    public FundController(FundService fundService) {
        this.fundService = fundService;
    }

    /**
     * Retrieves a list of all available investment funds offered by GFT.
     * This endpoint fetches all fund records from the system.
     *
     * @return A {@link ResponseEntity} containing a {@link List} of {@link FundResponseDTO}s,
     * representing all available funds, with an HTTP status of {@code 200 OK}.
     *
     * {@code @Operation} provides a summary and description for this API operation in Swagger UI.
     * {@code @ApiResponse} documents the expected HTTP response code and the schema of the response body.
     * {@code @GetMapping} maps HTTP GET requests to this method, specifically to "/api/funds".
     */
    @Operation(summary = "Get all available funds", description = "Retrieves a list of all investment funds offered by GFT.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of funds",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FundResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<FundResponseDTO>> getAllFunds() {
        List<Fund> funds = fundService.getAllFunds();
        // Map Fund entities to FundResponseDTOs for the API response.
        List<FundResponseDTO> fundDTOs = funds.stream()
                .map(fund -> new FundResponseDTO( // Assuming FundResponseDTO has a constructor for these fields
                        fund.getId(),
                        fund.getName(),
                        fund.getProductType(),
                        fund.getMinimumSubscriptionAmount()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(fundDTOs);
    }
}