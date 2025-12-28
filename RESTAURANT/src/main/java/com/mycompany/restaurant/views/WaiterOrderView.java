package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.models.MenuItem;
import com.mycompany.restaurant.services.MenuService;
import com.mycompany.restaurant.services.OrderService;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.io.*;

/**
 * WaiterOrderView - Interactive order placement system
 */
public class WaiterOrderView {

    private BorderPane root;
    private RestaurantApp app;

    // Form fields
    private ComboBox<String> tableBox, categoryBox, itemBox, sizeBox;
    private CheckBox extraToppingsCheck, drinkCheck, wrapCheck;

    // Bill display
    private TextArea billArea;

    // Orders table
    private TableView<Order> orderTable;

    // Services
    private MenuService menuService;
    private OrderService orderService;

    public WaiterOrderView(RestaurantApp app) {
        this.app = app;
        this.menuService = MenuService.getInstance();
        this.orderService = OrderService.getInstance();

        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("scene-root");
        root.setPadding(new Insets(30));

        // Top bar
        VBox topBar = createTopBar();
        root.setTop(topBar);

        // Center: Split pane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.45);

        // Left: Order form
        VBox orderForm = createOrderForm();

        // Right: Bill and orders
        VBox billAndOrders = createBillAndOrders();

        splitPane.getItems().addAll(orderForm, billAndOrders);
        root.setCenter(splitPane);

        // Bottom nav
        HBox bottomNav = createBottomNav();
        root.setBottom(bottomNav);

        // Animations
        AnimationUtils.fadeIn(topBar, 500);
        AnimationUtils.slideUpWithDelay(orderForm, 600, 150);
        AnimationUtils.slideUpWithDelay(billAndOrders, 600, 250);
    }

