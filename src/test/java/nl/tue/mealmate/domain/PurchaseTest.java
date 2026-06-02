package nl.tue.mealmate.domain;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

/**
 * Unit tests for {@link Purchase}.
 * <p>
 * Each test verifies either a requirement (e.g. FR-002 equal splitting) or a
 * part of the constructor's contract (its preconditions and the defensive copy).
 */
class PurchaseTest {

    /**
     * Creates a valid member for use in test fixtures.
     *
     * @param name the member's display name
     * @return a member with the given name and a derived test email address
     */
    private Member member(String name) {
        return new Member(name, name.toLowerCase() + "@tue.nl");
    }

    /** Verifies that a shared cost is split equally among all participants (FR-002). */
    @Test
    void sharePerMember_splitsEqually() {
        Member a = member("Bulut");
        Member b = member("Doruk");
        Member c = member("Serhat");
        Purchase p = new Purchase(30.0, "Groceries", LocalDate.now(),
                true, a, List.of(a, b, c));

        assertEquals(10.0, p.sharePerMember(), 0.0001);
    }

    /** Verifies that the constructor rejects a zero or negative amount. */
    @Test
    void constructor_rejectsNonPositiveAmount() {
        Member a = member("Bulut");
        List<Member> participants = List.of(a);

        assertThrows(IllegalArgumentException.class, () ->
                new Purchase(0.0, "Groceries", LocalDate.now(), false, a, participants));
        assertThrows(IllegalArgumentException.class, () ->
                new Purchase(-5.0, "Groceries", LocalDate.now(), false, a, participants));
    }

    /** Verifies that the constructor rejects a null or blank description. */
    @Test
    void constructor_rejectsInvalidDescription() {
        Member a = member("Bulut");
        assertThrows(IllegalArgumentException.class, () ->
                new Purchase(10.0, null, LocalDate.now(), false, a, List.of(a)));
        assertThrows(IllegalArgumentException.class, () ->
                new Purchase(10.0, "   ", LocalDate.now(), false, a, List.of(a)));
    }

    /** Verifies that the constructor rejects a null date. */
    @Test
    void constructor_rejectsNullDate() {
        Member a = member("Bulut");
        assertThrows(IllegalArgumentException.class, () ->
                new Purchase(10.0, "Groceries", null, false, a, List.of(a)));
    }

    /** Verifies that the constructor rejects a null payer. */
    @Test
    void constructor_rejectsNullPayer() {
        Member a = member("Bulut");
        assertThrows(IllegalArgumentException.class, () ->
                new Purchase(10.0, "Groceries", LocalDate.now(), false, null, List.of(a)));
    }

    /** Verifies that the constructor rejects a null or empty participant list. */
    @Test
    void constructor_rejectsInvalidParticipants() {
        Member a = member("Bulut");
        assertThrows(IllegalArgumentException.class, () ->
                new Purchase(10.0, "Groceries", LocalDate.now(), true, a, null));
        assertThrows(IllegalArgumentException.class, () ->
                new Purchase(10.0, "Groceries", LocalDate.now(), true, a, List.of()));
    }

    /** Verifies that the participant list returned by the getter cannot be modified. */
    @Test
    void getParticipants_isUnmodifiable() {
        Member a = member("Bulut");
        Member b = member("Doruk");
        Purchase p = new Purchase(20.0, "Groceries", LocalDate.now(),
                true, a, List.of(a, b));

        assertThrows(UnsupportedOperationException.class, () ->
                p.getParticipants().add(member("Kuzey")));
    }
    
    /** Verifies that the getters return the values passed to the constructor. */
    @Test
    void getters_returnConstructorValues() {
        Member a = member("Bulut");
        Member b = member("Doruk");
        LocalDate date = LocalDate.of(2026, 6, 2);
        Purchase p = new Purchase(25.0, "Groceries", date, true, a, List.of(a, b));

        assertEquals(25.0, p.getAmount(), 0.0001);
        assertEquals("Groceries", p.getDescription());
        assertEquals(date, p.getDate());
        assertTrue(p.isShared());
        assertEquals(a, p.getPayer());
        assertEquals(List.of(a, b), p.getParticipants());
    }

    /** Verifies that the share equals the full amount when there is one participant. */
    @Test
    void sharePerMember_singleParticipant() {
        Member a = member("Bulut");
        Purchase p = new Purchase(10.0, "Groceries", LocalDate.now(),
                false, a, List.of(a));

        assertEquals(10.0, p.sharePerMember(), 0.0001);
    }

    /** Verifies the per-member share for an amount that does not divide evenly. */
    @Test
    void sharePerMember_unevenSplit() {
        Member a = member("Bulut");
        Member b = member("Doruk");
        Member c = member("Serhat");
        Purchase p = new Purchase(10.0, "Groceries", LocalDate.now(),
                true, a, List.of(a, b, c));

        assertEquals(3.3333, p.sharePerMember(), 0.0001);
    }

    /** Verifies that mutating the original list after construction does not affect the purchase. */
    @Test
    void constructor_makesDefensiveCopyOfParticipants() {
        Member a = member("Bulut");
        Member b = member("Doruk");
        List<Member> source = new ArrayList<>(List.of(a, b));
        Purchase p = new Purchase(20.0, "Groceries", LocalDate.now(),
                true, a, source);

        source.add(member("Kuzey"));   // mutate the original list after construction

        assertEquals(2, p.getParticipants().size());
    }
}