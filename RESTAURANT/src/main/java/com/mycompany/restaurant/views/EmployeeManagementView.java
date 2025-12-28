package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.io.*;
import java.util.*;

/**
 * EmployeeManagementView - Modern CRUD interface for employee records
 */
public class EmployeeManagementView {

    private BorderPane root;
    private RestaurantApp app;

    // Form fields
    private TextField nameField, addressField, emailField, ageField, hoursField, salaryField;
    private ComboBox<String> roleBox;

    // Table
    private TableView<Employee> table;
    private ObservableList<Employee> employeeList;

    // File
    private File file = new File("employees.txt");

    public EmployeeManagementView(RestaurantApp app) {
        this.app = app;
        employeeList = FXCollections.observableArrayList();
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

        // Center: Split pane
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.4);

        // Left: Form
        VBox formContainer = createFormPanel();

        // Right: Table
        VBox tableContainer = createTablePanel();

        splitPane.getItems().addAll(formContainer, tableContainer);
        root.setCenter(splitPane);

        // Bottom: Navigation
        HBox bottomNav = createBottomNav();
        root.setBottom(bottomNav);

        // Animate components
        AnimationUtils.fadeIn(topBar, 500);
        AnimationUtils.slideUpWithDelay(formContainer, 600, 200);
        AnimationUtils.slideUpWithDelay(tableContainer, 600, 300);
    }

    private VBox createTopBar() {
        VBox topBar = new VBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 20, 0));

        Text title = new Text("Employee Management");
        title.getStyleClass().add("page-title");

        Text subtitle = new Text("Manage employee records and information");
        subtitle.getStyleClass().addAll("label-modern", "text-center");
        subtitle.setStyle("-fx-font-size: 16px;");

        topBar.getChildren().addAll(title, subtitle);
        return topBar;
    }

    private VBox createFormPanel() {
        VBox formPanel = new VBox(15);
        formPanel.getStyleClass().add("form-container");
        formPanel.setPadding(new Insets(25));

        Text formTitle = new Text("Employee Details");
        formTitle.getStyleClass().add("section-title");

        // Name
        VBox nameBox = createFormField("Full Name:", nameField = new TextField());

        // Address
        VBox addressBox = createFormField("Address:", addressField = new TextField());

        // Email
        VBox emailBox = createFormField("Email:", emailField = new TextField());

        // Age
        VBox ageBox = createFormField("Age:", ageField = new TextField());

        // Hours
        VBox hoursBox = createFormField("Working Hours:", hoursField = new TextField());

        // Salary
        VBox salaryBox = createFormField("Salary:", salaryField = new TextField());

        // Role
        VBox roleBox_container = new VBox(5);
        Label roleLabel = new Label("Employment Role:");
        roleLabel.getStyleClass().add("label-modern");

        roleBox = new ComboBox<>();
        roleBox.getStyleClass().add("combo-box-modern");
        roleBox.setPrefWidth(Double.MAX_VALUE);
        roleBox.getItems().addAll("Chief", "Waiter", "Manager", "Swipper", "Guard", "Delivery Boy");
        roleBox.getSelectionModel().selectFirst();

        roleBox_container.getChildren().addAll(roleLabel, roleBox);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addBtn = new Button("Add Employee");
        addBtn.getStyleClass().add("btn-success");
        addBtn.setPrefWidth(150);
        addBtn.setOnAction(e -> addEmployee());

        Button editBtn = new Button("Update Selected");
        editBtn.getStyleClass().add("btn-primary");
        editBtn.setPrefWidth(150);
        editBtn.setOnAction(e -> editEmployee());

        Button clearBtn = new Button("Clear Form");
        clearBtn.getStyleClass().add("btn-secondary");
        clearBtn.setPrefWidth(150);
        clearBtn.setOnAction(e -> clearForm());

        buttonBox.getChildren().addAll(addBtn, editBtn);

        HBox clearBox = new HBox(clearBtn);
        clearBox.setAlignment(Pos.CENTER);

        formPanel.getChildren().addAll(
                formTitle,
                nameBox,
                addressBox,
                emailBox,
                ageBox,
                hoursBox,
                salaryBox,
                roleBox_container,
                buttonBox,
                clearBox);

        // Wrap in ScrollPane
        ScrollPane scrollPane = new ScrollPane(formPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");
        scrollPane.setStyle("-fx-background-color: transparent;");

        // Return VBox containing ScrollPane
        VBox wrapper = new VBox(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return wrapper;
    }

    private VBox createFormField(String labelText, TextField textField) {
        VBox fieldBox = new VBox(5);

        Label label = new Label(labelText);
        label.getStyleClass().add("label-modern");

        textField.getStyleClass().add("text-field-modern");
        textField.setPromptText("Enter " + labelText.toLowerCase().replace(":", ""));

        fieldBox.getChildren().addAll(label, textField);
        return fieldBox;
    }

    private VBox createTablePanel() {
        VBox tablePanel = new VBox(15);
        tablePanel.setPadding(new Insets(25));

        Text tableTitle = new Text("Employee Records");
        tableTitle.getStyleClass().add("section-title");

        // Create table
        table = new TableView<>();
        table.setItems(employeeList);
        table.getStyleClass().add("table-view");

        // Columns
        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Employee, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(100);

        TableColumn<Employee, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(60);

        TableColumn<Employee, String> hoursCol = new TableColumn<>("Hours");
        hoursCol.setCellValueFactory(new PropertyValueFactory<>("hours"));
        hoursCol.setPrefWidth(80);

        TableColumn<Employee, String> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryCol.setPrefWidth(100);

        TableColumn<Employee, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(180);

        TableColumn<Employee, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(150);

        table.getColumns().addAll(nameCol, roleCol, ageCol, hoursCol, salaryCol, emailCol, addressCol);

        // Delete button
        HBox deleteBox = new HBox();
        deleteBox.setAlignment(Pos.CENTER);

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.getStyleClass().add("btn-danger");
        deleteBtn.setPrefWidth(150);
        deleteBtn.setOnAction(e -> deleteEmployee());

        deleteBox.getChildren().add(deleteBtn);

        // Table click handler to populate form
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });

        tablePanel.getChildren().addAll(tableTitle, table, deleteBox);
        VBox.setVgrow(table, Priority.ALWAYS);

        return tablePanel;
    }

    private HBox createBottomNav() {
        HBox bottomNav = new HBox(15);
        bottomNav.setAlignment(Pos.CENTER);
        bottomNav.setPadding(new Insets(20, 0, 0, 0));

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.getStyleClass().add("btn-back");
        backBtn.setOnAction(e -> goBack());

        bottomNav.getChildren().add(backBtn);
        return bottomNav;
    }

    private void addEmployee() {
        if (!validateForm()) {
            showError("Please fill all fields correctly.");
            return;
        }

        Employee emp = new Employee(
                nameField.getText(),
                addressField.getText(),
                emailField.getText(),
                ageField.getText(),
                hoursField.getText(),
                salaryField.getText(),
                roleBox.getValue());

        employeeList.add(emp);
        appendToFile(emp);
        clearForm();
        showSuccess("Employee added successfully!");
    }

    private void editEmployee() {
        Employee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an employee to edit.");
            return;
        }

        if (!validateForm()) {
            showError("Please fill all fields correctly.");
            return;
        }

        selected.setName(nameField.getText());
        selected.setAddress(addressField.getText());
        selected.setEmail(emailField.getText());
        selected.setAge(ageField.getText());
        selected.setHours(hoursField.getText());
        selected.setSalary(salaryField.getText());
        selected.setRole(roleBox.getValue());

        table.refresh();
        rewriteFile();
        clearForm();
        showSuccess("Employee updated successfully!");
    }

    private void deleteEmployee() {
        Employee selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an employee to delete.");
            return;
        }

        employeeList.remove(selected);
        rewriteFile();
        clearForm();
        showSuccess("Employee deleted successfully!");
    }

    private void populateForm(Employee emp) {
        nameField.setText(emp.getName());
        addressField.setText(emp.getAddress());
        emailField.setText(emp.getEmail());
        ageField.setText(emp.getAge());
        hoursField.setText(emp.getHours());
        salaryField.setText(emp.getSalary());
        roleBox.setValue(emp.getRole());
    }

    private void clearForm() {
        nameField.clear();
        addressField.clear();
        emailField.clear();
        ageField.clear();
        hoursField.clear();
        salaryField.clear();
        roleBox.getSelectionModel().selectFirst();
        table.getSelectionModel().clearSelection();
    }

    private boolean validateForm() {
        return !nameField.getText().trim().isEmpty() &&
                !addressField.getText().trim().isEmpty() &&
                !emailField.getText().trim().isEmpty() &&
                !ageField.getText().trim().isEmpty() &&
                !hoursField.getText().trim().isEmpty() &&
                !salaryField.getText().trim().isEmpty();
    }

    private void appendToFile(Employee emp) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(emp.toFileString());
            bw.newLine();
        } catch (IOException e) {
            showError("Error saving to file.");
        }
    }

    private void rewriteFile() {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (Employee emp : employeeList) {
                pw.println(emp.toFileString());
            }
        } catch (IOException e) {
            showError("Error updating file.");
        }
    }

    private void loadFromFile() {
        if (!file.exists())
            return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Employee emp = Employee.fromFileString(line);
                if (emp != null) {
                    employeeList.add(emp);
                }
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

    private void goBack() {
        OwnerDashboard dashboard = new OwnerDashboard(app);
        javafx.scene.Scene scene = new javafx.scene.Scene(dashboard.getView(), 1200, 700);
        com.mycompany.restaurant.utils.SceneManager.applyStylesheet(scene);
        com.mycompany.restaurant.utils.SceneManager.switchScene(scene);
    }

    public BorderPane getView() {
        return root;
    }

    // Employee data class
    public static class Employee {
        private String name, address, email, age, hours, salary, role;

        public Employee(String name, String address, String email, String age, String hours, String salary,
                String role) {
            this.name = name;
            this.address = address;
            this.email = email;
            this.age = age;
            this.hours = hours;
            this.salary = salary;
            this.role = role;
        }

        public String toFileString() {
            return "Name: " + name + ", Role: " + role + ", Age: " + age + ", Hours: " + hours +
                    ", Salary: " + salary + ", E-mail: " + email + ", Address: " + address;
        }

        public static Employee fromFileString(String line) {
            try {
                String[] parts = line.split(", ");
                String name = parts[0].split(": ")[1];
                String role = parts[1].split(": ")[1];
                String age = parts[2].split(": ")[1];
                String hours = parts[3].split(": ")[1];
                String salary = parts[4].split(": ")[1];
                String email = parts[5].split(": ")[1];
                String address = parts[6].split(": ")[1];
                return new Employee(name, address, email, age, hours, salary, role);
            } catch (Exception e) {
                return null;
            }
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
