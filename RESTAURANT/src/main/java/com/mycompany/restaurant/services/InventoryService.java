package com.mycompany.restaurant.services;

import com.mycompany.restaurant.models.InventoryItem;
import com.mycompany.restaurant.utils.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryService {

    private static InventoryService instance;
    private final ObservableList<InventoryItem> inventory;

    private InventoryService() {
        inventory = FXCollections.observableArrayList();
        loadFromDatabase();
        if (inventory.isEmpty()) {
            initializeDefaultData();
        }
    }

    private void initializeDefaultData() {
        // Raw Materials
        addItem("Burger Buns", 100, "pcs");
        addItem("Pizza Dough", 50, "pcs");
        addItem("Chicken Meat", 20, "kg");
        addItem("Beef Mince", 15, "kg");
        addItem("Mozzarella Cheese", 10, "kg");
        addItem("Cheddar Cheese", 5, "kg");
        addItem("Cooking Oil", 30, "liters");
        addItem("Frozen Fries", 50, "kg");
        addItem("Nuggets", 10, "kg");

        // Drinks Stock
        addItem("Coke Bottles (500ml)", 100, "units");
        addItem("Sprite Bottles (500ml)", 100, "units");
        addItem("Fanta Bottles (500ml)", 50, "units");
        addItem("Water Bottles", 200, "units");

        // Fresh
        addItem("Onions", 10, "kg");
        addItem("Tomatoes", 10, "kg");
        addItem("Lettuce", 5, "kg");
        addItem("Sauces (Mayo/Ketchup)", 20, "liters");
    }

    public static InventoryService getInstance() {
        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    private void loadFromDatabase() {
        inventory.clear();
        String query = "SELECT * FROM inventory";

        try (java.sql.Connection conn = DatabaseHelper.connect();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                inventory.add(new InventoryItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("quantity"),
                        rs.getString("unit"),
                        rs.getDouble("min_stock")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItem(String name, double qty, String unit) {
        String sql = "INSERT INTO inventory(name, quantity, unit) VALUES(?,?,?)";

        try (java.sql.Connection conn = DatabaseHelper.connect();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, qty);
            pstmt.setString(3, unit);
            pstmt.executeUpdate();
            loadFromDatabase(); // Reload to get ID

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStock(InventoryItem item, double newQty) {
        String sql = "UPDATE inventory SET quantity = ? WHERE id = ?";

        try (java.sql.Connection conn = DatabaseHelper.connect();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newQty);
            pstmt.setInt(2, item.getItemId());
            pstmt.executeUpdate();

            item.setCurrentStock(newQty); // Update memory

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<InventoryItem> getInventory() {
        return inventory;
    }
}
