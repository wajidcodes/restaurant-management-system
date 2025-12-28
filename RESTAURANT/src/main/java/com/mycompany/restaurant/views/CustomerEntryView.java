package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.io.*;
import java.time.LocalDate;

/**
 * CustomerEntryView - Modern customer reservation/entry logging interface
 */
public class CustomerEntryView {

    private BorderPane root;
    private RestaurantApp app;

    // Form fields
    private TextField customerNameField, timeField;
    private DatePicker datePicker;
    private ComboBox<String> reasonBox;

    // Entry list
    private ListView<String> entryListView;
    private ObservableList<String> entries;

    // File
    private File file = new File("customer_entries.txt");

    public CustomerEntryView(RestaurantApp app) {
        this.app = app;
        entries = FXCollections.observableArrayList();
        createView();
        loadFromFile();
    }

    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("scene-root");
        root.setPadding(new Insets(30));

        // Top bar
        VBox topBar = createTopBar();
        root.setTop(topBar);

        // Center content
        VBox centerContent = createCenterContent();
        root.setCenter(centerContent);

        // Bottom nav
        HBox bottomNav = createBottomNav();
        root.setBottom(bottomNav);

        // Animations
        AnimationUtils.fadeIn(topBar, 500);
        AnimationUtils.slideUpWithDelay(centerContent, 600, 200);
    }

    private VBox createTopBar() {
        VBox topBar = new VBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 20, 0));

        Text title = new Text("Customer Entry Panel");
        title.getStyleClass().add("page-title");

        Text subtitle = new Text("Log customer reservations and visits");
        subtitle.getStyleClass().addAll("label-modern", "text-center");
        subtitle.setStyle("-fx-font-size: 16px;");

        topBar.getChildren().addAll(title, subtitle);
        return topBar;
    }

    private VBox createCenterContent() {
        VBox content = new VBox(25);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(20));
        content.setMaxWidth(900);

        // Form card
        VBox formCard = createFormCard();

        // Entries list card
        VBox listCard = createListCard();

        content.getChildren().addAll(formCard, listCard);

        // Center the content
        HBox wrapper = new HBox(content);
        wrapper.setAlignment(Pos.CENTER);

        // Return wrapper in VBox to use VBox.setVgrow
        VBox containerVBox = new VBox(wrapper);
        VBox.setVgrow(wrapper, Priority.ALWAYS);

        return containerVBox;
    }

    private VBox createFormCard() {
        VBox card = new VBox(20);
        card.getStyleClass().add("glass-card");
        card.setPadding(new Insets(30));
        card.setMaxWidth(800);

        Text cardTitle = new Text("New Entry");
        cardTitle.getStyleClass().add("section-title");

        // Grid for form fields
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        // Customer Name
        Label nameLabel = new Label("Customer Name:");
        nameLabel.getStyleClass().add("label-modern");
        customerNameField = new TextField();
        customerNameField.getStyleClass().add("text-field-modern");
        customerNameField.setPromptText("Enter customer name");
        customerNameField.setPrefWidth(300);

        // Date
        Label dateLabel = new Label("Date:");
        dateLabel.getStyleClass().add("label-modern");
        datePicker = new DatePicker(LocalDate.now());
        datePicker.getStyleClass().add("date-picker-modern");
        datePicker.setPrefWidth(300);

        // Time
        Label timeLabel = new Label("Time:");
        timeLabel.getStyleClass().add("label-modern");
        timeField = new TextField();
        timeField.getStyleClass().add("text-field-modern");
        timeField.setPromptText("e.g., 7:00 PM");
        timeField.setPrefWidth(300);

        // Reason
        Label reasonLabel = new Label("Visit Reason:");
        reasonLabel.getStyleClass().add("label-modern");
        reasonBox = new ComboBox<>();
        reasonBox.getStyleClass().add("combo-box-modern");
        reasonBox.getItems().addAll("Simple Order", "Birthday", "Anniversary", "Other");
        reasonBox.getSelectionModel().selectFirst();
        reasonBox.setPrefWidth(300);

        grid.add(nameLabel, 0, 0);
        grid.add(customerNameField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(timeLabel, 0, 2);
        grid.add(timeField, 1, 2);
        grid.add(reasonLabel, 0, 3);
        grid.add(reasonBox, 1, 3);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button addBtn = new Button("Add Entry");
        addBtn.getStyleClass().add("btn-primary");
        addBtn.setPrefWidth(120);
        addBtn.setOnAction(e -> addEntry());

        Button resetBtn = new Button("Reset");
        resetBtn.getStyleClass().add("btn-secondary");
        resetBtn.setPrefWidth(120);
        resetBtn.setOnAction(e -> resetForm());

        buttonBox.getChildren().addAll(addBtn, resetBtn);

        card.getChildren().addAll(cardTitle, grid, buttonBox);

        return card;
    }

    private VBox createListCard() {
        VBox card = new VBox(15);
        card.getStyleClass().add("glass-card");
        card.setPadding(new Insets(25));
        card.setMaxWidth(800);
        card.setPrefHeight(300);

        Text cardTitle = new Text("Recent Entries");
        cardTitle.getStyleClass().add("section-title");

        entryListView = new ListView<>(entries);
        entryListView.getStyleClass().add("list-view");
        VBox.setVgrow(entryListView, Priority.ALWAYS);

        card.getChildren().addAll(cardTitle, entryListView);

        return card;
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

    private void addEntry() {
        String name = customerNameField.getText().trim();
        String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
        String time = timeField.getText().trim();
        String reason = reasonBox.getValue();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            showError("Please fill all fields!");
            return;
        }

        String entry = "Name: " + name + ", Date: " + date + ", Time: " + time + ", Reason: " + reason;
        entries.add(0, entry); // Add to beginning
        appendToFile(entry);
        resetForm();
        showSuccess("Entry added successfully!");
    }

    private void resetForm() {
        customerNameField.clear();
        datePicker.setValue(LocalDate.now());
        timeField.clear();
        reasonBox.getSelectionModel().selectFirst();
    }

    private void appendToFile(String entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(entry);
            bw.newLine();
        } catch (IOException e) {
            showError("Error saving to file.");
        }
    }

    private void loadFromFile() {
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                entries.add(line);
            }
        } catch (IOException e) {
            showError("Error reading from file.");
        }
    }

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
}
