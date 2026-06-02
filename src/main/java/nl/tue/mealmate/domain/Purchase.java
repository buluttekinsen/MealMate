package nl.tue.mealmate.domain;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents an immutable grocery purchase logged by a member.
 * <p>
 * Class invariant: amount > 0 and participants is a non-empty,
 * unmodifiable list for the entire lifetime of the object.
 */
public class Purchase {

    private final double amount;             // total amount paid, in euros
    private final String description;        // what was bought
    private final LocalDate date;            // date of the purchase
    private final boolean shared;            // true = shared, false = personal
    private final Member payer;              // the member who paid
    private final List<Member> participants; // members the cost is split among

    /**
     * Creates a purchase and validates its arguments.
     *
     * @param amount       the total amount paid, in euros
     * @param description  a short description of what was bought
     * @param date         the date on which the purchase was made
     * @param shared       true if the cost is shared among members, false if personal
     * @param payer        the member who paid for the purchase
     * @param participants the members the cost is split among
     * @pre amount > 0
     * @pre description != null && !description.isBlank()
     * @pre date != null
     * @pre payer != null
     * @pre participants != null && !participants.isEmpty()
     * @post the new purchase stores the given amount, description, date,
     *       shared flag and payer
     * @post getParticipants() is an unmodifiable copy of participants
     * @throws IllegalArgumentException if any precondition is violated
     */
    public Purchase(double amount, String description, LocalDate date,
                    boolean shared, Member payer, List<Member> participants) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description must not be null or blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        if (payer == null) {
            throw new IllegalArgumentException("payer must not be null");
        }
        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("participants must not be null or empty");
        }
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.shared = shared;
        this.payer = payer;
        this.participants = List.copyOf(participants); // defensive, unmodifiable copy
    }

    /** @return the total amount paid, in euros */
    public double getAmount() { return amount; }

    /** @return a short description of what was bought */
    public String getDescription() { return description; }

    /** @return the date on which the purchase was made */
    public LocalDate getDate() { return date; }

    /** @return true if the cost is shared among members, false if personal */
    public boolean isShared() { return shared; }

    /** @return the member who paid for the purchase */
    public Member getPayer() { return payer; }

    /** @return an unmodifiable list of the members the cost is split among */
    public List<Member> getParticipants() { return participants; }

    /**
     * Computes the equal share of the cost per participant (FR-002).
     *
     * @pre getParticipants() is non-empty (guaranteed by the constructor)
     * @post the returned value equals amount / participants.size()
     * @return the amount each participant owes for this purchase
     */
    public double sharePerMember() {
        return amount / participants.size();
    }
}