package com.mycompany.restaurant.views.components;

import com.mycompany.restaurant.utils.ViewType;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Sidebar navigation component for the restaurant management system
 */
public class Sidebar {
    private VBox root;
    private ViewType activeView = ViewType.DASHBOARD;
    private Runnable onNavigate;
    private Runnable onLogout;

    public Sidebar(Runnable onNavigate, Runnable onLogout) {
        this.onNavigate = onNavigate;
        this.onLogout = onLogout;
        this.root = new VBox();
        createSidebar();
    }

    private void createSidebar() {
        root.getChildren().clear();
        root.getStyleClass().setAll("sidebar");
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2C3E50 0%, #1a1a2e 100%);");
        root.setPrefWidth(240); // Slightly narrower
        root.setMinWidth(240);
        root.setMaxWidth(240);

        // Header with logo
        VBox header = new VBox(10);
        header.getStyleClass().add("sidebar-header");
        header.setAlignment(Pos.CENTER);

        Text logo = new Text("🍽️");
        logo.setStyle("-fx-font-size: 48px;");

        Text appName = new Text("RestaurantPOS");
        appName.getStyleClass().add("sidebar-logo");

        header.getChildren().addAll(logo, appName);

        // Navigation section
        VBox nav = new VBox(5);
        nav.setPadding(new Insets(30, 10, 20, 10));

        nav.getChildren().addAll(
                createNavItem("🏠", "Dashboard", ViewType.DASHBOARD),
                createNavItem("🍽️", "Tables", ViewType.TABLES),
                createNavItem("📋", "Orders (POS)", ViewType.POS),
                createNavItem("👨‍🍳", "Kitchen Display", ViewType.KITCHEN),
                createNavItem("📖", "Menu", ViewType.MENU),
                createNavItem("📅", "Reservations", ViewType.RESERVATIONS),
                createNavItem("📦", "Inventory", ViewType.INVENTORY),
                createNavItem("👥", "Staff", ViewType.STAFF),
                createNavItem("📊", "Analytics", ViewType.ANALYTICS));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Footer
        VBox footer = new VBox(10);
        footer.getStyleClass().add("sidebar-footer");
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15));

        Text userInfo = new Text("👤 Admin");
        userInfo.setStyle("-fx-fill: #BDC3C7; -fx-font-size: 14px;");

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("btn-secondary");
        logoutBtn.setPrefWidth(200);
        logoutBtn.setOnAction(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });

        footer.getChildren().addAll(userInfo, logoutBtn);

        root.getChildren().addAll(header, nav, spacer, footer);
    }

    private Button createNavItem(String icon, String label, ViewType viewType) {
        Button btn = new Button(icon + "  " + label);
        btn.getStyleClass().add("sidebar-nav-item");

        // Explicit styles for guaranteed visibility - REDUCED TO 35px
        btn.setPrefHeight(35);
        btn.setMinHeight(35);
        btn.setPadding(new Insets(5, 10, 5, 20));

        if (viewType == activeView) {
            btn.getStyleClass().add("active");
            btn.setStyle(
                    "-fx-background-color: rgba(72, 209, 204, 0.15); -fx-text-fill: #48D1CC; -fx-border-color: transparent transparent transparent #48D1CC; -fx-border-width: 0 0 0 4; -fx-font-size: 13px;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #BDC3C7; -fx-font-size: 13px;");
        }

        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        btn.setOnAction(e -> {
            setActive(viewType);
            if (onNavigate != null) {
                onNavigate.run();
            }
        });

        return btn;
    }

    public void setActive(ViewType viewType) {
        this.activeView = viewType;
        // Refresh navigation to update active state
        createSidebar();
    }

    public VBox getView() {
        return root;
    }

    public ViewType getActiveView() {
        return activeView;
    }
}
