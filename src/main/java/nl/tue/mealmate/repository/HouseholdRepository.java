package nl.tue.mealmate.repository;

import java.util.List;
import nl.tue.mealmate.domain.Household;

/**
 * Stores and retrieves households.
 * <p>
 * This abstracts the storage mechanism: an implementation may keep households
 * in memory, in a file, or in a database, without affecting the code that 
 * depends on this interface.
 */
public interface HouseholdRepository {
    
    /**
     * Stores the given household.
     * 
     * @param household the household to store
     * @pre household is not null
     * @post the household is contained in the result of findAll()
     */
    void save(Household household);
    
    /**
     * Returns all stored households
     * 
     * @return an unmodifiable list of all stored households, in insertion order
     * @post the result is never null
     */
    List<Household> findAll();
}