package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * OwnerDashboard - Navigation hub for owner operations
 */
public class OwnerDashboard {

    private BorderPane root;
    private RestaurantApp app;

    public OwnerDashboard(RestaurantApp app) {
        this.app = app;
        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("scene-root");
        root.setPadding(new Insets(30));

        // Top bar
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Center content
        VBox centerContent = createCenterContent();
        root.setCenter(centerContent);

        // Bottom navigation
        HBox bottomNav = createBottomNav();
        root.setBottom(bottomNav);
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 30, 0));

        Text title = new Text("Owner Dashboard");
        title.getStyleClass().add("page-title");
        AnimationUtils.fadeIn(title, 600);

        topBar.getChildren().add(title);
        return topBar;
    }

    private VBox createCenterContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        // Navigation cards grid
        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setAlignment(Pos.CENTER);

        // Employee Management Card
        VBox employeeCard = createDashboardCard(
                "👥",
                "Employee Management",
                "Add, edit, and manage employee records",
                () -> showEmployeeManagement());
        AnimationUtils.slideUpWithDelay(employeeCard, 500, 200);

        // Financial Statement Card
        VBox financeCard = createDashboardCard(
                "💰",
                "Financial Statement",
                "View monthly sales and revenue reports",
                () -> showFinancialView());
        AnimationUtils.slideUpWithDelay(financeCard, 500, 350);

        // Password Reset Card
        VBox passwordCard = createDashboardCard(
                "🔐",
                "Password Reset",
                "Change your owner password",
                () -> handlePasswordReset());
        AnimationUtils.slideUpWithDelay(passwordCard, 500, 500);

        grid.add(employeeCard, 0, 0);
        grid.add(financeCard, 1, 0);
        grid.add(passwordCard, 2, 0);

        content.getChildren().add(grid);
        return content;
    }

    private VBox createDashboardCard(String icon, String title, String description, Runnable onClick) {
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("navigation-card");
        card.setPrefSize(300, 280);
        card.setMaxSize(300, 280);

        // Icon
        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 70px;");

        // Title
        Text titleText = new Text(title);
        titleText.getStyleClass().add("card-title");
        titleText.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Description
        Text descText = new Text(description);
        descText.getStyleClass().add("label-modern");
        descText.setStyle("-fx-text-alignment: center;");
        descText.setWrappingWidth(260);

        card.getChildren().addAll(iconText, titleText, descText);

        card.setOnMouseClicked(e -> {
            AnimationUtils.pulse(card);
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(150);
                    onClick.run();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        });

        AnimationUtils.addHoverEffect(card);

        return card;
    }

    private HBox createBottomNav() {
        HBox bottomNav = new HBox(15);
        bottomNav.setAlignment(Pos.CENTER);
        bottomNav.setPadding(new Insets(30, 0, 0, 0));

        Button backBtn = new Button("← Back to Login");
        backBtn.getStyleClass().add("btn-back");
        backBtn.setOnAction(e -> app.showLoginScreen());
        AnimationUtils.fadeInWithDelay(backBtn, 400, 600);

        bottomNav.getChildren().add(backBtn);
        return bottomNav;
    }

    private void showEmployeeManagement() {
        EmployeeManagementView view = new EmployeeManagementView(app);
        javafx.scene.Scene scene = new javafx.scene.Scene(view.getView(), 1200, 700);
        com.mycompany.restaurant.utils.SceneManager.applyStylesheet(scene);
        com.mycompany.restaurant.utils.SceneManager.switchScene(scene);
    }

    private void showFinancialView() {
        FinancialView view = new FinancialView(app);
        javafx.scene.Scene scene = new javafx.scene.Scene(view.getView(), 1200, 700);
        com.mycompany.restaurant.utils.SceneManager.applyStylesheet(scene);
        com.mycompany.restaurant.utils.SceneManager.switchScene(scene);
    }

    private void handlePasswordReset() {
        // Current password dialog
        TextInputDialog currentDialog = new TextInputDialog();
        currentDialog.setTitle("Password Reset");
        currentDialog.setHeaderText("Enter Current Password");
        currentDialog.setContentText("Current Password:");

        DialogPane dialogPane = currentDialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/styles/application.css").toExternalForm());
        dialogPane.getStyleClass().add("glass-card");

        currentDialog.showAndWait().ifPresent(currentPass -> {
            if (RestaurantApp.ownerPassword.equals(currentPass)) {
                // New password dialog
                TextInputDialog newDialog = new TextInputDialog();
                newDialog.setTitle("Password Reset");
                newDialog.setHeaderText("Enter New Password");
                newDialog.setContentText("New Password:");

                DialogPane newDialogPane = newDialog.getDialogPane();
                newDialogPane.getStylesheets().add(
                        getClass().getResource("/styles/application.css").toExternalForm());
                newDialogPane.getStyleClass().add("glass-card");

                newDialog.showAndWait().ifPresent(newPass -> {
                    RestaurantApp.ownerPassword = newPass;

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Success");
                    success.setHeaderText("Password Changed!");
                    success.setContentText("Your password has been updated successfully.");

                    DialogPane successPane = success.getDialogPane();
                    successPane.getStylesheets().add(
                            getClass().getResource("/styles/application.css").toExternalForm());

                    success.showAndWait();
                });
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("Incorrect Password");
                error.setContentText("The current password you entered is incorrect.");

                DialogPane errorPane = error.getDialogPane();
                errorPane.getStylesheets().add(
                        getClass().getResource("/styles/application.css").toExternalForm());

                error.showAndWait();
            }
        });
    }

    public BorderPane getView() {
        return root;
    }
}
