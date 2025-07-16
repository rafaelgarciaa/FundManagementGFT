package com.fondosGFT.fondosGFT.repository.client;

import com.fondosGFT.fondosGFT.model.client.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Client} entities in MongoDB.
 * Extends {@link MongoRepository} to provide standard CRUD operations
 * and pagination functionalities.
 * <p>
 * This interface also defines custom query methods for specific client data retrieval needs.
 * </p>
 */
@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

    /**
     * Retrieves a client by their email address.
     * Spring Data MongoDB automatically generates the query from the method name.
     *
     * @param email The email address of the client to find.
     * @return An {@link Optional} containing the {@link Client} if found, or empty if not.
     */
    Optional<Client> findByEmail(String email);

    /**
     * Retrieves a list of clients whose current balance is greater than or equal to the specified amount.
     * Spring Data MongoDB automatically generates the query from the method name.
     *
     * @param amount The minimum balance amount to filter by.
     * @return A {@link List} of {@link Client} objects matching the criteria.
     */
    List<Client> findByCurrentBalanceGreaterThanEqual(BigDecimal amount);

    /**
     * Retrieves a list of clients who have at least one active investment in a specific fund.
     * This query searches within the nested 'activeInvestments' array for a matching 'fundId'.
     *
     * @param fundId The ID of the fund to check for active investments.
     * @return A {@link List} of {@link Client} objects with active investments in the specified fund.
     */
    @Query("{ 'activeInvestments.fundId' : ?0 }")
    List<Client> findByActiveInvestments_FundId(String fundId);

    /**
     * Retrieves a list of clients who do not have any active investments.
     * This query uses the MongoDB '$size' operator to check if the 'activeInvestments' array is empty.
     *
     * @return A {@link List} of {@link Client} objects with no active investments.
     */
    @Query("{ 'activeInvestments' : { $size : 0 } }")
    List<Client> findByActiveInvestmentsEmpty();

    /**
     * Retrieves a list of clients based on their notification preference.
     * Spring Data MongoDB automatically generates the query from the method name.
     *
     * @param notificationType The {@link com.pruebagft.gestionFondosGFT.util.enums.NotificationType} to filter by.
     * @return A {@link List} of {@link Client} objects matching the specified notification preference.
     */
    List<Client> findByNotificationPreference(com.pruebagft.gestionFondosGFT.util.enums.NotificationType notificationType);

    /**
     * Retrieves a list of clients whose first name contains the given string, ignoring case.
     *
     * @param firstNamePart The part of the first name to search for.
     * @return A {@link List} of {@link Client} objects matching the criteria.
     */
    List<Client> findByFirstNameContainingIgnoreCase(String firstNamePart);

    /**
     * Retrieves a list of clients whose last name contains the given string, ignoring case.
     *
     * @param lastNamePart The part of the last name to search for.
     * @return A {@link List} of {@link Client} objects matching the criteria.
     */
    List<Client> findByLastNameContainingIgnoreCase(String lastNamePart);


    /**
     * Retrieves a list of clients who have at least one active investment
     * where the initial amount invested in that specific investment is greater than the given value.
     * This query searches within the nested 'activeInvestments' array with a condition on 'initialAmountInvested'.
     *
     * @param amount The minimum initial investment amount to filter by.
     * @return A {@link List} of {@link Client} objects matching the investment criteria.
     */
    @Query("{ 'activeInvestments.initialAmountInvested' : { $gt : ?0 } }")
    List<Client> findByActiveInvestments_InitialAmountInvestedGreaterThan(BigDecimal amount);
}