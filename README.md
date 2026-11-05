# Restaurant Management System (RESTAURANT)

A professional Point of Sale (POS) application built with Java and JavaFX, designed for efficient restaurant management.

## 🌟 Overview

The Restaurant Management System is a modular application focusing on role-based access control, real-time order management, and detailed financial analytics. It features a modern UI with glassmorphism effects and smooth transitions.

## 🚀 Key Features

- **Role-Based Access Control**: Tailored views and permissions for Owners, Waiters, and Front Desk staff.
- **Interactive Order Management**: Real-time order placement, automatic price calculation, and bill generation.
- **Kitchen Sync**: Instant display of pending orders for kitchen staff.
- **Analytics Dashboard**: Comprehensive financial reports and sales statistics.
- **Inventory Management**: Track and manage stock levels in real-time.
- **Modern UX**: Premium UI with animations and custom CSS.

## 🛠️ Technology Stack

- **Language**: Java 21
- **UI Framework**: JavaFX 21
- **Persistence**: SQLite (via JDBC)
- **Build Tool**: Maven

## 📂 Project Structure

```text
RESTAURANT/
├── src/main/java/com/mycompany/restaurant/
│   ├── RestaurantApp.java      # Application entry point
│   ├── controllers/            # UI event handlers
│   ├── services/               # Business logic (OrderService, MenuService, etc.)
│   ├── models/                 # Data entities (Order, MenuItem, etc.)
│   └── utils/                  # Utility classes (DatabaseHelper, SceneManager)
├── src/main/resources/         # FXML files, CSS, and assets
└── pom.xml                     # Maven configuration
```

## ▶️ How to Run

### Prerequisites

- JDK 21
- Apache Maven 3.9 or later

### Run the app

```bash
cd RESTAURANT
mvn javafx:run
```

The app starts with the login screen. Data is stored in a local SQLite database, so no database server or extra setup is needed.

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.
