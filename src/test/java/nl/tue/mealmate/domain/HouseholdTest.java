package nl.tue.mealmate.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Household}.
 */
class HouseholdTest {

    /** Verifies that the creator becomes a member with the ADMIN role. */
    @Test
    void constructor_makesCreatorAdmin() {
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Household household = new Household("Student House 12", admin);

        assertEquals("Student House 12", household.getName());
        assertTrue(household.isMember(admin));
        assertEquals(Role.ADMIN, household.getRole(admin));
    }

    /** Verifies that the constructor rejects a null or blank name. */
    @Test
    void constructor_rejectsInvalidName() {
        Member admin = new Member("Bulut", "bulut@tue.nl");
        assertThrows(IllegalArgumentException.class, () -> new Household(null, admin));
        assertThrows(IllegalArgumentException.class, () -> new Household("   ", admin));
    }

    /** Verifies that the constructor rejects a null admin. */
    @Test
    void constructor_rejectsNullAdmin() {
        assertThrows(IllegalArgumentException.class, () -> new Household("House", null));
    }

    /** Verifies that an added member is in the household with the given role. */
    @Test
    void addMember_addsWithRole() {
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Member doruk = new Member("Doruk", "doruk@tue.nl");
        Household household = new Household("House", admin);

        household.addMember(doruk, Role.MEMBER);

        assertTrue(household.isMember(doruk));
        assertEquals(Role.MEMBER, household.getRole(doruk));
        assertEquals(2, household.getMembers().size());
    }

    /** Verifies that addMember rejects null arguments. */
    @Test
    void addMember_rejectsNullArguments() {
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Member doruk = new Member("Doruk", "doruk@tue.nl");
        Household household = new Household("House", admin);

        assertThrows(IllegalArgumentException.class, () -> household.addMember(null, Role.MEMBER));
        assertThrows(IllegalArgumentException.class, () -> household.addMember(doruk, null));
    }

    /** Verifies that addMember rejects a member who is already in the household. */
    @Test
    void addMember_rejectsDuplicate() {
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Household household = new Household("House", admin);

        assertThrows(IllegalArgumentException.class, () -> household.addMember(admin, Role.MEMBER));
    }

    /** Verifies that getRole rejects a member who is not in the household. */
    @Test
    void getRole_rejectsNonMember() {
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Member stranger = new Member("Kuzey", "kuzey@tue.nl");
        Household household = new Household("House", admin);

        assertThrows(IllegalArgumentException.class, () -> household.getRole(stranger));
    }

    /** Verifies that isMember returns false for a non-member. */
    @Test
    void isMember_falseForNonMember() {
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Member stranger = new Member("Kuzey", "kuzey@tue.nl");
        Household household = new Household("House", admin);

        assertFalse(household.isMember(stranger));
    }

    /** Verifies that the set returned by getMembers cannot be modified. */
    @Test
    void getMembers_isUnmodifiable() {
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Household household = new Household("House", admin);

        assertThrows(UnsupportedOperationException.class, () ->
                household.getMembers().add(new Member("Kuzey", "kuzey@tue.nl")));
    }
}