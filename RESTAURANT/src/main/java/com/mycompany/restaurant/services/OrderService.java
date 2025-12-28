package com.mycompany.restaurant.services;

import com.mycompany.restaurant.views.WaiterOrderView.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * OrderService - Manages active orders across the system
 */
public class OrderService {

    private static OrderService instance;
    private final ObservableList<Order> activeOrders;

    private OrderService() {
        activeOrders = FXCollections.observableArrayList();
        loadActiveOrders();
    }

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    private void loadActiveOrders() {
        activeOrders.clear();
        String query = "SELECT * FROM orders WHERE status != 'COMPLETED'";

        try (java.sql.Connection conn = com.mycompany.restaurant.utils.DatabaseHelper.connect();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getString("table_name"),
                        rs.getString("item_name"),
                        rs.getString("size"),
                        rs.getString("extras"),
                        rs.getString("total_price"));
                activeOrders.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (activeOrders.isEmpty()) {
            initializeSampleData();
        }
    }

    private void initializeSampleData() {
        // Sample orders for demonstration
        addOrder(new Order("Table 5", "Achari Chicken", "Large", "Extra Cheese", "Rs. 1550"));
        addOrder(new Order("Table 2", "Cheesy Sizzler", "", "None", "Rs. 780"));
        addOrder(new Order("Table 8", "Coca Cola 500ml", "", "Cold", "Rs. 120"));
        addOrder(new Order("Table 1", "Zinger Burger", "", "Cheese", "Rs. 550"));

        // Additional orders to fill rows
        addOrder(new Order("Table 3", "Pepperoni Feast", "Medium", "None", "Rs. 1300"));
        addOrder(new Order("Table 4", "Chicken Nuggets (10pcs)", "", "Sauce", "Rs. 650"));
        addOrder(new Order("Table 6", "Mint Margarita", "", "Ice", "Rs. 290"));
    }

    public void addOrder(Order order) {
        activeOrders.add(order);
        String sql = "INSERT INTO orders(table_name, item_name, size, extras, total_price, status) VALUES(?,?,?,?,?,'PENDING')";

        try (java.sql.Connection conn = com.mycompany.restaurant.utils.DatabaseHelper.connect();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, order.getTable());
            pstmt.setString(2, order.getItem());
            pstmt.setString(3, order.getSize());
            pstmt.setString(4, order.getExtras());
            pstmt.setString(5, order.getTotal());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeOrder(Order order) {
        activeOrders.remove(order);
        // Best effort delete
        String sql = "UPDATE orders SET status = 'COMPLETED' WHERE table_name=? AND item_name=? AND total_price=? AND status!='COMPLETED'";
        try (java.sql.Connection conn = com.mycompany.restaurant.utils.DatabaseHelper.connect();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, order.getTable());
            pstmt.setString(2, order.getItem());
            pstmt.setString(3, order.getTotal());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public java.util.Set<String> getOccupiedTables() {
        return activeOrders.stream()
                .map(Order::getTable)
                .collect(java.util.stream.Collectors.toSet());
    }

    public ObservableList<Order> getActiveOrders() {
        return activeOrders;
    }
    // --- Analytics Methods ---

    public java.util.Map<String, Integer> getSalesByItemStats() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        String sql = "SELECT item_name, COUNT(*) as count FROM orders GROUP BY item_name";

        try (java.sql.Connection conn = com.mycompany.restaurant.utils.DatabaseHelper.connect();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                stats.put(rs.getString("item_name"), rs.getInt("count"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    public double getTotalRevenue() {
        double totalRevenue = 0;
        String sql = "SELECT total_price FROM orders";

        try (java.sql.Connection conn = com.mycompany.restaurant.utils.DatabaseHelper.connect();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String priceStr = rs.getString("total_price");
                // Remove "Rs. " prefix and non-numeric characters except decimal point
                if (priceStr != null) {
                    priceStr = priceStr.replaceAll("[^0-9.]", "");
                    if (!priceStr.isEmpty()) {
                        totalRevenue += Double.parseDouble(priceStr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }
}
