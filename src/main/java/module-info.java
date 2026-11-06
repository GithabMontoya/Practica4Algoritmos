module com.example.p4algoritmos {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.p4algoritmos to javafx.fxml;
    exports com.example.p4algoritmos;
}