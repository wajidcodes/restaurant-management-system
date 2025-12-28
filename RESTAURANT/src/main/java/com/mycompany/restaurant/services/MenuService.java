package com.mycompany.restaurant.services;

import com.mycompany.restaurant.models.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

/**
 * MenuService - Manages all food and drink items
 */
public class MenuService {

    private static MenuService instance;
    private final ObservableList<MenuItem> menuItems;

    private MenuService() {
        menuItems = FXCollections.observableArrayList();
        loadFromDatabase();
        if (menuItems.isEmpty()) {
            initializeDefaultData();
        }
    }

    public static MenuService getInstance() {
        if (instance == null) {
            instance = new MenuService();
        }
        return instance;
    }

    private void loadFromDatabase() {
        menuItems.clear();
        String query = "SELECT * FROM menu_items WHERE available = 1";

        try (java.sql.Connection conn = com.mycompany.restaurant.utils.DatabaseHelper.connect();
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                menuItems.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("description")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeDefaultData() {
        // Pizzas
        add(new MenuItem(0, "Chicken Fajita", "Pizza", 1200, "Spicy chicken with onions and capsicum"));
        add(new MenuItem(0, "Pepperoni Feast", "Pizza", 1300, "Loaded with pepperoni slices"));
        add(new MenuItem(0, "BBQ Chicken", "Pizza", 1250, "Chicken with smoke BBQ sauce"));
        add(new MenuItem(0, "Veggie Lover", "Pizza", 1100, "Mushrooms, onions, tomatoes, olives"));
        add(new MenuItem(0, "Cheese Lover", "Pizza", 1150, "Double cheese margarita"));
        add(new MenuItem(0, "Achari Chicken", "Pizza", 1250, "Traditional pickle spice flavor"));

        // Burgers
        add(new MenuItem(0, "Zinger Burger", "Burger", 550, "Crispy fried chicken fillet with mayo"));
        add(new MenuItem(0, "Cheesy Sizzler", "Burger", 780, "Loaded with cheese and spicy sauce"));
        add(new MenuItem(0, "Beef Smash", "Burger", 750, "Double beef patty with cheese"));
        add(new MenuItem(0, "Grilled Chicken", "Burger", 600, "Healthy grilled breast with lettuce"));
        add(new MenuItem(0, "Chapli Burger", "Burger", 500, "Traditional spicy beef patty"));
        add(new MenuItem(0, "Tower Burger", "Burger", 850, "Double zinger fillets with cheese"));

        // Sides (Fries, Nuggets)
        add(new MenuItem(0, "Regular Fries", "Sides", 250, "Crispy salted fries"));
        add(new MenuItem(0, "Mayo Garlic Fries", "Sides", 350, "Fries topped with special mayo sauce"));
        add(new MenuItem(0, "Chicken Nuggets (6pcs)", "Sides", 400, "Golden fried chicken bites"));
        add(new MenuItem(0, "Chicken Nuggets (10pcs)", "Sides", 650, "Family share box"));
        add(new MenuItem(0, "Garlic Bread", "Sides", 200, "Toasted bread with garlic butter"));

        // BBQ
        add(new MenuItem(0, "Chicken Tikka", "BBQ", 450, "Quarter leg piece grilled"));
        add(new MenuItem(0, "Malai Boti", "BBQ", 600, "Creamy boneless chicken pieces (6pcs)"));
        add(new MenuItem(0, "Seekh Kabab", "BBQ", 550, "Spicy minced beef kababs (4pcs)"));
        add(new MenuItem(0, "Reshmi Kabab", "BBQ", 600, "Soft minced chicken kababs"));

        // Drinks
        add(new MenuItem(0, "Coca Cola 500ml", "Drinks", 120, "Chilled bottle"));
        add(new MenuItem(0, "Sprite 500ml", "Drinks", 120, "Chilled bottle"));
        add(new MenuItem(0, "Fanta 500ml", "Drinks", 120, "Chilled bottle"));
        add(new MenuItem(0, "Mint Margarita", "Drinks", 290, "Fresh mint, lime and soda"));
        add(new MenuItem(0, "Fresh Lime", "Drinks", 150, "Soda with lime juice"));
        add(new MenuItem(0, "Mineral Water", "Drinks", 80, "Small bottle"));
        add(new MenuItem(0, "Karak Chai", "Drinks", 100, "Strong tea"));
    }

    public void add(MenuItem item) {
        String sql = "INSERT INTO menu_items(name, category, price, description) VALUES(?,?,?,?)";

        try (java.sql.Connection conn = com.mycompany.restaurant.utils.DatabaseHelper.connect();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getName());
            pstmt.setString(2, item.getCategory());
            pstmt.setDouble(3, item.getPrice());
            pstmt.setString(4, item.getDescription());
            pstmt.executeUpdate();

            // Reload to get ID
            loadFromDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(MenuItem item) {
        String sql = "DELETE FROM menu_items WHERE name = ?"; // Ideally use ID
        try (java.sql.Connection conn = com.mycompany.restaurant.utils.DatabaseHelper.connect();
                java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getName());
            pstmt.executeUpdate();

            menuItems.remove(item);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<MenuItem> getAllItems() {
        return menuItems;
    }

    public ObservableList<MenuItem> getItemsByCategory(String category) {
        if (category == null || category.equals("All")) {
            return menuItems;
        }
        return menuItems.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
}
