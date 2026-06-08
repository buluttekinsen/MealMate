package nl.tue.mealmate.service;

import java.util.ArrayList;
import java.util.List;
import nl.tue.mealmate.domain.Household;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.domain.Role;
import nl.tue.mealmate.repository.HouseholdRepository;
import nl.tue.mealmate.repository.InMemoryHouseholdRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link HouseholdService} (use case FR-004).
 */
class HouseholdServiceTest {

    /**
     * A test double {@link InvitationService} that records the members it was
     * asked to invite, instead of delivering anything.
     */
    private static final class RecordingInviter implements InvitationService {
        private final List<Member> invited = new ArrayList<>();

        @Override
        public void sendInvitation(Member invitee, Household household) {
            invited.add(invitee);
        }
    }

    /** Verifies that createHousehold stores the household with the creator as ADMIN. */
    @Test
    void createHousehold_storesHouseholdWithAdmin() {
        HouseholdRepository repo = new InMemoryHouseholdRepository();
        HouseholdService service = new HouseholdService(repo, new RecordingInviter());
        Member admin = new Member("Bulut", "bulut@tue.nl");

        Household household = service.createHousehold("House 12", admin);

        assertEquals(1, repo.findAll().size());
        assertEquals(household, repo.findAll().get(0));
        assertEquals(Role.ADMIN, household.getRole(admin));
    }

    /** Verifies that inviteMember adds the member with the given role. */
    @Test
    void inviteMember_addsMemberWithRole() {
        HouseholdRepository repo = new InMemoryHouseholdRepository();
        HouseholdService service = new HouseholdService(repo, new RecordingInviter());
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Member doruk = new Member("Doruk", "doruk@tue.nl");
        Household household = service.createHousehold("House 12", admin);

        service.inviteMember(household, doruk, Role.MEMBER);

        assertTrue(household.isMember(doruk));
        assertEquals(Role.MEMBER, household.getRole(doruk));
    }

    /** Verifies that inviteMember sends an invitation to the invitee. */
    @Test
    void inviteMember_sendsInvitation() {
        HouseholdRepository repo = new InMemoryHouseholdRepository();
        RecordingInviter inviter = new RecordingInviter();
        HouseholdService service = new HouseholdService(repo, inviter);
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Member doruk = new Member("Doruk", "doruk@tue.nl");
        Household household = service.createHousehold("House 12", admin);

        service.inviteMember(household, doruk, Role.MEMBER);

        assertEquals(1, inviter.invited.size());
        assertTrue(inviter.invited.contains(doruk));
    }

    /** Verifies that inviting an existing member is rejected and sends no invitation. */
    @Test
    void inviteMember_rejectsExistingMember() {
        HouseholdRepository repo = new InMemoryHouseholdRepository();
        RecordingInviter inviter = new RecordingInviter();
        HouseholdService service = new HouseholdService(repo, inviter);
        Member admin = new Member("Bulut", "bulut@tue.nl");
        Household household = service.createHousehold("House 12", admin);

        assertThrows(IllegalArgumentException.class, () ->
                service.inviteMember(household, admin, Role.MEMBER));
        assertTrue(inviter.invited.isEmpty());
    }

    /** Verifies that the constructor rejects null dependencies. */
    @Test
    void constructor_rejectsNullDependencies() {
        HouseholdRepository repo = new InMemoryHouseholdRepository();
        RecordingInviter inviter = new RecordingInviter();
        assertThrows(IllegalArgumentException.class, () -> new HouseholdService(null, inviter));
        assertThrows(IllegalArgumentException.class, () -> new HouseholdService(repo, null));
    }
}