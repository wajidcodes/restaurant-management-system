package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Main dashboard view with metrics and quick actions
 */
public class DashboardView {
    private BorderPane root;

    private RestaurantApp app;

    public DashboardView(RestaurantApp app) {
        this.app = app;
        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #ECF0F1;");
        root.setPadding(new Insets(30));

        // Top section
        VBox topSection = new VBox(20);

        // Page title
        Text title = new Text("Dashboard");
        title.getStyleClass().add("page-title");

        Text subtitle = new Text("Welcome back! Here's what's happening today.");
        subtitle.getStyleClass().add("text-secondary");
        subtitle.setStyle("-fx-font-size: 16px;");

        topSection.getChildren().addAll(title, subtitle);

        // Metrics cards
        HBox metricsRow = new HBox(20);
        metricsRow.setPadding(new Insets(20, 0, 0, 0));

        VBox revenueCard = createMetricCard("💰", "Today's Revenue", "Rs. 15,750", "+12%", true);
        VBox ordersCard = createMetricCard("📋", "Total Orders", "48", "+8%", true);
        VBox tablesCard = createMetricCard("🍽️", "Active Tables", "12/20", "60%", false);
        VBox staffCard = createMetricCard("👥", "Staff On Duty", "15", "All present", false);

        metricsRow.getChildren().addAll(revenueCard, ordersCard, tablesCard, staffCard);
        HBox.setHgrow(revenueCard, Priority.ALWAYS);
        HBox.setHgrow(ordersCard, Priority.ALWAYS);
        HBox.setHgrow(tablesCard, Priority.ALWAYS);
        HBox.setHgrow(staffCard, Priority.ALWAYS);

        // Quick actions
        VBox actionsSection = new VBox(15);
        actionsSection.setPadding(new Insets(30, 0, 20, 0));

        Text actionsTitle = new Text("Quick Actions");
        actionsTitle.getStyleClass().add("section-title");

        HBox actionButtons = new HBox(15);

        Button newOrderBtn = new Button("📝 New Order");
        newOrderBtn.getStyleClass().add("btn-primary");
        newOrderBtn.setPrefWidth(180);
        newOrderBtn.setPrefHeight(50);
        newOrderBtn.setOnAction(e -> app.navigateToView(com.mycompany.restaurant.utils.ViewType.POS));

        Button kitchenBtn = new Button("🍳 Kitchen");
        kitchenBtn.getStyleClass().add("btn-success");
        kitchenBtn.setPrefWidth(180);
        kitchenBtn.setPrefHeight(50);
        kitchenBtn.setOnAction(e -> app.navigateToView(com.mycompany.restaurant.utils.ViewType.KITCHEN));

        Button viewMenuBtn = new Button("📖 View Menu");
        viewMenuBtn.getStyleClass().add("btn-secondary");
        viewMenuBtn.setPrefWidth(180);
        viewMenuBtn.setPrefHeight(50);
        viewMenuBtn.setOnAction(e -> app.navigateToView(com.mycompany.restaurant.utils.ViewType.MENU));

        Button inventoryBtn = new Button("📦 Inventory");
        inventoryBtn.getStyleClass().add("btn-info");
        // Force high-contrast blue background with white text
        inventoryBtn.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold;");
        inventoryBtn.setPrefWidth(180);
        inventoryBtn.setPrefHeight(50);
        inventoryBtn.setOnAction(e -> app.navigateToView(com.mycompany.restaurant.utils.ViewType.INVENTORY));

        Button reportsBtn = new Button("📊 Reports");
        reportsBtn.getStyleClass().add("btn-warning");
        // Force high-contrast orange background with white text
        reportsBtn.setStyle("-fx-background-color: #F39C12; -fx-text-fill: white; -fx-font-weight: bold;");
        reportsBtn.setPrefWidth(180);
        reportsBtn.setPrefHeight(50);
        reportsBtn.setOnAction(e -> app.navigateToView(com.mycompany.restaurant.utils.ViewType.ANALYTICS));

        actionButtons.getChildren().addAll(newOrderBtn, kitchenBtn, viewMenuBtn, inventoryBtn, reportsBtn);
        actionsSection.getChildren().addAll(actionsTitle, actionButtons);

        // Recent activity
        VBox activitySection = new VBox(15);
        activitySection.setPadding(new Insets(20, 0, 0, 0));

        Text activityTitle = new Text("Recent Activity");
        activityTitle.getStyleClass().add("section-title");

        VBox activityList = new VBox(10);
        activityList.getStyleClass().add("glass-card");
        activityList.setPadding(new Insets(20));
        activityList.setMaxWidth(800);

        activityList.getChildren().addAll(
                createActivityItem("🍽️", "Table 5 seated - Party of 4", "2 minutes ago"),
                createActivityItem("✅", "Order #127 completed - Rs. 1,250", "5 minutes ago"),
                createActivityItem("🧹", "Table 12 cleared and cleaned", "8 minutes ago"),
                createActivityItem("📋", "New order #128 - Table 3", "10 minutes ago"),
                createActivityItem("👥", "New employee registered: John Doe", "15 minutes ago"));

        activitySection.getChildren().addAll(activityTitle, activityList);

        // Main content
        VBox content = new VBox(20);
        content.getChildren().addAll(topSection, metricsRow, actionsSection, activitySection);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");

        root.setCenter(scroll);

        // Animations
        AnimationUtils.fadeIn(topSection, 500);
        AnimationUtils.slideUpWithDelay(metricsRow, 400, 100);
        AnimationUtils.slideUpWithDelay(actionsSection, 400, 200);
        AnimationUtils.slideUpWithDelay(activitySection, 400, 300);
    }

    private VBox createMetricCard(String icon, String label, String value, String change, boolean isPositive) {
        VBox card = new VBox(10);
        card.getStyleClass().add("metric-card");
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.CENTER_LEFT);

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 32px;");

        Text labelText = new Text(label);
        labelText.getStyleClass().add("metric-label");

        Text valueText = new Text(value);
        valueText.getStyleClass().add("metric-value");

        Text changeText = new Text(change);
        changeText.getStyleClass().add(isPositive ? "metric-change-positive" : "text-secondary");
        changeText.setStyle("-fx-font-size: 13px;");

        card.getChildren().addAll(iconText, labelText, valueText, changeText);

        return card;
    }

    private HBox createActivityItem(String icon, String text, String time) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 20px;");

        VBox textContent = new VBox(3);

        Text mainText = new Text(text);
        mainText.setStyle("-fx-font-size: 14px; -fx-fill: #2C3E50;");

        Text timeText = new Text(time);
        timeText.setStyle("-fx-font-size: 12px; -fx-fill: #7F8C8D;");

        textContent.getChildren().addAll(mainText, timeText);

        item.getChildren().addAll(iconText, textContent);

        return item;
    }

    public BorderPane getView() {
        return root;
    }
}
