package nl.tue.mealmate.domain;
/**
 * Represents a housemate (member) of a household.
 * <p>
 * Class invariant: name and email are non-null and non-blank for the
 * entire lifetime of the object.
 */
public class Member {

    private final String name;   // display name, fixed after creation
    private final String email;  // used for invitations and notifications
    private double balance;      // net balance: positive = is owed, negative = owes

    /**
     * Creates a new member with a zero starting balance.
     *
     * @param name  the member's display name
     * @param email the member's email address
     * @pre name != null && !name.isBlank()
     * @pre email != null && !email.isBlank()
     * @post getBalance() == 0.0
     * @post getName().equals(name) && getEmail().equals(email)
     * @throws IllegalArgumentException if name or email is null or blank
     */
    public Member(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("email must not be null or blank");
        }
        this.name = name;
        this.email = email;
        this.balance = 0.0;
    }

    /** @return the member's display name */
    public String getName() { return name; }

    /** @return the member's email address */
    public String getEmail() { return email; }

    /** @return the member's current net balance (positive = is owed, negative = owes) */
    public double getBalance() { return balance; }

    /**
     * Adjusts the member's balance by the given amount.
     *
     * @param delta the amount to add to the balance; positive increases what
     *              the member is owed, negative decreases it
     * @post getBalance() equals its previous value plus delta
     */
    public void adjustBalance(double delta) {
        this.balance += delta;
    }
    
    /**
     * Indicates if this member is equal to another object. Two members are 
     * equal if they have the same email, which uniquely identifies a member
     * 
     * @param obj the object to compare with
     * @return true if obj is a member with the same email, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Member other)) {
            return false;
        }
        return email.equals(other.email);
    }
    
    /**
     * Returns a hash code consistent with {@link #equals(Object)}, based on the
     * member's email
     * 
     * @return this member's hash code
     */
    @Override
    public int hashCode() {
        return email.hashCode();
    }
}