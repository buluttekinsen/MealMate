package nl.tue.mealmate.service;

import java.time.LocalDate;
import java.util.List;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.domain.Purchase;
import nl.tue.mealmate.repository.PurchaseRepository;

/**
 * Coordinates the logging of purchases: it stores the purchase, splits a shared
 * cost across the participants, updates their balances and notifies them.
 * <p>
 * This is the implementation of use case UC-001 (Log Purchase). It depends only
 * on the {@link PurchaseRepository} and {@link NotificationService} abstractions,
 * so the storage and notification mechanisms can be replaced without changing
 * this class.
 */
public class PurchaseService {

    private final PurchaseRepository repository;
    private final NotificationService notificationService;

    /**
     * Creates a purchase service.
     *
     * @param repository          the repository used to store purchases
     * @param notificationService the service used to notify members
     * @pre repository != null
     * @pre notificationService != null
     * @throws IllegalArgumentException if repository or notificationService is null
     */
    public PurchaseService(PurchaseRepository repository,
                           NotificationService notificationService) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }
        if (notificationService == null) {
            throw new IllegalArgumentException("notificationService must not be null");
        }
        this.repository = repository;
        this.notificationService = notificationService;
    }

    /**
     * Logs a purchase (UC-001). The purchase is stored; if it is shared, the cost
     * is split equally among the participants, their balances are updated and
     * they are notified.
     *
     * @param amount       the total amount paid, in euros
     * @param description  a short description of what was bought
     * @param date         the date on which the purchase was made
     * @param shared       true if the cost is shared, false if personal
     * @param payer        the member who paid
     * @param participants the members the cost is split among
     * @return the stored purchase
     * @pre all arguments satisfy the preconditions of the Purchase constructor
     * @post the returned purchase is contained in repository.findAll()
     * @post if shared, the payer's balance is increased by amount and each
     *       participant's balance is decreased by the equal share
     * @post if shared, every participant has been notified
     * @post if not shared, no balance is changed and no member is notified
     * @throws IllegalArgumentException if any argument is invalid (see Purchase)
     */
    public Purchase logPurchase(double amount, String description, LocalDate date,
                                boolean shared, Member payer, List<Member> participants) {
        Purchase purchase =
                new Purchase(amount, description, date, shared, payer, participants);
        repository.save(purchase);

        if (shared) {
            double share = purchase.sharePerMember();
            payer.adjustBalance(amount);
            for (Member participant : purchase.getParticipants()) {
                participant.adjustBalance(-share);
                notificationService.notify(participant,
                        payer.getName() + " logged a shared purchase of " + amount
                                + " for \"" + description + "\". Your share is " + share + ".");
            }
        }

        return purchase;
    }
}