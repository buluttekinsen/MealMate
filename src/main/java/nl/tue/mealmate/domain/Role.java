package nl.tue.mealmate.domain;

/**
 * The role a member can have within the household
 */
public enum Role {
    
    /** Can manage the household and invite members. */
    ADMIN,
    
    /** A regular housemate who can log purchases and manage shared data */
    MEMBER,
    
    /** An external user with read-only access to household info */ 
    GUEST
}