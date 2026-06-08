package nl.tue.mealmate.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.tue.mealmate.domain.Household;
import nl.tue.mealmate.domain.Member;

public class LoginScreen {

    private final MealMateApp app;
    private final Household household;

    public LoginScreen(MealMateApp app, Household household) {
        this.app = app;
        this.household = household;
    }

    public Scene createScene() {
        Label title = new Label("MealMate");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label("Household: " + household.getName());
        subtitle.setFont(Font.font("System", 14));

        Label emailLabel = new Label("Email address");
        TextField emailField = new TextField();
        emailField.setPromptText("e.g. doruk@tue.nl");
        emailField.setMaxWidth(280);

        Button loginButton = new Button("Log In");
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(280);
        loginButton.setOnAction(e -> handleLogin(emailField.getText().trim()));

        VBox root = new VBox(12, title, subtitle, emailLabel, emailField, loginButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        return new Scene(root, 380, 280);
    }

    private void handleLogin(String email) {
        if (email.isBlank()) {
            showError("Please enter your email address.");
            return;
        }

        Member member = household.getMembers().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (member == null) {
            showError("No member found with that email in this household.");
            return;
        }

        app.showDashboard(member);
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}