package nl.tue.mealmate.repository;

import java.util.List;
import nl.tue.mealmate.domain.Purchase;

/**
 * Stores and retrieves purchases.
 * <p>
 * This is an abstraction over the storage mechanism: an implementation may keep
 * purchases in memory, in a file, or in a database, without affecting the code
 * that depends on this interface.
 */
public interface PurchaseRepository {

    /**
     * Stores the given purchase.
     *
     * @param purchase the purchase to store
     * @pre purchase != null
     * @post the purchase is contained in the result of findAll()
     */
    void save(Purchase purchase);

    /**
     * Returns all stored purchases.
     *
     * @return an unmodifiable list of all stored purchases, in insertion order
     * @post the result is never null
     */
    List<Purchase> findAll();
}