    private VBox createTopBar() {
        VBox topBar = new VBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 20, 0));

        Text title = new Text("Order Management System");
        title.getStyleClass().add("page-title");

        Text subtitle = new Text("Take orders and manage customer requests");
        subtitle.getStyleClass().addAll("label-modern", "text-center");
        subtitle.setStyle("-fx-font-size: 16px;");

        topBar.getChildren().addAll(title, subtitle);
        return topBar;
    }

    private VBox createOrderForm() {
        VBox form = new VBox(18);
        form.getStyleClass().add("form-container");
        form.setPadding(new Insets(25));

        Text formTitle = new Text("📝 New Order");
        formTitle.getStyleClass().add("section-title");

        // Table selection
        VBox tableBox_container = createFormField("Table / Delivery:", tableBox = new ComboBox<>());
        refreshTableOptions();

        // Add listener to refresh when clicked? Or just rely on initial load?
        // Let's add a focus listener to refresh when user tries to pick
        tableBox.setOnMouseClicked(e -> refreshTableOptions());

        // Category
        VBox categoryBox_container = createFormField("Category:", categoryBox = new ComboBox<>());
        categoryBox.getItems().addAll("Pizza", "Burger", "Sides", "BBQ", "Drinks");
        categoryBox.getSelectionModel().selectFirst();

        // Item type
        VBox itemBox_container = createFormField("Item:", itemBox = new ComboBox<>());

        // Size
        VBox sizeBox_container = createFormField("Size (Pizzas Only):", sizeBox = new ComboBox<>());
        sizeBox.getItems().addAll("Medium", "Large", "XL");
        sizeBox.getSelectionModel().selectFirst();

        // Initial Update
        updateItemOptions();

        // Category change listener
        categoryBox.setOnAction(e -> updateItemOptions());

        // Checkboxes
        VBox checkBoxContainer = new VBox(10);
        Text extrasTitle = new Text("Add-ons:");
        extrasTitle.getStyleClass().add("label-modern");

        extraToppingsCheck = new CheckBox("Extra Toppings / Cheese (Rs. 200)");
        extraToppingsCheck.getStyleClass().add("check-box-modern");

        drinkCheck = new CheckBox("Add Drink (Rs. 290)"); // Simplified as distinct items are available
        drinkCheck.getStyleClass().add("check-box-modern");

        wrapCheck = new CheckBox("Wrap: WRAPTA HOO JAYE (Rs. 350)");
        wrapCheck.getStyleClass().add("check-box-modern");

        checkBoxContainer.getChildren().addAll(extrasTitle, extraToppingsCheck, drinkCheck, wrapCheck);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button placeBtn = new Button("🍕 Place Order");
        placeBtn.getStyleClass().add("btn-primary");
        placeBtn.setPrefWidth(150);
        placeBtn.setOnAction(e -> placeOrder());

        Button clearBtn = new Button("Clear");
        clearBtn.getStyleClass().add("btn-secondary");
        clearBtn.setPrefWidth(100);
        clearBtn.setOnAction(e -> clearBill());

        buttonBox.getChildren().addAll(placeBtn, clearBtn);

        form.getChildren().addAll(
                formTitle,
                tableBox_container,
                categoryBox_container,
                itemBox_container,
                sizeBox_container,
                checkBoxContainer,
                buttonBox);

        // Wrap form in ScrollPane
        ScrollPane formScroll = new ScrollPane(form);
        formScroll.setFitToWidth(true);
        formScroll.getStyleClass().add("scroll-pane");
        formScroll.setStyle("-fx-background-color: transparent;");

        VBox wrapper = new VBox(formScroll);
        VBox.setVgrow(formScroll, Priority.ALWAYS);

        return wrapper;
    }

    private VBox createFormField(String labelText, ComboBox<String> comboBox) {
        VBox fieldBox = new VBox(5);

        Label label = new Label(labelText);
        label.getStyleClass().add("label-modern");

        comboBox.getStyleClass().add("combo-box-modern");
        comboBox.setPrefWidth(Double.MAX_VALUE);

        fieldBox.getChildren().addAll(label, comboBox);
        return fieldBox;
    }

    private VBox createBillAndOrders() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(25));

        // Bill area
        VBox billCard = new VBox(10);
        billCard.getStyleClass().add("glass-card");
        billCard.setPadding(new Insets(20));

        Text billTitle = new Text("💵 Current Bill");
        billTitle.getStyleClass().add("section-title");

        billArea = new TextArea();
        billArea.getStyleClass().add("text-area-modern");
        billArea.setEditable(false);
        billArea.setPrefHeight(180);
        billArea.setText("Select items and calculate order...");
        billArea.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace;");

        billCard.getChildren().addAll(billTitle, billArea);

        // Orders table
        VBox ordersCard = new VBox(10);
        ordersCard.getStyleClass().add("glass-card");
        ordersCard.setPadding(new Insets(20));

        Text ordersTitle = new Text("📋 Active Orders");
        ordersTitle.getStyleClass().add("section-title");

        orderTable = new TableView<>(orderService.getActiveOrders()); // Bind to Shared Service
        orderTable.getStyleClass().add("table-view");
        orderTable.setPrefHeight(250);

        // Columns
        TableColumn<Order, String> tableCol = new TableColumn<>("Table");
        tableCol.setCellValueFactory(new PropertyValueFactory<>("table"));
        tableCol.setPrefWidth(100);

        TableColumn<Order, String> pizzaCol = new TableColumn<>("Item");
        pizzaCol.setCellValueFactory(new PropertyValueFactory<>("pizza"));
        pizzaCol.setPrefWidth(150);

        TableColumn<Order, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setPrefWidth(80);

        TableColumn<Order, String> extrasCol = new TableColumn<>("Extras");
        extrasCol.setCellValueFactory(new PropertyValueFactory<>("extras"));
        extrasCol.setPrefWidth(120);

        TableColumn<Order, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalCol.setPrefWidth(100);

        orderTable.getColumns().addAll(tableCol, pizzaCol, sizeCol, extrasCol, totalCol);

        Button cancelBtn = new Button("Cancel Selected");
        cancelBtn.getStyleClass().add("btn-danger");
        cancelBtn.setOnAction(e -> cancelOrder());

        ordersCard.getChildren().addAll(ordersTitle, orderTable, cancelBtn);

        container.getChildren().addAll(billCard, ordersCard);
        VBox.setVgrow(ordersCard, Priority.ALWAYS);

        return container;
    }

    private HBox createBottomNav() {
        HBox bottomNav = new HBox(15);
        bottomNav.setAlignment(Pos.CENTER);
        bottomNav.setPadding(new Insets(20, 0, 0, 0));

        Button backBtn = new Button("← Back to Login");
        backBtn.getStyleClass().add("btn-back");
        backBtn.setOnAction(e -> app.showLoginScreen());

        bottomNav.getChildren().add(backBtn);
        return bottomNav;
    }

    private void updateItemOptions() {
        itemBox.getItems().clear();
        String category = categoryBox.getValue();

        // Hide Size box if not Pizza
        boolean isPizza = "Pizza".equals(category);
        sizeBox.getParent().setVisible(isPizza);
        sizeBox.getParent().setManaged(isPizza);

        // Fetch items from Service
        ObservableList<MenuItem> items = menuService.getItemsByCategory(category);
        for (MenuItem item : items) {
            itemBox.getItems().add(item.getName());
        }

        if (itemBox.getItems().size() > 0) {
            itemBox.getSelectionModel().selectFirst();
        }
    }

    private void refreshTableOptions() {
        String currentSelection = tableBox.getValue();
        tableBox.getItems().clear();
        tableBox.getItems().add("Delivery");

        java.util.Set<String> occupied = orderService.getOccupiedTables();

        for (int i = 1; i <= 30; i++) {
            String tableName = "Table " + i;
            if (!occupied.contains(tableName)) {
                tableBox.getItems().add(tableName);
            }
        }

        if (currentSelection != null && tableBox.getItems().contains(currentSelection)) {
            tableBox.setValue(currentSelection);
        } else {
            tableBox.getSelectionModel().selectFirst();
        }
    }

    private void placeOrder() {
        String table = tableBox.getValue();
        String category = categoryBox.getValue();
        String itemName = itemBox.getValue();
        String size = sizeBox.getValue();

        // Find item to get base price
        MenuItem menuItem = menuService.getAllItems().stream()
                .filter(i -> i.getName().equals(itemName))
                .findFirst()
                .orElse(null);

        if (menuItem == null) {
            showError("Please select a valid item.");
            return;
        }

        // Calculate price
        double price = menuItem.getPrice(); // Base price

        // Apply Pizza Sizing Logic
        if ("Pizza".equals(category)) {
            if ("Large".equals(size)) {
                price += 300;
            } else if ("XL".equals(size)) {
                price += 600;
            }
        }

        double total = price;
        StringBuilder extras = new StringBuilder();

        if (extraToppingsCheck.isSelected()) {
            total += 200;
            extras.append("Toppings ");
        }
        if (drinkCheck.isSelected()) {
            total += 290;
            extras.append("Drink "); // This checkbox might be redundant now that we have Drinks category
        }
        if (wrapCheck.isSelected()) {
            total += 350;
            extras.append("Wrap ");
        }

        // Generate bill
        String bill = generateBill(table, itemName, category, size, extras.toString(), (int) total);
        billArea.setText(bill);

        // Add to orders
        // Add to orders
        Order order = new Order(table, itemName, size, extras.toString().trim(), "Rs. " + (int) total);
        orderService.addOrder(order); // Add to Shared Service

        // Show success
        showSuccess("Order placed successfully!");
        refreshTableOptions();
        clearBill();
    }

    private String generateBill(String table, String item, String category, String size, String extras, int total) {
        StringBuilder bill = new StringBuilder();
        bill.append("═══════════════════════════════════\n");
        bill.append("       RESTAURANT BILL\n");
        bill.append("═══════════════════════════════════\n\n");
        bill.append(String.format("Table/Delivery: %s\n", table));
        bill.append(String.format("Item: %s\n", item));
        if (!size.isEmpty() && sizeBox.getParent().isVisible()) {
            bill.append(String.format("Size: %s\n", size));
        }
        bill.append(String.format("Category: %s\n", category));
        bill.append(String.format("Extras: %s\n", extras.isEmpty() ? "None" : extras));
        bill.append("\n───────────────────────────────────\n");
        bill.append(String.format("TOTAL AMOUNT: Rs. %d/-\n", total));
        bill.append("═══════════════════════════════════\n");
        bill.append("       Thank you!\n");
        bill.append("═══════════════════════════════════");

        return bill.toString();
    }

    private void clearBill() {
        billArea.setText("Select items and calculate order...");
    }

    private void cancelOrder() {
        Order selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an order to cancel.");
            return;
        }

        orderService.removeOrder(selected);
        showSuccess("Order cancelled successfully!");
    }

    // File handling removed to use Database-only persistence

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/styles/application.css").toExternalForm());

        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/styles/application.css").toExternalForm());

        alert.showAndWait();
    }

    public BorderPane getView() {
        return root;
    }

    // Order data class
    public static class Order {
        private String table, pizza, size, extras, total;

        public Order(String table, String pizza, String size, String extras, String total) {
            this.table = table;
            this.pizza = pizza;
            this.size = size;
            this.extras = extras;
            this.total = total;
        }

        public String toFileString() {
            return table + "," + pizza + "," + size + "," + extras + "," + total;
        }

        public String getTable() {
            return table;
        }

        public String getPizza() {
            return pizza;
        }

        // For compatibility, we'll keep getPizza but conceptually it's 'item'
        public String getItem() {
            return pizza;
        }

        public String getSize() {
            return size;
        }

        public String getExtras() {
            return extras;
        }

        public String getTotal() {
            return total;
        }
    }
}
