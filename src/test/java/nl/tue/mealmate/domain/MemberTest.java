package nl.tue.mealmate.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Member}.
 * <p>
 * Each test verifies a part of the constructor's contract (its preconditions
 * and postconditions) or the behaviour of {@link Member#adjustBalance(double)}.
 */
class MemberTest {

    /** Verifies that a newly created member starts with a zero balance. */
    @Test
    void constructor_startsWithZeroBalance() {
        Member m = new Member("Bulut", "bulut@tue.nl");
        assertEquals(0.0, m.getBalance(), 0.0001);
    }

    /** Verifies that the constructor stores the given name and email. */
    @Test
    void constructor_storesNameAndEmail() {
        Member m = new Member("Bulut", "bulut@tue.nl");
        assertEquals("Bulut", m.getName());
        assertEquals("bulut@tue.nl", m.getEmail());
    }

    /** Verifies that adjustBalance applies positive and negative amounts cumulatively. */
    @Test
    void adjustBalance_appliesDeltasCumulatively() {
        Member m = new Member("Bulut", "bulut@tue.nl");
        m.adjustBalance(15.0);
        m.adjustBalance(-5.0);
        assertEquals(10.0, m.getBalance(), 0.0001);
    }

    /** Verifies that the constructor rejects a null or blank name. */
    @Test
    void constructor_rejectsInvalidName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Member(null, "bulut@tue.nl"));
        assertThrows(IllegalArgumentException.class, () ->
                new Member("   ", "bulut@tue.nl"));
    }

    /** Verifies that the constructor rejects a null or blank email. */
    @Test
    void constructor_rejectsInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () ->
                new Member("Bulut", null));
        assertThrows(IllegalArgumentException.class, () ->
                new Member("Bulut", "   "));
    }
    
    /** Verifies that adjustBalance can drive the balance negative (the member owes money). */
    @Test
    void adjustBalance_canProduceNegativeBalance() {
        Member m = new Member("Bulut", "bulut@tue.nl");
        m.adjustBalance(-10.0);
        assertEquals(-10.0, m.getBalance(), 0.0001);
    }
}