package nl.tue.mealmate.repository;

import java.time.LocalDate;
import java.util.List;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.domain.Purchase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link InMemoryPurchaseRepository}.
 */
class InMemoryPurchaseRepositoryTest {

    /**
     * Creates a simple valid purchase for use in test fixtures.
     *
     * @param description a short description of the purchase
     * @return a shared purchase of 10.0 paid by a single member
     */
    private Purchase purchase(String description) {
        Member a = new Member("Bulut", "bulut@tue.nl");
        return new Purchase(10.0, description, LocalDate.now(), true, a, List.of(a));
    }

    /** Verifies that a freshly created repository contains no purchases. */
    @Test
    void findAll_isEmptyInitially() {
        InMemoryPurchaseRepository repo = new InMemoryPurchaseRepository();
        assertTrue(repo.findAll().isEmpty());
    }

    /** Verifies that a saved purchase is returned by findAll. */
    @Test
    void save_thenFindAllContainsPurchase() {
        InMemoryPurchaseRepository repo = new InMemoryPurchaseRepository();
        Purchase p = purchase("Groceries");
        repo.save(p);

        List<Purchase> all = repo.findAll();
        assertEquals(1, all.size());
        assertEquals(p, all.get(0));
    }

    /** Verifies that purchases are returned in insertion order. */
    @Test
    void findAll_preservesInsertionOrder() {
        InMemoryPurchaseRepository repo = new InMemoryPurchaseRepository();
        Purchase first = purchase("First");
        Purchase second = purchase("Second");
        repo.save(first);
        repo.save(second);

        List<Purchase> all = repo.findAll();
        assertEquals(first, all.get(0));
        assertEquals(second, all.get(1));
    }

    /** Verifies that save rejects a null purchase. */
    @Test
    void save_rejectsNull() {
        InMemoryPurchaseRepository repo = new InMemoryPurchaseRepository();
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }

    /** Verifies that the list returned by findAll cannot be modified. */
    @Test
    void findAll_isUnmodifiable() {
        InMemoryPurchaseRepository repo = new InMemoryPurchaseRepository();
        assertThrows(UnsupportedOperationException.class, () ->
                repo.findAll().add(purchase("Groceries")));
    }
    
    /** Verifies that findAll returns an independent snapshot, not a live view. */
    @Test
    void findAll_returnsIndependentSnapshot() {
        InMemoryPurchaseRepository repo = new InMemoryPurchaseRepository();
        repo.save(purchase("First"));

        List<Purchase> snapshot = repo.findAll();
        repo.save(purchase("Second"));

        assertEquals(1, snapshot.size());
        assertEquals(2, repo.findAll().size());
    }
}