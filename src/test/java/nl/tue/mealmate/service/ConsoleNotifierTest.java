package nl.tue.mealmate.service;

import nl.tue.mealmate.domain.Member;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ConsoleNotifier}, limited to its input contract.
 */
class ConsoleNotifierTest {

    /** Verifies that notify rejects a null recipient. */
    @Test
    void notify_rejectsNullRecipient() {
        ConsoleNotifier notifier = new ConsoleNotifier();
        assertThrows(IllegalArgumentException.class, () ->
                notifier.notify(null, "Hello"));
    }

    /** Verifies that notify rejects a null or blank message. */
    @Test
    void notify_rejectsInvalidMessage() {
        ConsoleNotifier notifier = new ConsoleNotifier();
        Member m = new Member("Bulut", "bulut@tue.nl");
        assertThrows(IllegalArgumentException.class, () -> notifier.notify(m, null));
        assertThrows(IllegalArgumentException.class, () -> notifier.notify(m, "   "));
    }
}