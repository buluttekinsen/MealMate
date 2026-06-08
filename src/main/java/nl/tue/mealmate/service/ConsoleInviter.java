package nl.tue.mealmate.service;

import nl.tue.mealmate.domain.Household;
import nl.tue.mealmate.domain.Member;

/**
 * A stub {@link InvitationService} that prints invitations to the console
 * instead of sending them over a network
 * <p>
 * Used for the desktop build and testing; it can later be replaced by a 
 * real email-based implementation without changing any code that depends on 
 * {@link InvitationService}.
 */
public class ConsoleInviter implements InvitationService {
    
    /**
     * {@inheritDoc}
     * <p>
     * This implementation writes the invitation to standard output
     * 
     * @throws IllegalArgumentException if invitee or household is null
     */
    @Override
    public void sendInvitation(Member invitee, Household household) {
        if (invitee == null) {
            throw new IllegalArgumentException("invitee must not be null");
        }
        if (household == null) {
            throw new IllegalArgumentException("household must not be null");
        }
        System.out.println("[INVITATION] to " + invitee.getEmail() + 
                ": you are invited to join \"" + household .getName() + "\"." );
    }
}