package nl.tue.mealmate.service;

import nl.tue.mealmate.domain.Member;

/**
 * A stub {@link NotificationService} that prints notifications to the console
 * instead of sending them over a network.
 * <p>
 * Used for the desktop build and for testing, where real delivery (email, push,
 * etc.) is out of scope. It can later be replaced by a real implementation
 * without changing any code that depends on {@link NotificationService}.
 */
public class ConsoleNotifier implements NotificationService {

    /**
     * {@inheritDoc}
     * <p>
     * This implementation writes the notification to standard output.
     *
     * @throws IllegalArgumentException if recipient is null, or message is null or blank
     */
    @Override
    public void notify(Member recipient, String message) {
        if (recipient == null) {
            throw new IllegalArgumentException("recipient must not be null");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be null or blank");
        }
        System.out.println("[NOTIFICATION] to " + recipient.getEmail() + ": " + message);
    }
}