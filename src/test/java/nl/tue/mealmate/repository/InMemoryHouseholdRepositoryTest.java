package nl.tue.mealmate.repository;

import java.util.List;
import nl.tue.mealmate.domain.Household;
import nl.tue.mealmate.domain.Member;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link InMemoryHouseholdRepository}.
 */
class InMemoryHouseholdRepositoryTest {

    /**
     * Creates a simple valid household for use in test fixtures.
     *
     * @param name the household name
     * @return a household with the given name and a single admin member
     */
    private Household household(String name) {
        return new Household(name, new Member("Bulut", "bulut@tue.nl"));
    }

    /** Verifies that a freshly created repository contains no households. */
    @Test
    void findAll_isEmptyInitially() {
        InMemoryHouseholdRepository repo = new InMemoryHouseholdRepository();
        assertTrue(repo.findAll().isEmpty());
    }

    /** Verifies that a saved household is returned by findAll. */
    @Test
    void save_thenFindAllContainsHousehold() {
        InMemoryHouseholdRepository repo = new InMemoryHouseholdRepository();
        Household h = household("House 12");
        repo.save(h);

        List<Household> all = repo.findAll();
        assertEquals(1, all.size());
        assertEquals(h, all.get(0));
    }

    /** Verifies that save rejects a null household. */
    @Test
    void save_rejectsNull() {
        InMemoryHouseholdRepository repo = new InMemoryHouseholdRepository();
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }

    /** Verifies that the list returned by findAll cannot be modified. */
    @Test
    void findAll_isUnmodifiable() {
        InMemoryHouseholdRepository repo = new InMemoryHouseholdRepository();
        assertThrows(UnsupportedOperationException.class, () ->
                repo.findAll().add(household("House 12")));
    }
}