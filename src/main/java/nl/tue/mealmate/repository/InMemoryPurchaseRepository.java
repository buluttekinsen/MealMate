package nl.tue.mealmate.repository;

import java.util.ArrayList;
import java.util.List;
import nl.tue.mealmate.domain.Purchase;

/**
 * An in-memory {@link PurchaseRepository} backed by a list.
 * <p>
 * Purchases are kept only for the lifetime of the object; nothing is persisted
 * to disk. This implementation is used during development and testing, and can
 * later be replaced by a file- or database-backed implementation without
 * changing any code that depends on {@link PurchaseRepository}.
 */
public class InMemoryPurchaseRepository implements PurchaseRepository {

    private final List<Purchase> purchases = new ArrayList<>();

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if purchase is null
     */
    @Override
    public void save(Purchase purchase) {
        if (purchase == null) {
            throw new IllegalArgumentException("purchase must not be null");
        }
        purchases.add(purchase);
    }

    /** {@inheritDoc} */
    @Override
    public List<Purchase> findAll() {
        return List.copyOf(purchases);
    }
}