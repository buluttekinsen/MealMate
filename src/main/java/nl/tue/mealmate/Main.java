package nl.tue.mealmate;

import java.time.LocalDate;
import java.util.List;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.repository.InMemoryPurchaseRepository;
import nl.tue.mealmate.repository.PurchaseRepository;
import nl.tue.mealmate.service.ConsoleNotifier;
import nl.tue.mealmate.service.NotificationService;
import nl.tue.mealmate.service.PurchaseService;

/**
 * Application entry point and composition root.
 * <p>
 * This is the single place that chooses the concrete implementations
 * ({@link InMemoryPurchaseRepository}, {@link ConsoleNotifier}) and wires them
 * into the {@link PurchaseService}. Every other class depends only on the
 * abstractions, so swapping an implementation here changes nothing elsewhere.
 * <p>
 * For now it runs a small demonstration of use case UC-001 (Log Purchase).
 */
public final class Main {

    private Main() {
        // Utility class: not meant to be instantiated.
    }

    /**
     * Runs a demonstration of logging a shared purchase.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        // 1. Choose the concrete implementations (composition root).
        PurchaseRepository repository = new InMemoryPurchaseRepository();
        NotificationService notifier = new ConsoleNotifier();
        PurchaseService service = new PurchaseService(repository, notifier);

        // 2. Set up a household of three members.
        Member bulut = new Member("Bulut", "bulut@tue.nl");
        Member doruk = new Member("Doruk", "doruk@tue.nl");
        Member serhat = new Member("Serhat", "serhat@tue.nl");

        // 3. Log a shared purchase of 30.0 paid by Bulut, split three ways.
        service.logPurchase(30.0, "Groceries", LocalDate.now(),
                true, bulut, List.of(bulut, doruk, serhat));

        // 4. Show the resulting balances.
        System.out.println();
        System.out.println("Balances after the purchase:");
        for (Member member : List.of(bulut, doruk, serhat)) {
            System.out.println("  " + member.getName() + ": " + member.getBalance());
        }

        // 5. Show the expense summary (all stored purchases).
        System.out.println();
        System.out.println("Expense summary (" + repository.findAll().size() + " purchase(s)):");
        repository.findAll().forEach(p ->
                System.out.println("  " + p.getDate() + " - " + p.getDescription()
                        + " - " + p.getAmount() + " paid by " + p.getPayer().getName()));
    }
}