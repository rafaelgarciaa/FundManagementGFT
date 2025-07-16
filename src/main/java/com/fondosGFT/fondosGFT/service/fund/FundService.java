package com.fondosGFT.fondosGFT.service.fund;
import com.fondosGFT.fondosGFT.model.fund.Fund;
import com.fondosGFT.fondosGFT.repository.fund.FundRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class responsible for managing investment fund-related business logic.
 * This class interacts with the {@link FundRepository} to perform operations
 * such as retrieving fund information and initializing sample data.
 * <p>
 * {@code @Service} indicates that this class is a Spring service component,
 * eligible for Spring's component scanning and dependency injection.
 * </p>
 */
@Service
public class FundService {

    private final FundRepository fondoRepository;

    /**
     * Constructs a new FundService with the specified FundRepository.
     * Spring's dependency injection automatically provides the FundRepository instance.
     *
     * @param fondoRepository The repository for accessing and managing fund data in the database.
     */
    @Autowired
    public FundService(FundRepository fondoRepository) {
        this.fondoRepository = fondoRepository;
    }

    /**
     * Initializes the database with sample fund data if the fund collection is empty.
     * This method is automatically executed after the application context is initialized,
     * ensuring that there are initial funds available for testing or demonstration purposes.
     */
    @PostConstruct
    public void initializeFunds() {
        if (fondoRepository.count() == 0) {
            // Sample initial funds with their minimum subscription amounts
            Fund fondo1 = new Fund("1", "Fondo BTG Liquidez", "FPV", new BigDecimal("100000.00")); // $100,000
            Fund fondo2 = new Fund("2", "Fondo BTG Acciones", "FIC", new BigDecimal("250000.00")); // $250,000
            Fund fondo3 = new Fund("3", "Fondo BTG Renta Fija", "FPV", new BigDecimal("150000.00")); // $150,000
            Fund fondo4 = new Fund("4", "Fondo BTG Global", "FIC", new BigDecimal("300000.00")); // $300,000

            fondoRepository.saveAll(List.of(fondo1, fondo2, fondo3, fondo4));
            System.out.println("Initial funds loaded.");
        }
    }

    /**
     * Retrieves a list of all investment funds available in the database.
     *
     * @return A {@link List} of {@link Fund} objects representing all available funds.
     */
    public List<Fund> getAllFunds() {
        return fondoRepository.findAll();
    }

    /**
     * Retrieves a fund by its unique identifier.
     *
     * @param id The unique ID of the fund to retrieve.
     * @return The {@link Fund} object if found.
     * @throws RuntimeException if the fund with the specified ID is not found.
     * (Note: Consider implementing a more specific custom exception for production use.)
     */
    public Fund getFondoById(String id) {
        return fondoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado con ID: " + id)); // Implement a custom exception later
    }
}