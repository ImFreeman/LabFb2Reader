package com.example.labaparser;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PopupController {

    @FXML
    private Button okButton;

    @FXML
    void OnOkClick(MouseEvent event)
    {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

}

