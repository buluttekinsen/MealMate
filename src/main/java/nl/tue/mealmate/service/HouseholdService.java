package nl.tue.mealmate.service;

import nl.tue.mealmate.domain.Household;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.domain.Role;
import nl.tue.mealmate.repository.HouseholdRepository;

/**
 * Coordinates household management: creating households and inviting members
 * (use case FR-004). It stores households through the {@link HouseholdRepository}
 * abstraction and sends invitation through the {@link InvitationService}
 * abstraction, so storage and delivery can be replaced without changing this class
 */
public class HouseholdService {
    
    private final HouseholdRepository repository;
    private final InvitationService invitationService;
    
    /**
     * Creates a household service
     * 
     * @param repository the repo used to store households
     * @param invitationService the service used to send invitations
     * @pre repository and invitationService must not be null
     * @throws IllegalArgumentException if repository or invitationService is null
     */
    public HouseholdService(HouseholdRepository repository,
            InvitationService invitationService) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }
        if (invitationService == null) {
            throw new IllegalArgumentException("invitationService must not be null");
        }
        this.repository = repository;
        this.invitationService = invitationService;
    }
    
    /**
     * Creates a household with given administrator and stores it
     * 
     * @param name the name of the household
     * @param admin the admin of the household
     * @return the created household
     * @pre name must not be null or blank
     * @pre admin must not be null
     * @post the returned household is contained in repository.findAll()
     * @post the returned household has admin as its administrator
     * @throws IllegalArgumentException if name is null or blank, or admin is null
     */
    public Household createHousehold(String name, Member admin) {
        Household household = new Household(name, admin);
        repository.save(household);
        return household;
    }
    
    /**
     * Adds a member to the household with the given role and sends them an 
     * invitation.
     * 
     * @param household the household to add member to
     * @param invitee the member being invited
     * @param role the role of the invited member
     * @pre household, invitee, and role must not be null
     * @post household.isMember(invitee) && household.getRole(invitee) == role
     * @post an invitation has been sent to the invitee
     * @throws IllegalArgumentException if any argument is null, or the invitee
     * is already a member of the household.
     */
    public void inviteMember(Household household, Member invitee, Role role) {
        if (household == null) {
            throw new IllegalArgumentException("household must not be null");
        }
        household.addMember(invitee, role);
        invitationService.sendInvitation(invitee, household);
    }
    
}