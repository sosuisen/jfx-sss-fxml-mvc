module com.sosuisha {
    requires static lombok;

    requires javafx.controls;
    requires javafx.fxml;

    opens com.sosuisha to javafx.fxml, javafx.graphics;
}
