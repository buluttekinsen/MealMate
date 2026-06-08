package nl.tue.mealmate.service;

import nl.tue.mealmate.domain.Household;
import nl.tue.mealmate.domain.Member;

/**
 * Sends invitation to prospective household members.
 * <p> 
 * This abstracts the actual delivery mechanism (typically an email): an 
 * implementation may send a real email or, for the desktop build and tests,
 * simply log the invitation.
 */
public interface InvitationService {
    
    /**
     * Sends an invitation to the given member to join the household.
     * 
     * @param invitee the member being invited
     * @param household the household the member is invited to join
     * @pre invitee is not null
     * @pre household is not null
     * @post an invitation has been delivered to the invitee through the
     * underlying mechanism
     */
    void sendInvitation(Member invitee, Household household);
}