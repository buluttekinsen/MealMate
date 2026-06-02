package nl.tue.mealmate.service;

import nl.tue.mealmate.domain.Member;

/**
 * Delivers a notification to a household member.
 * <p>
 * This is an abstraction over the actual delivery mechanism: an implementation
 * may send an email, show a desktop message, or — for the current desktop
 * build and for testing — simply log the notification.
 */
public interface NotificationService {

    /**
     * Sends a notification message to the given recipient.
     *
     * @param recipient the member to notify
     * @param message   the message to deliver
     * @pre recipient != null
     * @pre message != null && !message.isBlank()
     * @post the message has been delivered to the recipient through the
     *       underlying mechanism
     */
    void notify(Member recipient, String message);
}