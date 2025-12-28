package com.mycompany.restaurant.views;

import com.mycompany.restaurant.models.Table;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * Table Management View - Visual floor plan of restaurant tables
 */
public class TableManagementView {
    private BorderPane root;
    private ObservableList<Table> tables;
    private GridPane tableGrid;

    public TableManagementView() {
        tables = FXCollections.observableArrayList();
        initializeTables();
        createView();
    }

    private void initializeTables() {
        // Create 20 tables with varying capacities
        int[] capacities = { 2, 4, 4, 2, 6, 4, 4, 2, 6, 4, 2, 4, 6, 4, 2, 6, 4, 4, 2, 4 };
        for (int i = 0; i < 20; i++) {
            tables.add(new Table(i + 1, capacities[i]));
        }

        // Set some initial states for demo
        tables.get(1).seatParty(2, "John");
        tables.get(4).reserve();
        tables.get(7).seatParty(4, "Sarah");
        tables.get(10).setStatus("CLEANING");
    }

    private void createView() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: #ECF0F1;");
        root.setPadding(new Insets(30));

        // Top section
        VBox topSection = new VBox(15);

        Text title = new Text("Table Management");
        title.getStyleClass().add("page-title");

        Text subtitle = new Text("Monitor and manage restaurant seating");
        subtitle.getStyleClass().add("text-secondary");
        subtitle.setStyle("-fx-font-size: 16px;");

        // Status legend
        HBox legend = new HBox(20);
        legend.setPadding(new Insets(15, 0, 0, 0));

        legend.getChildren().addAll(
                createLegendItem("🟢", "Available"),
                createLegendItem("🔴", "Occupied"),
                createLegendItem("🟡", "Reserved"),
                createLegendItem("🔵", "Cleaning"));

        topSection.getChildren().addAll(title, subtitle, legend);
        root.setTop(topSection);

        // Table grid
        tableGrid = new GridPane();
        tableGrid.setHgap(20);
        tableGrid.setVgap(20);
        tableGrid.setPadding(new Insets(30));
        tableGrid.setAlignment(Pos.CENTER);

        // Arrange tables in 4x5 grid
        int col = 0, row = 0;
        for (Table table : tables) {
            VBox card = createTableCard(table);
            tableGrid.add(card, col, row);

            int delay = (row * 5 + col) * 50;
            AnimationUtils.fadeInWithDelay(card, 300, delay);

            col++;
            if (col >= 5) {
                col = 0;
                row++;
            }
        }

        ScrollPane scroll = new ScrollPane(tableGrid);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background-color: transparent;");

        root.setCenter(scroll);

