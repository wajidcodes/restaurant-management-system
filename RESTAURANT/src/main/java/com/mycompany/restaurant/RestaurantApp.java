package com.mycompany.restaurant;

import com.mycompany.restaurant.utils.SceneManager;
import com.mycompany.restaurant.utils.AnimationUtils;
import com.mycompany.restaurant.views.*;
import com.mycompany.restaurant.views.components.Sidebar;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Restaurant Management System - Professional POS Application
 * Main entry point with dashboard-based architecture
 */
public class RestaurantApp extends Application {

    // Password for owner
    public static String ownerPassword = "Apple";

    // Primary stage reference
    private Stage primaryStage;
    private BorderPane mainLayout;
    private Sidebar sidebar;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        SceneManager.initialize(stage);

        // Initialize Database
        com.mycompany.restaurant.utils.DatabaseHelper.initializeDatabase();

        // Show login first
        Scene loginScene = createLoginScene();
        SceneManager.applyStylesheet(loginScene);

        stage.setScene(loginScene);
        stage.setTitle("Restaurant Management System");
        stage.setMaximized(true);
        stage.setWidth(1600);
        stage.setHeight(900);
        stage.show();
    }

    /**
     * Create beautiful professional login scene with two-column layout
     */
    /**
     * Create beautiful professional login scene with two-column layout
     */
    private Scene createLoginScene() {
        // Main container with gradient background
        StackPane root = new StackPane();
        root.getStyleClass().add("login-background");
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2C3E50, #4CA1AF);");

        // Center card with shadow effect
        HBox loginCard = new HBox();
        loginCard.setMaxSize(1100, 650);
        loginCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 20px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 30, 0, 0, 10);");

        // LEFT SIDE - Branding and Image
        VBox leftPanel = new VBox(25);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setPrefWidth(500);
        try {
            leftPanel.setStyle(
                    "-fx-background-image: url('"
                            + getClass().getResource("/images/login_background.png").toExternalForm() + "'); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-position: center; " +
                            "-fx-background-radius: 20px 0 0 20px;");
        } catch (Exception e) {
            leftPanel.setStyle(
                    "-fx-background-color: linear-gradient(135deg, #2C3E50 0%, #000000 100%);" +
                            "-fx-background-radius: 20px 0 0 20px;");
        }
        leftPanel.setPadding(new Insets(40));

        Label logoIcon = new Label("🍽️");
        logoIcon.setStyle(
                "-fx-font-size: 80px; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");

        Label appName = new Label("GOURMET\nPOS SYSTEM");
        appName.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        appName.setWrapText(true);
        appName.setStyle(
                "-fx-font-family: 'Segoe UI', sans-serif;" +
                        "-fx-font-size: 36px;" +
                        "-fx-font-weight: 800;" +
                        "-fx-text-fill: white;" +
                        "-fx-alignment: center;" +
                        "-fx-effect: dropshadow(gaussian, black, 10, 0.5, 0, 0);");

        Label tagline = new Label("Excellence in every interaction");
        tagline.setWrapText(true);
        tagline.setStyle(
                "-fx-font-size: 16px; -fx-text-fill: rgba(255,255,255,0.9); -fx-font-style: italic; -fx-effect: dropshadow(gaussian, black, 5, 0.5, 0, 0);");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        Label versionText = new Label("v2.5.0 Enterprise Edition");
        versionText.setStyle(
                "-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.8); -fx-effect: dropshadow(gaussian, black, 2, 0.5, 0, 0);");

        leftPanel.getChildren().addAll(spacer, logoIcon, appName, tagline, spacer2, versionText);

        // RIGHT SIDE - Login Actions
        VBox rightPanel = new VBox(35);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(50, 60, 50, 60));
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        Label welcomeText = new Label("Welcome Back");
        welcomeText.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        Label subText = new Label("Please select your access level");
        subText.setStyle("-fx-font-size: 16px; -fx-text-fill: #95A5A6;");

        VBox headerBox = new VBox(10, welcomeText, subText);
        headerBox.setAlignment(Pos.CENTER);

        // Role Buttons (styled as modern actionable cards)
        VBox rolesBox = new VBox(20);
        rolesBox.setAlignment(Pos.CENTER);

        rolesBox.getChildren().addAll(
                createModernRoleButton("👑", "Restaurant Owner", "Admin Access & Reports", () -> handleOwnerLogin()),
                createModernRoleButton("📋", "Front Desk", "Reservations & Reception", () -> showCustomerEntryView()),
                createModernRoleButton("🍽️", "Service Staff", "Orders & Tables", () -> showWaiterOrderView()));

        rightPanel.getChildren().addAll(headerBox, rolesBox);

        // Add panels to card
        loginCard.getChildren().addAll(leftPanel, rightPanel);

        // Add card to root
        root.getChildren().add(loginCard);

        // ScrollPane wrapper for the entire login screen
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.getStyleClass().add("viewport-scroll"); // Optional for CSS targeting
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        // Entrance animation
        loginCard.setOpacity(0);
        loginCard.setCache(false);

        // Manual fade transition to ensure control
        FadeTransition ft = new FadeTransition(Duration.millis(800), loginCard);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setDelay(Duration.millis(100));
        ft.setOnFinished(e -> {
            loginCard.setOpacity(1.0);
            loginCard.setVisible(true); // Force visibility
        });
        ft.play();

        return new Scene(scrollPane, 1600, 900);
    }

    private Button createModernRoleButton(String icon, String title, String subtitle, Runnable action) {
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(80);
        btn.getStyleClass().add("role-button-modern"); // We will need to ensure this class exists or use inline styles

        // Custom graphic for button
        HBox graphic = new HBox(20);
        graphic.setAlignment(Pos.CENTER_LEFT);
        graphic.setPadding(new Insets(0, 10, 0, 10));

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 30px;");

        VBox textBox = new VBox(3);
        textBox.setAlignment(Pos.CENTER_LEFT);

        Text titleText = new Text(title);
        titleText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #34495E;");

        Text subText = new Text(subtitle);
        subText.setStyle("-fx-font-size: 13px; -fx-fill: #7F8C8D;");

        textBox.getChildren().addAll(titleText, subText);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Text arrow = new Text("❯");
        arrow.setStyle("-fx-font-size: 18px; -fx-fill: #BDC3C7;");

        graphic.getChildren().addAll(iconText, textBox, spacer, arrow);
        btn.setGraphic(graphic);

        // Inline styles for immediate effect
        btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ECF0F1;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #F8F9F9;" +
                        "-fx-border-color: #3498DB;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(52, 152, 219, 0.2), 10, 0, 0, 2);"));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #ECF0F1;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-cursor: hand;"));

        btn.setOnAction(e -> action.run());

        return btn;
    }

    /**
     * Create compact horizontal role card
     */
    private VBox createCompactRoleCard(String icon, String roleName, String description, Runnable onSelect) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #E0E0E0;" +
                        "-fx-border-width: 1.5px;" +
                        "-fx-border-radius: 12px;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-cursor: hand;");

        // Icon
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 40px;");

        // Text content
        VBox textContent = new VBox(5);
        textContent.setAlignment(Pos.CENTER_LEFT);

        Text roleText = new Text(roleName);
        roleText.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-fill: #333;");

        Text descText = new Text(description);
        descText.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-fill: #666;");

        textContent.getChildren().addAll(roleText, descText);

        // Arrow icon
        Text arrow = new Text("→");
        arrow.setStyle("-fx-font-size: 24px; -fx-fill: #008B8B;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(iconText, textContent, spacer, arrow);

        // Click handler
        card.setOnMouseClicked(e -> {
            AnimationUtils.pulse(card);
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(150);
                    onSelect.run();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        });

        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle(
                    "-fx-background-color: linear-gradient(to right, #F0F8FF, white);" +
                            "-fx-border-color: #008B8B;" +
                            "-fx-border-width: 2px;" +
                            "-fx-border-radius: 12px;" +
                            "-fx-background-radius: 12px;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,139,139,0.3), 15, 0, 0, 5);");
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #E0E0E0;" +
                            "-fx-border-width: 1.5px;" +
                            "-fx-border-radius: 12px;" +
                            "-fx-background-radius: 12px;" +
                            "-fx-cursor: hand;");
        });

        VBox wrapper = new VBox(card);
        return wrapper;
    }

    /**
     * Create a beautiful role selection card (DEPRECATED - kept for compatibility)
     */
    private VBox createRoleCard(String icon, String roleName, String description, Runnable onSelect) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("navigation-card");
        card.setPrefSize(280, 320);
        card.setMaxSize(280, 320);

        // Icon
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 80px;");

        // Role name
        Text roleText = new Text(roleName);
        roleText.getStyleClass().add("card-title");
        roleText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Description
        Text descText = new Text(description);
        descText.getStyleClass().add("label-modern");
        descText.setStyle("-fx-text-alignment: center; -fx-wrap-text: true;");
        descText.setWrappingWidth(240);

        card.getChildren().addAll(iconText, roleText, descText);

        // Click handler with animation
        card.setOnMouseClicked(e -> {
            AnimationUtils.pulse(card);
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(150);
                    onSelect.run();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        });

        AnimationUtils.addHoverEffect(card);

        return card;
    }

    /**
     * Handle owner login with password
     */
    private void handleOwnerLogin() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Owner Authentication");
        dialog.setHeaderText("Enter Owner Password");
        dialog.setContentText("Password:");

        // Style the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/styles/application.css").toExternalForm());
        dialogPane.getStyleClass().add("glass-card");

        dialog.showAndWait().ifPresent(password -> {
            if (password.equals(ownerPassword)) {
                showMainDashboard();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Authentication Failed");
                alert.setHeaderText("Incorrect Password");
                alert.setContentText("The password you entered is incorrect.");

                DialogPane alertPane = alert.getDialogPane();
                alertPane.getStylesheets().add(
                        getClass().getResource("/styles/application.css").toExternalForm());

                alert.showAndWait();
            }
        });
    }

    /**
     * Show Owner Dashboard
     */
    private void showOwnerDashboard() {
        OwnerDashboard dashboard = new OwnerDashboard(this);
        Scene scene = new Scene(dashboard.getView(), 1600, 900);
        SceneManager.applyStylesheet(scene);
        SceneManager.switchScene(scene);
    }

    /**
     * Show Customer Entry View
     */
    private void showCustomerEntryView() {
        CustomerEntryView view = new CustomerEntryView(this);
        Scene scene = new Scene(view.getView(), 1600, 900);
        SceneManager.applyStylesheet(scene);
        SceneManager.switchScene(scene);
    }

    /**
     * Show Waiter Order View
     */
    private void showWaiterOrderView() {
        WaiterOrderView view = new WaiterOrderView(this);
        Scene scene = new Scene(view.getView(), 1600, 900);
        SceneManager.applyStylesheet(scene);
        SceneManager.switchScene(scene);
    }

    /**
     * Show main dashboard with sidebar navigation
     */
    private void showMainDashboard() {
        mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("main-layout");

        // Create sidebar with navigation handler AND logout handler
        sidebar = new Sidebar(
                () -> navigateToView(sidebar.getActiveView()),
                () -> showLoginScreen());
        mainLayout.setLeft(sidebar.getView());

        // Show initial dashboard view using the helper to wrap it
        setCenterView(new DashboardView(this).getView());

        Scene scene = new Scene(mainLayout, 1600, 900);
        SceneManager.applyStylesheet(scene);
        SceneManager.switchScene(scene);
    }

    /**
     * Navigate to different views based on sidebar selection
     */
    /**
     * Navigate to different views based on sidebar selection
     */
    public void navigateToView(com.mycompany.restaurant.utils.ViewType viewType) {
        switch (viewType) {
            case DASHBOARD:
                setCenterView(new DashboardView(this).getView());
                break;
            case TABLES:
                setCenterView(new TableManagementView().getView());
                break;
            case POS:
                // Use existing POS view embedded in dashboard
                setCenterView(new WaiterOrderView(this).getView());
                break;
            case KITCHEN:
                // Kitchen display
                setCenterView(new KitchenView(this).getView());
                break;
            case MENU:
                // Menu management
                setCenterView(new MenuManagementView(this).getView());
                break;
            case RESERVATIONS:
                // Use existing customer entry view
                setCenterView(new CustomerEntryView(this).getView());
                break;
            case INVENTORY:
                // Inventory
                setCenterView(new InventoryManagementView(this).getView());
                break;
            case STAFF:
                // Use existing employee view
                setCenterView(new EmployeeManagementView(this).getView());
                break;
            case ANALYTICS:
                // Financial Analytics
                setCenterView(new FinancialView(this).getView());
                break;
        }
    }

    /**
     * Helper method to set the center content with a ScrollPane
     */
    private void setCenterView(javafx.scene.Node view) {
        ScrollPane scroll = new ScrollPane(view);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        mainLayout.setCenter(scroll);
    }

    /**
     * Show placeholder view for modules in development
     */

    /**
     * Return to login screen
     */
    public void showLoginScreen() {
        Scene loginScene = createLoginScene();
        SceneManager.applyStylesheet(loginScene);
        SceneManager.switchScene(loginScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
