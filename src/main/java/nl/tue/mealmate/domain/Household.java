package nl.tue.mealmate.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A household: a group of members who share expenses, with a role for each
 * member. The member who creates the household is the administrator.
 * <p>
 * Class invariant: the household has a non-blank name and always contains at
 * least its administrator.
 */
public class Household {
    
    private final String name;
    private final Map<Member, Role> members = new LinkedHashMap<>();
    
    /**
     * Creates a household with the given name and administrator.
     * 
     * @param name the name of the household
     * @param admin the member who creates and administers the household.
     * @pre name != null && !name.isBlank()
     * @pre admin != null
     * @post getName.equals(name)
     * @post isMember(admin) && getRole(admin) == Role.ADMIN
     * @throws IllegalArgumentException if name is null or blank, or admin is null
     */
    public Household(String name, Member admin) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be null or blank");
        }
        if (admin == null) {
            throw new IllegalArgumentException("admin must not be null");
        }
        this.name = name;
        this.members.put(admin, Role.ADMIN);
    }
    
    /**
     * Adds a member to the household with the given role.
     * 
     * @param member the member to add
     * @param role the role of the member within the household
     * @pre member is not null and role is not null
     * @pre member is not already in the household
     * @post isMember(member) && getRole(member) == role
     * @throws IllegalArgumentException if member or role is null or the member
     * is already in the household
     */
    public void addMember(Member member, Role role) {
        if (member == null) {
            throw new IllegalArgumentException("member must not be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }
        if (isMember(member)) {
            throw new IllegalArgumentException("member is already in the household");
        }
        members.put(member, role);
    }
    
    /**
     * Returns the role of the member within the household.
     * 
     * @param member the member whose role is requested
     * @return the member's role
     * @pre isMember(member)
     * @throws IllegalArgumentException if member is not in the household
     */
    public Role getRole(Member member) {
        if (!isMember(member)) {
            throw new IllegalArgumentException("member is not in the household");
        }
        return members.get(member);
    }
    
    /**
     * Indicates whether the given member belongs to the household.
     * 
     * @param member the member to check
     * @return true if the member is in the household, false otherwise
     */
    public boolean isMember(Member member) {
        return members.containsKey(member);
    }
    
    /** @return the name of the household */
    public String getName() {
        return this.name;
    }
    
    /** 
     * Returns all members of the household
     * 
     * @return an unmodifiable set of the household's members
     */
    public Set<Member> getMembers() {
        return Set.copyOf(members.keySet());
    }
}