        AnimationUtils.fadeIn(topSection, 500);
    }

    private VBox createTableCard(Table table) {
        VBox card = new VBox(12);
        card.getStyleClass().add("table-card");
        card.setPrefSize(160, 160);
        card.setAlignment(Pos.CENTER);

        // Apply status styling
        String status = table.getStatus();
        card.getStyleClass().add("table-" + status.toLowerCase());

        // Table icon
        Text icon = new Text("🍽️");
        icon.setStyle("-fx-font-size: 36px;");

        // Table number
        Text number = new Text("Table " + table.getTableNumber());
        number.getStyleClass().add("table-number");

        // Capacity
        Text capacity = new Text("👥 " + table.getCapacity() + " seats");
        capacity.getStyleClass().add("table-capacity");

        card.getChildren().addAll(icon, number, capacity);

        // Add status info if occupied
        if (status.equals("OCCUPIED")) {
            long minutes = table.getOccupiedMinutes();
            Text time = new Text("⏱️ " + minutes + " min");
            time.setStyle("-fx-font-size: 13px; -fx-fill: #C0392B;");
            card.getChildren().add(time);
        } else if (status.equals("RESERVED")) {
            Text reserved = new Text("Reserved");
            reserved.setStyle("-fx-font-size: 13px; -fx-fill: #F39C12; -fx-font-weight: bold;");
            card.getChildren().add(reserved);
        }

        // Click handler
        card.setOnMouseClicked(e -> showTableDialog(table));

        // Hover effect
        AnimationUtils.addHoverEffect(card);

        return card;
    }

    private HBox createLegendItem(String icon, String label) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);

        Text iconText = new Text(icon);
        iconText.setStyle("-fx-font-size: 18px;");

        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-size: 14px; -fx-fill: #2C3E50; -fx-font-weight: 500;");

        item.getChildren().addAll(iconText, labelText);
        return item;
    }

    private void showTableDialog(Table table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Table " + table.getTableNumber());
        dialog.setHeaderText("Table Management - " + table.getTableNumber());

        // Dialog content
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Table info
        Text info = new Text(
                "Capacity: " + table.getCapacity() + " seats\n" +
                        "Status: " + table.getStatus());
        info.setStyle("-fx-font-size: 14px;");

        content.getChildren().add(info);

        // Action buttons based on status
        String status = table.getStatus();

        if (status.equals("AVAILABLE") || status.equals("CLEANING")) {
            // Seat party option
            TextField partySizeField = new TextField();
            partySizeField.setPromptText("Party size");
            partySizeField.getStyleClass().add("text-field-modern");

            TextField serverField = new TextField();
            serverField.setPromptText("Server name");
            serverField.getStyleClass().add("text-field-modern");

            Button seatBtn = new Button("Seat Party");
            seatBtn.getStyleClass().add("btn-success");
            seatBtn.setPrefWidth(200);
            seatBtn.setOnAction(e -> {
                try {
                    int partySize = Integer.parseInt(partySizeField.getText());
                    String server = serverField.getText();
                    if (!server.trim().isEmpty()) {
                        table.seatParty(partySize, server);
                        refreshView();
                        dialog.close();
                        showSuccessAlert("Table " + table.getTableNumber() + " has been seated!");
                    }
                } catch (NumberFormatException ex) {
                    showErrorAlert("Please enter a valid party size");
                }
            });

            content.getChildren().addAll(
                    new Label("Party Size:"),
                    partySizeField,
                    new Label("Server Name:"),
                    serverField,
                    seatBtn);

            if (status.equals("CLEANING")) {
                Button makeAvailableBtn = new Button("Mark as Available");
                makeAvailableBtn.getStyleClass().add("btn-primary");
                makeAvailableBtn.setPrefWidth(200);
                makeAvailableBtn.setOnAction(e -> {
                    table.makeAvailable();
                    refreshView();
                    dialog.close();
                });
                content.getChildren().add(makeAvailableBtn);
            }
        } else if (status.equals("OCCUPIED")) {
            // Show occupied info
            Text occupiedInfo = new Text(
                    "Server: " + table.getAssignedServer() + "\n" +
                            "Party Size: " + table.getPartySize() + "\n" +
                            "Duration: " + table.getOccupiedMinutes() + " minutes");
            occupiedInfo.setStyle("-fx-font-size: 14px;");

            Button clearBtn = new Button("Clear Table");
            clearBtn.getStyleClass().add("btn-danger");
            clearBtn.setPrefWidth(200);
            clearBtn.setOnAction(e -> {
                table.clearTable();
                refreshView();
                dialog.close();
                showSuccessAlert("Table " + table.getTableNumber() + " cleared!");
            });

            content.getChildren().addAll(occupiedInfo, clearBtn);
        } else if (status.equals("RESERVED")) {
            Button cancelReservationBtn = new Button("Cancel Reservation");
            cancelReservationBtn.getStyleClass().add("btn-warning");
            // Force high-contrast orange background with white text
            cancelReservationBtn
                    .setStyle("-fx-background-color: #F39C12; -fx-text-fill: white; -fx-font-weight: bold;");
            cancelReservationBtn.setPrefWidth(200);
            cancelReservationBtn.setOnAction(e -> {
                table.makeAvailable();
                refreshView();
                dialog.close();
            });

            content.getChildren().add(cancelReservationBtn);
        }

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Apply stylesheet
        dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/styles/application.css").toExternalForm());

        dialog.showAndWait();
    }

    private void refreshView() {
        // Recreate the view to reflect changes
        createView();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/styles/application.css").toExternalForm());
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/styles/application.css").toExternalForm());
        alert.showAndWait();
    }

    public BorderPane getView() {
        return root;
    }
}
