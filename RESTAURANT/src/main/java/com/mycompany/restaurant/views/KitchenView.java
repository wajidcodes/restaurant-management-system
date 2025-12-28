package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.services.OrderService;
import com.mycompany.restaurant.views.WaiterOrderView.Order;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.collections.ListChangeListener;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * KitchenView - Kitchen Display System (KDS)
 * Shows active orders in real-time
 */
public class KitchenView {

    private BorderPane root;
    private RestaurantApp app;
    private TilePane ordersContainer;
    private OrderService orderService;

    public KitchenView(RestaurantApp app) {
        this.app = app;
        this.orderService = OrderService.getInstance();
        createView();

        // Listen for new orders
        orderService.getActiveOrders().addListener((ListChangeListener<Order>) c -> refreshOrders());
    }

    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("scene-root");
        root.setPadding(new Insets(30));

        // Top bar
        VBox topBar = new VBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 30, 0));

        Text title = new Text("👨‍🍳 Kitchen Display System");
        title.getStyleClass().add("page-title");

        Text subtitle = new Text("Real-time order monitoring");
        subtitle.getStyleClass().addAll("label-modern", "text-center");
        subtitle.setStyle("-fx-font-size: 16px;");

        topBar.getChildren().addAll(title, subtitle);
        root.setTop(topBar);

        // Orders Area
        ordersContainer = new TilePane();
        ordersContainer.setPrefColumns(3); // Enforce 3 columns
        ordersContainer.setHgap(20);
        ordersContainer.setVgap(20);
        ordersContainer.setAlignment(Pos.TOP_LEFT);
        ordersContainer.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(ordersContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("scroll-pane");

        root.setCenter(scrollPane);

        // Initial Load
        refreshOrders();

        AnimationUtils.fadeIn(topBar, 500);
    }

    private void refreshOrders() {
        ordersContainer.getChildren().clear();

        for (Order order : orderService.getActiveOrders()) {
            VBox ticket = createOrderTicket(order);
            ordersContainer.getChildren().add(ticket);
            AnimationUtils.fadeIn(ticket, 400);
        }

        if (orderService.getActiveOrders().isEmpty()) {
            VBox emptyState = new VBox(15);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPrefWidth(800);

            Text emptyIcon = new Text("✅");
            emptyIcon.setStyle("-fx-font-size: 60px;");

            Text emptyText = new Text("All orders completed!");
            emptyText.getStyleClass().add("section-title");
            emptyText.setStyle("-fx-fill: #95A5A6;");

            emptyState.getChildren().addAll(emptyIcon, emptyText);
            ordersContainer.getChildren().add(emptyState);
        }
    }

    private VBox createOrderTicket(Order order) {
        VBox ticket = new VBox(10);
        ticket.setPrefWidth(260); // Optimal for 3-col grid
        ticket.setPrefHeight(320);
        ticket.getStyleClass().add("table-card");
        ticket.setStyle(
                "-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); -fx-background-radius: 10; -fx-padding: 15;");

        // Image Selection Logic
        String imagePath = "/images/food_sides.png"; // Default
        String itemName = order.getItem().toLowerCase();

        if (itemName.contains("pizza") || itemName.contains("fajita") || itemName.contains("feast")
                || itemName.contains("achari") || itemName.contains("lover")) {
            imagePath = "/images/food_pizza.png";
        } else if (itemName.contains("burger") || itemName.contains("smash") || itemName.contains("zinger")
                || itemName.contains("sizzler") || itemName.contains("grilled") || itemName.contains("chapli")) {
            imagePath = "/images/food_burger.png";
        } else if (itemName.contains("drink") || itemName.contains("cola") || itemName.contains("sprite")
                || itemName.contains("marg") || itemName.contains("lime") || itemName.contains("water")
                || itemName.contains("chai")) {
            imagePath = "/images/food_drink.png";
        }

        javafx.scene.image.ImageView itemImage = new javafx.scene.image.ImageView();
        try {
            itemImage.setImage(new javafx.scene.image.Image(getClass().getResource(imagePath).toExternalForm()));
            itemImage.setFitWidth(60);
            itemImage.setFitHeight(60);
            itemImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Could not load image: " + imagePath);
        }

        // Header (Table & Time)
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label tableLbl = new Label(order.getTable());
        tableLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #E74C3C;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label timeLbl = new Label(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLbl.setStyle("-fx-text-fill: #95A5A6;");
        header.getChildren().addAll(tableLbl, spacer, timeLbl);

        // Content Row with Image
        HBox contentRow = new HBox(15);
        contentRow.setAlignment(Pos.CENTER_LEFT);

        VBox textContent = new VBox(5);
        Label itemLbl = new Label(order.getItem());
        itemLbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        itemLbl.setWrapText(true);
        itemLbl.setMaxWidth(180);

        textContent.getChildren().add(itemLbl);
        contentRow.getChildren().addAll(itemImage, textContent);

        // Details Box
        VBox detailsBox = new VBox(5);
        detailsBox.setStyle("-fx-background-color: #F8F9F9; -fx-padding: 10; -fx-background-radius: 5;");

        if (order.getSize() != null && !order.getSize().isEmpty()) {
            detailsBox.getChildren().add(new Label("Size: " + order.getSize()));
        }
        if (order.getExtras() != null && !order.getExtras().isEmpty()) {
            Label extras = new Label("Extras: " + order.getExtras());
            extras.setStyle("-fx-text-fill: #27AE60; -fx-font-weight: bold;");
            detailsBox.getChildren().add(extras);
        }

        // Actions
        Button completeBtn = new Button("Mark Ready");
        completeBtn.getStyleClass().add("btn-success");
        completeBtn.setMaxWidth(Double.MAX_VALUE);
        completeBtn.setOnAction(e -> {
            orderService.removeOrder(order);
            // refresh handled by listener
        });

        VBox.setVgrow(detailsBox, Priority.ALWAYS);
        ticket.getChildren().addAll(header, new Separator(), contentRow, detailsBox, completeBtn);

        return ticket;
    }

    public BorderPane getView() {
        return root;
    }
}
