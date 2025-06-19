package com.sosuisha;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the main.fxml
 * 
 * This class must be specified as the fx:controller in main.fxml.
 */
public class MainController {
    @FXML
    private Label messageLabel;

    private Model model;

    public MainController(Model model) {
        // Notice that @FXML-annotated fields (e.g., messageLabel) have not been loaded yet.
        this.model = model;
    }

    @FXML
    private void initialize() {
        // initialize() is called after main.fxml is loaded.
        messageLabel.setText(model.getMessage());
    }
}
