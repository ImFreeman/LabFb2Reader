module com.example.labaparser {
    requires javafx.controls;
    requires javafx.fxml;
    requires checker;
    requires java.xml;
    //requires fb2parser;


    opens com.example.labaparser to javafx.fxml;
    exports com.example.labaparser;
}