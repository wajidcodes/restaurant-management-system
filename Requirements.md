# System Requirements - Restaurant Management System

This document outlines the functional and non-functional requirements for the Restaurant Management System.

## 📋 Functional Requirements

### 1. User Authentication & Authorization
- **Role-Based Login**: Support for Owner, Waiter, and Front Desk roles.
- **Secure Access**: Each role must have access only to their specific dashboards and features.

### 2. Order Management
- **Order Placement**: Waiters must be able to select menu items and place orders for specific tables.
- **Real-time Tracking**: Orders must be tracked from "Pending" to "Completed".
- **Dynamic Pricing**: Automatic calculation of total bill based on selected items.

### 3. Menu Management
- **Category Support**: Organize menu items into categories (e.g., Appetizers, Main Course, Drinks).
- **CRUD Operations**: Ability for Owners to add, update, or remove menu items.

### 4. Inventory Management
- **Stock Tracking**: Monitor the quantity of items available in the inventory.
- **Automatic Deductions**: Deduct stock when orders are placed (if applicable).

### 5. Analytics & Reporting
- **Sales Statistics**: Display total revenue, popular items, and daily sales reports.
- **Financial Overview**: Provide Owners with a summary of restaurant performance.

## ⚙️ Non-Functional Requirements

- **Performance**: The UI should remain responsive even under heavy load; database queries should be optimized.
- **Usability**: The interface must be intuitive, requiring minimal training for staff.
- **Maintainability**: Follow MVC patterns and clean code standards for easy future updates.
- **Data Integrity**: Ensure database consistency across all transactions.
- **Aesthetics**: Provide a modern, professional look (Glassmorphism design).

## 💻 Hardware & Software Requirements

- **Operating System**: Windows, macOS, or Linux.
- **Memory**: Minimum 4GB RAM (8GB recommended).
- **Storage**: At least 100MB of free disk space for the application and database.
- **Runtime**: Java Runtime Environment (JRE) 21.
