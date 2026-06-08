package nl.tue.mealmate.repository;

import java.util.ArrayList;
import java.util.List;
import nl.tue.mealmate.domain.Household;

public class InMemoryHouseholdRepository implements HouseholdRepository {
    
    private final List<Household> households = new ArrayList<>();
    
    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException if the household is null
     */
    @Override
    public void save(Household household) {
        if (household == null) {
            throw new IllegalArgumentException("household must not be null");
        }
        households.add(household);
    }
    
    /** {@inheritDoc} */
    @Override
    public List<Household> findAll() {
        return List.copyOf(households);
    }
}