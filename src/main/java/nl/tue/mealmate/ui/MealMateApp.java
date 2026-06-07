package nl.tue.mealmate.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.repository.InMemoryPurchaseRepository;
import nl.tue.mealmate.repository.PurchaseRepository;
import nl.tue.mealmate.service.ConsoleNotifier;
import nl.tue.mealmate.service.NotificationService;
import nl.tue.mealmate.service.PurchaseService;

/**
 * JavaFX desktop UI for MealMate. Currently provides a simple "Log Purchase"
 * screen (UC-001) that delegates all logic to {@link PurchaseService}.
 */
public class MealMateApp extends Application {

    private final List<Member> members = List.of(
            new Member("Bulut", "bulut@tue.nl"),
            new Member("Doruk", "doruk@tue.nl"),
            new Member("Serhat", "serhat@tue.nl"));

    private PurchaseService service;
    private final List<CheckBox> participantBoxes = new ArrayList<>();
    private Label balancesLabel;

    /**
     * Builds and shows the Log Purchase screen. Called by the JavaFX runtime
     * when the application is launched.
     *
     * @param stage the primary stage supplied by the JavaFX runtime
     * @pre stage != null (guaranteed by the JavaFX runtime)
     * @post the Log Purchase screen is displayed on the given stage
     */
    @Override
    public void start(Stage stage) {
        // Composition root: choose the concrete implementations.
        PurchaseRepository repository = new InMemoryPurchaseRepository();
        NotificationService notifier = new ConsoleNotifier();
        service = new PurchaseService(repository, notifier);

        TextField amountField = new TextField();
        amountField.setPromptText("Amount, e.g. 30.00");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description, e.g. Groceries");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        CheckBox sharedBox = new CheckBox("Shared purchase");
        sharedBox.setSelected(true);

        ComboBox<Member> payerBox = new ComboBox<>();
        payerBox.getItems().addAll(members);
        payerBox.getSelectionModel().selectFirst();
        payerBox.setConverter(new StringConverter<Member>() {
            /**
             * Returns the display text for a member in the combo box.
             *
             * @param m the member to render, or null
             * @return the member's name, or an empty string if m is null
             */
            @Override
            public String toString(Member m) {
                return m == null ? "" : m.getName();
            }

            /**
             * Not used: the combo box is not editable, so no conversion from
             * text back to a member is required.
             *
             * @param s the text to convert
             * @return always null
             */
            @Override
            public Member fromString(String s) {
                return null;
            }
        });

        VBox participantsBox = new VBox(4, new Label("Split with:"));
        for (Member m : members) {
            CheckBox cb = new CheckBox(m.getName());
            cb.setUserData(m);
            cb.setSelected(true);
            participantBoxes.add(cb);
            participantsBox.getChildren().add(cb);
        }

        Button logButton = new Button("Log purchase");
        balancesLabel = new Label();
        refreshBalances();

        logButton.setOnAction(e -> handleLog(
                amountField, descriptionField, datePicker, sharedBox, payerBox));

        VBox root = new VBox(10,
                new Label("Log a purchase"),
                amountField, descriptionField, datePicker, sharedBox,
                new Label("Paid by:"), payerBox,
                participantsBox, logButton,
                new Label("Balances:"), balancesLabel);
        root.setPadding(new Insets(16));

        stage.setScene(new Scene(root, 360, 520));
        stage.setTitle("MealMate");
        stage.show();
    }

    /**
     * Reads the form, logs the purchase through the service and refreshes the
     * displayed balances. Shows an error dialog if the input is invalid.
     *
     * @param amountField      the field holding the amount text
     * @param descriptionField the field holding the description
     * @param datePicker       the picker holding the purchase date
     * @param sharedBox        the checkbox indicating a shared purchase
     * @param payerBox         the combo box holding the selected payer
     * @post if the input is valid, the purchase is stored and the balances are
     *       refreshed; otherwise an error dialog is shown and nothing is stored
     */
    private void handleLog(TextField amountField, TextField descriptionField,
                           DatePicker datePicker, CheckBox sharedBox,
                           ComboBox<Member> payerBox) {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            List<Member> participants = new ArrayList<>();
            for (CheckBox cb : participantBoxes) {
                if (cb.isSelected()) {
                    participants.add((Member) cb.getUserData());
                }
            }
            service.logPurchase(amount, descriptionField.getText().trim(),
                    datePicker.getValue(), sharedBox.isSelected(),
                    payerBox.getValue(), participants);

            refreshBalances();
            showAlert(Alert.AlertType.INFORMATION, "Purchase logged successfully.");
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Amount must be a valid number.");
        } catch (IllegalArgumentException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }

    /**
     * Updates the balances label to show the current balance of each member.
     *
     * @post the balances label shows one line per member with that member's balance
     */
    private void refreshBalances() {
        StringBuilder sb = new StringBuilder();
        for (Member m : members) {
            sb.append(m.getName()).append(": ").append(m.getBalance()).append('\n');
        }
        balancesLabel.setText(sb.toString());
    }

    /**
     * Shows a modal alert dialog with the given type and message.
     *
     * @param type    the type of alert to show
     * @param message the message to display
     * @pre type != null && message != null
     * @post an alert of the given type displaying the message has been shown
     */
    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).showAndWait();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args the command-line arguments (ignored)
     * @post the JavaFX application has been started
     */
    public static void main(String[] args) {
        launch(args);
    }
}