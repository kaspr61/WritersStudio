module com.team34 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.team34 to javafx.fxml;
    exports com.team34;
}