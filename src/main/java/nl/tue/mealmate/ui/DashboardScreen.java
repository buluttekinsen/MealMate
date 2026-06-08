package nl.tue.mealmate.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import nl.tue.mealmate.domain.Household;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.repository.PurchaseRepository;
import nl.tue.mealmate.service.PurchaseService;

public class DashboardScreen {

    private final MealMateApp app;
    private final Member loggedInMember;
    private final Household household;
    private final PurchaseService purchaseService;
    private final PurchaseRepository purchaseRepository;

    private final List<CheckBox> participantBoxes = new ArrayList<>();
    private Label balancesLabel;
    private VBox expenseList;

    public DashboardScreen(MealMateApp app, Member loggedInMember,
            Household household, PurchaseService purchaseService,
            PurchaseRepository purchaseRepository) {
        this.app = app;
        this.loggedInMember = loggedInMember;
        this.household = household;
        this.purchaseService = purchaseService;
        this.purchaseRepository = purchaseRepository;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setTop(buildHeader());
        root.setLeft(buildLogPurchasePanel());
        root.setRight(buildSummaryPanel());
        root.setPadding(new Insets(16));
        return new Scene(root, 780, 540);
    }

    private HBox buildHeader() {
        Label title = new Label("MealMate — " + household.getName());
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        Label userLabel = new Label("Logged in: " + loggedInMember.getName()
                + " (" + household.getRole(loggedInMember) + ")");

        Button logoutButton = new Button("Log Out");
        logoutButton.setOnAction(e -> app.showLoginScreen());

        HBox header = new HBox(16, title, userLabel, logoutButton);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 12, 0));
        return header;
    }

    private VBox buildLogPurchasePanel() {
        Label heading = new Label("Log a Purchase");
        heading.setFont(Font.font("System", FontWeight.BOLD, 14));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);

        TextField amountField = new TextField();
        amountField.setPromptText("e.g. 30.00");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("e.g. Groceries");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        CheckBox sharedBox = new CheckBox("Shared purchase");
        sharedBox.setSelected(true);

        List<Member> members = new ArrayList<>(household.getMembers());

        ComboBox<Member> payerBox = new ComboBox<>();
        payerBox.getItems().addAll(members);
        payerBox.setValue(loggedInMember);
        payerBox.setConverter(new StringConverter<Member>() {
            @Override public String toString(Member m) { return m == null ? "" : m.getName(); }
            @Override public Member fromString(String s) { return null; }
        });

        VBox participantsBox = new VBox(4, new Label("Split with:"));
        for (Member m : members) {
            CheckBox cb = new CheckBox(m.getName());
            cb.setUserData(m);
            cb.setSelected(true);
            participantBoxes.add(cb);
            participantsBox.getChildren().add(cb);
        }

        form.addRow(0, new Label("Amount (€):"), amountField);
        form.addRow(1, new Label("Description:"), descriptionField);
        form.addRow(2, new Label("Date:"), datePicker);
        form.addRow(3, new Label("Paid by:"), payerBox);
        form.add(sharedBox, 1, 4);
        form.add(participantsBox, 1, 5);

        Button logButton = new Button("Log Purchase");
        logButton.setDefaultButton(true);
        logButton.setOnAction(e -> {
            handleLog(amountField, descriptionField, datePicker, sharedBox, payerBox);
        });

        VBox panel = new VBox(10, heading, new Separator(), form, logButton);
        panel.setPadding(new Insets(0, 24, 0, 0));
        panel.setPrefWidth(360);
        return panel;
    }

    private VBox buildSummaryPanel() {
        Label balancesHeading = new Label("Balances");
        balancesHeading.setFont(Font.font("System", FontWeight.BOLD, 14));

        balancesLabel = new Label();
        refreshBalances();

        Label historyHeading = new Label("Expense History");
        historyHeading.setFont(Font.font("System", FontWeight.BOLD, 14));

        expenseList = new VBox(4);
        refreshExpenseList();

        ScrollPane scroll = new ScrollPane(expenseList);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(280);

        VBox panel = new VBox(10,
                balancesHeading, new Separator(), balancesLabel,
                historyHeading, new Separator(), scroll);
        panel.setPrefWidth(340);
        return panel;
    }

    private void handleLog(TextField amountField, TextField descriptionField,
            DatePicker datePicker, CheckBox sharedBox, ComboBox<Member> payerBox) {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            List<Member> participants = new ArrayList<>();
            for (CheckBox cb : participantBoxes) {
                if (cb.isSelected()) participants.add((Member) cb.getUserData());
            }
            purchaseService.logPurchase(amount, descriptionField.getText().trim(),
                    datePicker.getValue(), sharedBox.isSelected(),
                    payerBox.getValue(), participants);

            amountField.clear();
            descriptionField.clear();
            refreshBalances();
            refreshExpenseList();
            new Alert(Alert.AlertType.INFORMATION, "Purchase logged.").showAndWait();
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Amount must be a valid number.").showAndWait();
        } catch (IllegalArgumentException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
        }
    }

    private void refreshBalances() {
        StringBuilder sb = new StringBuilder();
        for (Member m : household.getMembers()) {
            String sign = m.getBalance() >= 0 ? "is owed" : "owes";
            sb.append(String.format("%s: %s €%.2f%n",
                    m.getName(), sign, Math.abs(m.getBalance())));
        }
        balancesLabel.setText(sb.toString());
    }

    private void refreshExpenseList() {
        expenseList.getChildren().clear();
        if (purchaseRepository.findAll().isEmpty()) {
            expenseList.getChildren().add(new Label("No purchases yet."));
            return;
        }
        purchaseRepository.findAll().forEach(p -> {
            String text = String.format("%s  %s  €%.2f  paid by %s%s",
                    p.getDate(), p.getDescription(), p.getAmount(),
                    p.getPayer().getName(),
                    p.isShared() ? " (shared)" : " (personal)");
            expenseList.getChildren().add(new Label(text));
        });
    }
}