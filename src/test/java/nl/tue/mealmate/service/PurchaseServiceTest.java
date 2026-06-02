package nl.tue.mealmate.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.domain.Purchase;
import nl.tue.mealmate.repository.InMemoryPurchaseRepository;
import nl.tue.mealmate.repository.PurchaseRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PurchaseService} (use case UC-001).
 */
class PurchaseServiceTest {

    /**
     * A test double {@link NotificationService} that records the members it was
     * asked to notify, instead of delivering anything.
     */
    private static final class RecordingNotifier implements NotificationService {
        private final List<Member> notified = new ArrayList<>();

        @Override
        public void notify(Member recipient, String message) {
            notified.add(recipient);
        }
    }

    /** Verifies that a shared purchase is stored in the repository. */
    @Test
    void logPurchase_shared_storesPurchase() {
        PurchaseRepository repo = new InMemoryPurchaseRepository();
        PurchaseService service = new PurchaseService(repo, new RecordingNotifier());
        Member a = new Member("Bulut", "bulut@tue.nl");

        Purchase p = service.logPurchase(30.0, "Groceries", LocalDate.now(),
                true, a, List.of(a));

        assertEquals(1, repo.findAll().size());
        assertEquals(p, repo.findAll().get(0));
    }

    /** Verifies that a shared cost updates the payer's and participants' balances. */
    @Test
    void logPurchase_shared_updatesBalances() {
        PurchaseRepository repo = new InMemoryPurchaseRepository();
        PurchaseService service = new PurchaseService(repo, new RecordingNotifier());
        Member a = new Member("Bulut", "bulut@tue.nl");
        Member b = new Member("Doruk", "doruk@tue.nl");
        Member c = new Member("Serhat", "serhat@tue.nl");

        service.logPurchase(30.0, "Groceries", LocalDate.now(), true, a, List.of(a, b, c));

        assertEquals(20.0, a.getBalance(), 0.0001);   // paid 30, owes own share 10
        assertEquals(-10.0, b.getBalance(), 0.0001);
        assertEquals(-10.0, c.getBalance(), 0.0001);
    }

    /** Verifies that a shared purchase notifies every participant. */
    @Test
    void logPurchase_shared_notifiesParticipants() {
        PurchaseRepository repo = new InMemoryPurchaseRepository();
        RecordingNotifier notifier = new RecordingNotifier();
        PurchaseService service = new PurchaseService(repo, notifier);
        Member a = new Member("Bulut", "bulut@tue.nl");
        Member b = new Member("Doruk", "doruk@tue.nl");

        service.logPurchase(20.0, "Groceries", LocalDate.now(), true, a, List.of(a, b));

        assertEquals(2, notifier.notified.size());
        assertTrue(notifier.notified.contains(a));
        assertTrue(notifier.notified.contains(b));
    }

    /** Verifies that a personal purchase changes no balance and sends no notification. */
    @Test
    void logPurchase_personal_noBalanceChangeNoNotification() {
        PurchaseRepository repo = new InMemoryPurchaseRepository();
        RecordingNotifier notifier = new RecordingNotifier();
        PurchaseService service = new PurchaseService(repo, notifier);
        Member a = new Member("Bulut", "bulut@tue.nl");

        service.logPurchase(15.0, "Snacks", LocalDate.now(), false, a, List.of(a));

        assertEquals(0.0, a.getBalance(), 0.0001);
        assertTrue(notifier.notified.isEmpty());
        assertEquals(1, repo.findAll().size());   // still recorded
    }

    /** Verifies that the constructor rejects a null repository or notification service. */
    @Test
    void constructor_rejectsNullDependencies() {
        PurchaseRepository repo = new InMemoryPurchaseRepository();
        RecordingNotifier notifier = new RecordingNotifier();
        assertThrows(IllegalArgumentException.class, () -> new PurchaseService(null, notifier));
        assertThrows(IllegalArgumentException.class, () -> new PurchaseService(repo, null));
    }

    /** Verifies that an invalid amount is rejected and nothing is stored. */
    @Test
    void logPurchase_invalidAmount_throwsAndStoresNothing() {
        PurchaseRepository repo = new InMemoryPurchaseRepository();
        PurchaseService service = new PurchaseService(repo, new RecordingNotifier());
        Member a = new Member("Bulut", "bulut@tue.nl");

        assertThrows(IllegalArgumentException.class, () ->
                service.logPurchase(-5.0, "Groceries", LocalDate.now(), true, a, List.of(a)));
        assertTrue(repo.findAll().isEmpty());
    }
}