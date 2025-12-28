package com.mycompany.restaurant.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    private static final String DB_DIR = "Database";
    private static final String DB_NAME = "restaurant.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_DIR + File.separator + DB_NAME;

    public static void initializeDatabase() {
        // Ensure Database directory exists
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (Connection conn = connect()) {
            if (conn != null) {
                createTables(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING);
    }

    private static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        // Menu Items Table
        String createMenuTable = "CREATE TABLE IF NOT EXISTS menu_items ("
                + "id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "category TEXT NOT NULL,"
                + "price REAL NOT NULL,"
                + "description TEXT,"
                + "available INTEGER DEFAULT 1"
                + ");";
        stmt.execute(createMenuTable);

        // Orders Table
        // Storing active orders. For a real POS, we might want a 'sales' table for
        // history too.
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "table_name TEXT NOT NULL,"
                + "item_name TEXT NOT NULL,"
                + "size TEXT,"
                + "extras TEXT,"
                + "total_price TEXT,"
                + "status TEXT DEFAULT 'PENDING',"
                + "order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";
        stmt.execute(createOrdersTable);

        // Inventory Table
        String createInventoryTable = "CREATE TABLE IF NOT EXISTS inventory ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "quantity REAL NOT NULL,"
                + "unit TEXT NOT NULL,"
                + "min_stock REAL DEFAULT 10.0"
                + ");";
        stmt.execute(createInventoryTable);

        System.out.println("Database tables initialized.");
    }
}
