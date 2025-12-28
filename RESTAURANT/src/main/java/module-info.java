module com.mycompany.restaurant {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.base;
    requires java.sql;

    exports com.mycompany.restaurant;
    exports com.mycompany.restaurant.views;
    exports com.mycompany.restaurant.utils;
    exports com.mycompany.restaurant.models;
}
