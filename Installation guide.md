# Installation Guide - Restaurant Management System

Follow these steps to set up the development environment and run the Restaurant Management System on your local machine.

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 21**: The project requires JDK 21 or higher. You can download it from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/).
- **Apache Maven**: Used for dependency management and building the project.
- **SQLite**: (Optional) For manually inspecting the database, though the app handles connection automatically.

## 🛠️ Step-by-Step Installation

### 1. Clone the Repository
Open your terminal and clone the project:
```bash
git clone https://github.com/wajidcodes/restaurant-management-system.git
cd Restaurant Management System
```

### 2. Configure the Environment
Ensure your `JAVA_HOME` environment variable is set to your JDK 21 installation path.

### 3. Build the Project
Use Maven to clean and install the required dependencies:
```bash
mvn clean install
```

### 4. Run the Application
You can run the application directly using the JavaFX Maven plugin:
```bash
mvn javafx:run
```

## 🖥️ IDE Setup (Optional)

### IntelliJ IDEA / Eclipse / VS Code
- Import the project as a **Maven** project.
- Ensure the project SDK is set to **Java 21**.
- The IDE should automatically download the required JavaFX and SQLite dependencies defined in `pom.xml`.

## 🗄️ Database Initialization
The application uses SQLite. Upon the first run, `DatabaseHelper.java` will automatically initialize the `restaurant.db` file and create the necessary tables (`menu_items`, `orders`, `inventory`).
