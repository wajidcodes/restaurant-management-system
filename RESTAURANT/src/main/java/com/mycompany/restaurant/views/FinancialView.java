package com.mycompany.restaurant.views;

import com.mycompany.restaurant.RestaurantApp;
import com.mycompany.restaurant.utils.DatabaseHelper;
import com.mycompany.restaurant.utils.AnimationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class FinancialView {

    private BorderPane root;
    private RestaurantApp app;
    private com.mycompany.restaurant.services.OrderService orderService;

    public FinancialView(RestaurantApp app) {
        this.app = app;
        this.orderService = com.mycompany.restaurant.services.OrderService.getInstance();
        createView();
    }

    private void createView() {
        root = new BorderPane();
        root.getStyleClass().add("scene-root");
        root.setPadding(new Insets(30));

        // Header
        VBox topBar = new VBox(10);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 30, 0));

        Text title = new Text("Financial Analytics");
        title.getStyleClass().add("page-title");
        topBar.getChildren().add(title);
        root.setTop(topBar);

        // Content
        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setAlignment(Pos.CENTER);

        // 1. Sales by Item Chart (Pie)
        VBox itemChartBox = createItemSalesChart();
        grid.add(itemChartBox, 0, 0);

        // 2. Revenue Summary Card
        VBox summaryBox = createRevenueSummary();
        grid.add(summaryBox, 1, 0);

        root.setCenter(grid);
        AnimationUtils.fadeIn(root, 500);
    }

    private VBox createItemSalesChart() {
        VBox box = new VBox(10);
        box.setPrefSize(500, 400);
        box.getStyleClass().add("glass-card");
        box.setPadding(new Insets(20));

        Label lbl = new Label("Sales by Item");
        lbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        PieChart pieChart = new PieChart();

        // Fetch Data from Service
        Map<String, Integer> data = orderService.getSalesByItemStats();

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        box.getChildren().addAll(lbl, pieChart);
        return box;
    }

    private VBox createRevenueSummary() {
        VBox box = new VBox(20);
        box.setPrefSize(400, 400);
        box.getStyleClass().add("glass-card");
        box.setPadding(new Insets(30));
        box.setAlignment(Pos.CENTER);

        // Fetch Total Revenue from Service
        double totalRevenue = orderService.getTotalRevenue();

        Text title = new Text("Total Revenue");
        title.getStyleClass().add("label-modern");

        Text amount = new Text("Rs. " + (int) totalRevenue);
        amount.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-fill: #27AE60;");

        box.getChildren().addAll(title, amount);

        return box;
    }

    public BorderPane getView() {
        return root;
    }
}
