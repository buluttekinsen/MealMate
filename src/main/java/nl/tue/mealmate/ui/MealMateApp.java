package nl.tue.mealmate.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.tue.mealmate.domain.Household;
import nl.tue.mealmate.domain.Member;
import nl.tue.mealmate.domain.Role;
import nl.tue.mealmate.repository.HouseholdRepository;
import nl.tue.mealmate.repository.InMemoryHouseholdRepository;
import nl.tue.mealmate.repository.InMemoryPurchaseRepository;
import nl.tue.mealmate.repository.PurchaseRepository;
import nl.tue.mealmate.service.ConsoleInviter;
import nl.tue.mealmate.service.ConsoleNotifier;
import nl.tue.mealmate.service.HouseholdService;
import nl.tue.mealmate.service.InvitationService;
import nl.tue.mealmate.service.NotificationService;
import nl.tue.mealmate.service.PurchaseService;

public class MealMateApp extends Application {

    private Stage primaryStage;
    private Household household;
    private PurchaseService purchaseService;
    private HouseholdService householdService;
    private PurchaseRepository purchaseRepository;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Composition root
        purchaseRepository = new InMemoryPurchaseRepository();
        NotificationService notifier = new ConsoleNotifier();
        purchaseService = new PurchaseService(purchaseRepository, notifier);

        HouseholdRepository householdRepository = new InMemoryHouseholdRepository();
        InvitationService inviter = new ConsoleInviter();
        householdService = new HouseholdService(householdRepository, inviter);

        // Seed a demo household
        Member bulut  = new Member("Bulut",  "bulut@tue.nl");
        Member doruk  = new Member("Doruk",  "doruk@tue.nl");
        Member serhat = new Member("Serhat", "serhat@tue.nl");
        household = householdService.createHousehold("TUe House", bulut);
        householdService.inviteMember(household, doruk,  Role.MEMBER);
        householdService.inviteMember(household, serhat, Role.MEMBER);

        primaryStage.setTitle("MealMate");
        primaryStage.setResizable(false);
        showLoginScreen();
        primaryStage.show();
    }

    public void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(this, household);
        primaryStage.setScene(loginScreen.createScene());
    }

    public void showDashboard(Member loggedInMember) {
        DashboardScreen dashboard = new DashboardScreen(
                this, loggedInMember, household, purchaseService, purchaseRepository);
        primaryStage.setScene(dashboard.createScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}