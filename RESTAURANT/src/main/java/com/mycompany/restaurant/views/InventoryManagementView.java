package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.models.InventoryItem;
import com.mycompany.restaurant.services.InventoryService;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * InventoryManagementView - Track stock and ingredients
 */
public class InventoryManagementView {

    private BorderPane root;
    private RestaurantApp app;
    private InventoryService service;
    private TableView<InventoryItem> table;

    public InventoryManagementView(RestaurantApp app) {
        this.app = app;
        this.service = InventoryService.getInstance();
        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("scene-root");
        root.setPadding(new Insets(30));

        // Top Bar
        VBox topBar = new VBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 20, 0));

        Text title = new Text("Inventory Management");
        title.getStyleClass().add("page-title");
        topBar.getChildren().add(title);
        root.setTop(topBar);

        // Content
        VBox content = new VBox(20);

        // Stats Cards - We can bind these to list size later
        HBox stats = createStats();

        // Table Action
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        Button addBtn = new Button("Add Stock");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setOnAction(e -> showAddDialog());
        actions.getChildren().add(addBtn);

        // Table
        table = new TableView<>(service.getInventory());
        table.getStyleClass().add("table-view");

        TableColumn<InventoryItem, String> nameCol = new TableColumn<>("Item");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("itemName")); // Corrected property name

        TableColumn<InventoryItem, Number> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("currentStock")); // Corrected property name

        TableColumn<InventoryItem, String> unitCol = new TableColumn<>("Unit");
        unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));

        TableColumn<InventoryItem, Number> thresholdCol = new TableColumn<>("Low Threshold");
        thresholdCol.setCellValueFactory(new PropertyValueFactory<>("minStock")); // Corrected property name

        table.getColumns().addAll(nameCol, qtyCol, unitCol, thresholdCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        content.getChildren().addAll(stats, actions, table);
        root.setCenter(content);

        AnimationUtils.fadeIn(root, 500);
    }

    private HBox createStats() {
        HBox stats = new HBox(20);
        stats.setAlignment(Pos.CENTER);

        stats.getChildren().addAll(
                createCard("Total Items", String.valueOf(service.getInventory().size())),
                createCard("Low Stock", "0") // Placeholder
        );
        return stats;
    }

    private VBox createCard(String title, String value) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.getStyleClass().add("glass-card");
        card.setAlignment(Pos.CENTER);

        Text t = new Text(title);
        t.getStyleClass().add("label-modern");

        Text v = new Text(value);
        v.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #2C3E50;");

        card.getChildren().addAll(t, v);
        return card;
    }

    private void showAddDialog() {
        Dialog<InventoryItem> dialog = new Dialog<>();
        dialog.setTitle("Add Stock");
        dialog.setHeaderText("Add New Inventory Item");

        DialogPane pane = dialog.getDialogPane();
        pane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField qty = new TextField();
        qty.setPromptText("Quantity");
        TextField unit = new TextField();
        unit.setPromptText("Unit (kg/units)");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(qty, 1, 1);
        grid.add(new Label("Unit:"), 0, 2);
        grid.add(unit, 1, 2);

        pane.setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    String n = name.getText().trim();
                    String qStr = qty.getText().trim();
                    String u = unit.getText().trim();

                    if (n.isEmpty() || qStr.isEmpty() || u.isEmpty()) {
                        return null; // validation constraint
                    }

                    double q = Double.parseDouble(qStr);
                    if (q < 0)
                        return null;

                    service.addItem(n, q, u);
                    return new InventoryItem(0, n, q, u, 10); // Return dummy just to trigger 'present'
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait(); // We handle adding inside the converter or via service directly above
    }

    public BorderPane getView() {
        return root;
    }
}
