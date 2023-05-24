module com.example.bookclubdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.bookclubdesktop to javafx.fxml;
    exports com.example.bookclubdesktop;
}