package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.models.MenuItem;
import com.mycompany.restaurant.services.MenuService;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * MenuManagementView - Admin interface for Menu
 */
public class MenuManagementView {

    private BorderPane root;
    private RestaurantApp app;
    private MenuService menuService;
    private TableView<MenuItem> menuTable;

    public MenuManagementView(RestaurantApp app) {
        this.app = app;
        this.menuService = MenuService.getInstance();
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

        Text title = new Text("Menu Management");
        title.getStyleClass().add("page-title");
        topBar.getChildren().add(title);
        root.setTop(topBar);

        // Content
        HBox content = new HBox(30);
        content.setAlignment(Pos.CENTER);

        // Left: Form
        VBox form = createAddForm();

        // Right: Table
        VBox tableBox = createTable();
        HBox.setHgrow(tableBox, Priority.ALWAYS);

        content.getChildren().addAll(tableBox, form);
        root.setCenter(content);

        AnimationUtils.fadeIn(root, 500);
    }

    private VBox createAddForm() {
        VBox form = new VBox(15);
        form.setPrefWidth(350);
        form.setPadding(new Insets(20));
        form.getStyleClass().add("glass-card");

        Text header = new Text("Add New Item");
        header.getStyleClass().add("section-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Item Name");
        nameField.getStyleClass().add("text-field-modern");

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Regular", "Premium", "Extreme", "Burgers", "Pasta", "Drinks");
        categoryBox.setPromptText("Category");
        categoryBox.setMaxWidth(Double.MAX_VALUE);
        categoryBox.getStyleClass().add("combo-box-modern");

        TextField priceField = new TextField();
        priceField.setPromptText("Price (Rs)");
        priceField.getStyleClass().add("text-field-modern");

        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefHeight(100);
        descField.getStyleClass().add("text-area-modern");

        Button addBtn = new Button("Add Item");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String cat = categoryBox.getValue();
                String priceText = priceField.getText().trim();
                String desc = descField.getText().trim();

                if (name.isEmpty()) {
                    showError("Item Name cannot be empty.");
                    return;
                }
                if (cat == null) {
                    showError("Please select a Category.");
                    return;
                }
                if (priceText.isEmpty()) {
                    showError("Price cannot be empty.");
                    return;
                }

                double price = Double.parseDouble(priceText);
                if (price <= 0) {
                    showError("Price must be greater than 0.");
                    return;
                }

                // Add to Service & DB
                MenuItem newItem = new MenuItem(0, name, cat, price, desc);
                menuService.add(newItem);

                // Clear
                nameField.clear();
                priceField.clear();
                descField.clear();
                categoryBox.getSelectionModel().clearSelection();

                Alert success = new Alert(Alert.AlertType.INFORMATION, "Item added successfully!");
                success.show();

            } catch (NumberFormatException ex) {
                showError("Invalid Price. Please enter a valid number.");
            }
        });

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.getStyleClass().add("btn-danger");
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setOnAction(e -> {
            MenuItem selected = menuTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                menuService.remove(selected);
            }
        });

        form.getChildren().addAll(header, nameField, categoryBox, priceField, descField, addBtn, new Separator(),
                deleteBtn);
        return form;
    }

    private VBox createTable() {
        VBox box = new VBox(10);

        menuTable = new TableView<>(menuService.getAllItems());
        menuTable.getStyleClass().add("table-view");

        TableColumn<MenuItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MenuItem, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price")); // Ideally format this

        menuTable.getColumns().addAll(nameCol, catCol, priceCol);
        menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox.setVgrow(menuTable, Priority.ALWAYS);
        box.getChildren().add(menuTable);
        return box;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.show();
    }

    public BorderPane getView() {
        return root;
    }
}
