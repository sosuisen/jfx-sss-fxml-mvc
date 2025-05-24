package com.sosuisha;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label messageLabel;

    private Model model;

    public MainController(Model model) {
        this.model = model;
    }

    public void initialize() {
        // TODO: Add init logic that is called after the FXML has been loaded.
        messageLabel.setText(model.getMessage());
    }
}
